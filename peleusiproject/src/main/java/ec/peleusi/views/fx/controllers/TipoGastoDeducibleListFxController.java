package ec.peleusi.views.fx.controllers;

import java.util.List;
import java.util.Optional;
import ec.peleusi.controllers.TipoGastoDeducibleController;
import ec.peleusi.models.entities.TipoGastoDeducible;
import ec.peleusi.utils.fx.AlertsUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TipoGastoDeducibleListFxController extends GenericController {

	@FXML
	public TextField txtNombre;
	@FXML
	private TextField txtBuscar;
	@FXML
	private TableView<TipoGastoDeducible> tblLista = new TableView<TipoGastoDeducible>();
	@FXML
	TableColumn<TipoGastoDeducible, Integer> idCol;
	@FXML
	TableColumn<TipoGastoDeducible, String> nombreCol;
	Integer posicionTipoGastoSeleccionado;
	Boolean actualizar;
	TipoGastoDeducible tipoGastoDeducible = new TipoGastoDeducible();

	@FXML
	private Button btnNuevo;
	@FXML
	private Button btnGuardar;
	@FXML
	private Button btnEliminar;
	@FXML
	private Button btnCancelar;
	@FXML
	private Button btnBuscar;

	private ObservableList<TipoGastoDeducible> tipoGastoDeducibleObs = FXCollections.observableArrayList();

	@FXML
	private void initialize() {
		TipoGastoDeducibleController tipoGastoController = new TipoGastoDeducibleController();
		List<TipoGastoDeducible> listaGastoDeducible = tipoGastoController
				.getTipoGastoDeducibleList(txtBuscar.getText());
		nombreCol.setCellValueFactory(new PropertyValueFactory<TipoGastoDeducible, String>("nombre"));
		if (listaGastoDeducible != null) {
			tipoGastoDeducibleObs = FXCollections.observableList(listaGastoDeducible);
			tblLista.setItems(tipoGastoDeducibleObs);
		}
		tblLista.getSelectionModel().selectedItemProperty()
		.addListener((observable, oldValue, newValue) -> showTipoGastoDeducibleDetails(newValue));
	}

	private void showTipoGastoDeducibleDetails(TipoGastoDeducible tipoGastoDeducible) {
		posicionTipoGastoSeleccionado = tipoGastoDeducibleObs.indexOf(tipoGastoDeducible);
		if (tipoGastoDeducible != null) {
			txtNombre.setText(tipoGastoDeducible.getNombre());
			actualizar = true;
			btnGuardar.setText("Actualizar");
			btnEliminar.setDisable(false);
		} else {
			txtNombre.setText("");
		}
	}

	private void limpiarCampos() {
		actualizar = false;
		txtNombre.setText("");
		txtNombre.requestFocus();
		btnGuardar.setText("Guardar");
		btnEliminar.setDisable(true);
	}

	@FXML
	private void btnGuardarClick() {

		if (isCamposLlenos()) {
			TipoGastoDeducibleController tipoGastoDeducibleController = new TipoGastoDeducibleController();
			String error = "";
			tipoGastoDeducible = tblLista.getSelectionModel().getSelectedItem();
			if (tipoGastoDeducible != null && actualizar == true) {
				tipoGastoDeducible.setNombre(txtNombre.getText());
				error = tipoGastoDeducibleController.updateTipoGastoDeducible(tipoGastoDeducible);
				if (error == null)
					tipoGastoDeducibleObs.set(posicionTipoGastoSeleccionado, tipoGastoDeducible);

			} else {
				tipoGastoDeducible = new TipoGastoDeducible();
				tipoGastoDeducible.setNombre(txtNombre.getText());
				error = tipoGastoDeducibleController.createTipoGastoDeducible(tipoGastoDeducible);
				if (error == null) {
					tipoGastoDeducibleObs.add(tipoGastoDeducible);
					btnNuevoClick();
				}
			}
			if (error == null) {
				AlertsUtil.alertExito("Guardado correctamente");

			} else {
				AlertsUtil.alertError(error);
			}

		} else {
			AlertsUtil.alertWarning("Datos incompletos, no es posible guardar");
		}

	}

	@FXML
	private void btnNuevoClick() {
		limpiarCampos();
	}

	@FXML
	private void btnEliminarClick() {

		if (tblLista.getSelectionModel().getSelectedIndex() >= 0) {

			tipoGastoDeducible = new TipoGastoDeducible();
			tipoGastoDeducible = (TipoGastoDeducible) tipoGastoDeducibleObs.get(posicionTipoGastoSeleccionado);
			Optional<ButtonType> result = AlertsUtil
					.alertConfirmation("Está seguro que desea eliminar: \n" + tipoGastoDeducible.getNombre());
			if (result.get() == ButtonType.OK) {
				TipoGastoDeducibleController tipoGastoDeducibleController = new TipoGastoDeducibleController();
				String error = tipoGastoDeducibleController.deleteTipoGastoDeducible(tipoGastoDeducible);
				if (error == null) {
					tblLista.getItems().remove(tblLista.getSelectionModel().getSelectedIndex());
				} else {
					AlertsUtil.alertError(error);
				}
			} else {

			}
		}
	}

	@FXML
	private void btnBuscarClick(ActionEvent event) {
		TipoGastoDeducibleController tipoGastoDeducibleController = new TipoGastoDeducibleController();
		List<TipoGastoDeducible> tipoGastoDeducibleList = tipoGastoDeducibleController
				.getTipoGastoDeducibleList(txtBuscar.getText());
		if (tipoGastoDeducibleList != null) {
			tipoGastoDeducibleObs = FXCollections.observableList(tipoGastoDeducibleList);
			tblLista.setItems(tipoGastoDeducibleObs);
		} else {
			tipoGastoDeducibleObs.clear();
		}

	}

	@FXML
	private void btnCancelarClick(ActionEvent event) {
		Button btnCloseTab = (Button) event.getSource();
		Scene btnScene = btnCloseTab.getScene();
		TabPane thisTabPane = (TabPane) btnScene.lookup("#tpPrincipal");
		thisTabPane.getTabs().remove(tabIndex);
	}

	private boolean isCamposLlenos() {
		boolean llenos = true;
		if (txtNombre.getText().isEmpty())
			llenos = false;
		return llenos;
	}
}
