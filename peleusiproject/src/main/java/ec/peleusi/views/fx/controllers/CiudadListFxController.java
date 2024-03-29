package ec.peleusi.views.fx.controllers;

import java.util.List;
import java.util.Optional;

import ec.peleusi.controllers.CiudadController;
import ec.peleusi.models.entities.Ciudad;
import ec.peleusi.utils.fx.AlertsUtil;
import ec.peleusi.utils.fx.TableViewUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class CiudadListFxController extends GenericController {

	@FXML
	private TextField txtNombre;
	@FXML
	private TextField txtBuscar;
	@FXML
	VBox vbParaTabla;
	@FXML
	private Button btnNuevo;
	@FXML
	private Button btnGuardar;
	@FXML
	private Button btnEliminar;
	@FXML
	private Button btnCancelar;
	@FXML
	private TableView<Ciudad> tblLista;
	@FXML
	private Pagination pagination;
	@FXML
	private AnchorPane anchorPanePrincipal;
	@FXML
	private ProgressBar progressBar;
	@FXML
	Label lblProgressbar;

	ObservableList<Ciudad> ciudadesList;
	private Integer posicionObjetoEnTabla;
	private CiudadController ciudadController = new CiudadController();
	private String error = null;
	private Ciudad ciudad;
	final static int rowsPerPage = 100;

	@FXML
	private void initialize() {
		anchorPanePrincipal.getStyleClass().add("anchorPanePrincipal");
		crearTabla();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnNuevoClick(null);
			}
		});
		paginar();

	}

	private void paginar() {
		int count = ciudadesList.size() / rowsPerPage;
		if (count < ((double) (ciudadesList.size()) / rowsPerPage))
			count++;
		if (count == 0 && ciudadesList.size() > 0)
			count++;
		if (ciudadesList.size() > 0) {
			pagination.setVisible(true);
			pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
			pagination.setPageCount(count);
			// pagination.setPageFactory(this::createSubList);
			pagination.setPageFactory(new Callback<Integer, Node>() {
				public Node call(Integer pageIndex) {
					int fromIndex = pageIndex * rowsPerPage;
					int toIndex = Math.min(fromIndex + rowsPerPage, ciudadesList.size());
					tblLista.setItems(FXCollections.observableArrayList(ciudadesList.subList(fromIndex, toIndex)));
					return tblLista;
				}
			});

		} else {
			pagination.setVisible(false);
		}
	}

	private void crearTabla() {
		tblLista = new TableView<Ciudad>();
		TableColumn<Ciudad, String> nombreCol = new TableColumn<Ciudad, String>("Nombre");
		nombreCol.setCellValueFactory(new PropertyValueFactory<Ciudad, String>("nombre"));
		nombreCol.setPrefWidth(350);
		tblLista.getColumns().add(nombreCol);
		ciudadesList = FXCollections.observableList(ciudadController.ciudadList());
		tblLista.setItems(ciudadesList);
		final ObservableList<Ciudad> ciudadesListSelected = tblLista.getSelectionModel().getSelectedItems();
		ciudadesListSelected.addListener(escuchaCambiosEnTabla);
		tblLista.setOnKeyReleased(event -> {
			TableViewUtils.tblListaReleased(event, txtBuscar);
		});
	}

	private final ListChangeListener<Ciudad> escuchaCambiosEnTabla = new ListChangeListener<Ciudad>() {
		@Override
		public void onChanged(ListChangeListener.Change<? extends Ciudad> c) {
			cargarObjetoSeleccionadaEnFormulario();
		}
	};

	private void cargarObjetoSeleccionadaEnFormulario() {
		ciudad = (Ciudad) getObjetoSeleccionadoDeTabla();
		if (ciudad != null) {
			posicionObjetoEnTabla = ciudadesList.indexOf(ciudad);
			txtNombre.setText(ciudad.getNombre());
			btnGuardar.setText("Actualizar");
			btnGuardar.setDisable(false);
			btnEliminar.setDisable(false);
		}
	}

	public Object getObjetoSeleccionadoDeTabla() {
		if (tblLista != null) {
			if (tblLista.getSelectionModel().getSelectedItems().size() >= 1) {
				return tblLista.getSelectionModel().getSelectedItems().get(0);
			}
		}
		return null;
	}

	private void guardarNuevo() {
		error = ciudadController.createCiudad(ciudad);
		if (error == null) {
			ciudadesList.add(ciudad);
			txtBuscar.setText(txtNombre.getText());
			btnBuscarClick(null);
			btnNuevoClick(null);
			AlertsUtil.alertExito("Guardado correctamente");
		} else {
			AlertsUtil.alertError(error);
		}
	}

	private void actualizar() {
		error = ciudadController.updateCiudad(ciudad);
		if (error == null) {
			ciudadesList.set(posicionObjetoEnTabla, ciudad);
			txtBuscar.setText(txtNombre.getText());
			btnBuscarClick(null);
			btnNuevoClick(null);
			AlertsUtil.alertExito("Actualizado correctamente");
		} else {
			AlertsUtil.alertError(error);
		}
	}

	private void eliminar() {
		error = ciudadController.deleteCiudad(ciudad);
		if (error == null) {
			ciudadesList.remove(getObjetoSeleccionadoDeTabla());
			btnBuscarClick(null);
			btnNuevoClick(null);
		} else {
			AlertsUtil.alertError(error);
		}
	}

	@FXML
	private void btnNuevoClick(ActionEvent event) {
		ciudad = new Ciudad();
		txtNombre.setText("");
		btnGuardar.setText("Guardar");
		btnEliminar.setDisable(true);
		btnGuardar.setDisable(false);
		txtNombre.requestFocus();
	}

	@FXML
	private void btnGuardarClick(ActionEvent event) {
		ciudad.setNombre(txtNombre.getText());
		if (isCamposLlenos()) {
			if (btnGuardar.getText().toLowerCase().equals("actualizar")) {
				actualizar();
			} else {
				guardarNuevo();
			}
		} else {
			AlertsUtil.alertWarning("Datos incompletos, no es posible guardar");
		}

	}

	@FXML
	private void btnEliminarClick(ActionEvent event) {
		Optional<ButtonType> result = AlertsUtil
				.alertConfirmation("Está seguro que desea eliminar: \n" + ciudad.getNombre());
		if (result.get() == ButtonType.OK) {
			eliminar();
		}
	}

	@FXML
	private void btnBuscarClick(ActionEvent event) {
		List<Ciudad> ciudadList = ciudadController.ciudadList(txtBuscar.getText());
		if (ciudadList != null) {
			ciudadesList = FXCollections.observableList(ciudadList);
			tblLista.setItems(ciudadesList);
		} else {
			ciudadesList.clear();
		}
		paginar();
		btnNuevoClick(null);
		tblLista.requestFocus();
	}

	@FXML
	private void btnCancelarClick(ActionEvent event) {
		Button btnCloseTab = (Button) event.getSource();
		Scene btnScene = btnCloseTab.getScene();
		TabPane thisTabPane = (TabPane) btnScene.lookup("#tpPrincipal");
		thisTabPane.getTabs().remove(tabIndex);
	}

	@FXML
	private void txtBuscarReleased(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			btnBuscarClick(null);
		}
	}

	private boolean isCamposLlenos() {
		boolean llenos = true;
		if (txtNombre.getText().trim().isEmpty())
			llenos = false;
		return llenos;
	}
}
