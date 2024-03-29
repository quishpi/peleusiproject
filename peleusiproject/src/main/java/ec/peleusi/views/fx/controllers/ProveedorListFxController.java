package ec.peleusi.views.fx.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import ec.peleusi.controllers.DireccionProveedorController;
import ec.peleusi.controllers.ProveedorController;
import ec.peleusi.controllers.TipoIdentificacionController;
import ec.peleusi.models.entities.Ciudad;
import ec.peleusi.models.entities.DireccionProveedor;
import ec.peleusi.models.entities.Proveedor;
import ec.peleusi.models.entities.TipoIdentificacion;
import ec.peleusi.utils.fx.AlertsUtil;
import ec.peleusi.utils.fx.TableViewUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ProveedorListFxController extends GenericController {
	@FXML
	private TabPane tpnlProveedor;
	@FXML
	private Tab pnlDatos;
	@FXML
	private ComboBox<TipoIdentificacion> cmbTipoIdentificacion;
	@FXML
	private TextField txtIdentificacion;
	@FXML
	private TextField txtRazonSocial;
	@FXML
	private TextField txtDiasCredito;
	@FXML
	private TextField txtPorcentajeDescuento;
	@FXML
	private TextArea txtDescripcion;
	@FXML
	private TextField txtBuscar;
	@FXML
	private TableView<Proveedor> tblLista;
	@FXML
	private Pagination pagination;
	@FXML
	private Tab pnlDireccion;
	@FXML
	private TextField txtCiudad;
	@FXML
	private TextField txtNombre;
	@FXML
	private TextField txtDireccion;
	@FXML
	private TextField txtTelefono;
	@FXML
	private TextField txtCelular;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtCodigoPostal;
	@FXML
	private CheckBox chkPorDefecto;
	@FXML
	private TableView<DireccionProveedor> tblDireccionProveedor;
	@FXML
	TableColumn<DireccionProveedor, String> ciudadCol;
	@FXML
	TableColumn<DireccionProveedor, String> nombreCol;
	@FXML
	TableColumn<DireccionProveedor, String> direccionCol;
	@FXML
	TableColumn<DireccionProveedor, String> telefonoCol;
	@FXML
	TableColumn<DireccionProveedor, Boolean> porDefectoCol;
	@FXML
	private Button btnNuevoDireccion;
	@FXML
	private Button btnAgregar;
	@FXML
	private Button btnEliminarDireccion;

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

	ObservableList<Proveedor> proveedorsList;
	ObservableList<TipoIdentificacion> tipoIdentificacionsList;
	@SuppressWarnings("unused")
	private TipoIdentificacion tipoIdentificacion;
	private Integer posicionObjetoEnTabla;
	private Proveedor proveedor;
	private ProveedorController proveedorController = new ProveedorController();
	private String error = null;
	final static int rowsPerPage = 15;
	Boolean actualizarDireccion = false;

	ObservableList<DireccionProveedor> direccionProveedorsList;
	private DireccionProveedor direccionProveedor;
	private DireccionProveedorController direccionProveedorController = new DireccionProveedorController();
	private Integer posicionObjetoEnTablaDireccion;
	Boolean eliminarDireccion = false;
	Ciudad ciudad = new Ciudad();

	@FXML
	private void initialize() {
		pnlDireccion.setDisable(true);
		crearTabla();		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnNuevoClick(null);
			}
		});
		paginar();
		cargarComboTipoIdentificacion();
	}

	private void paginar() {
		int count = proveedorsList.size() / rowsPerPage;
		if (count < ((double) (proveedorsList.size()) / rowsPerPage))
			count++;
		if (count == 0 && proveedorsList.size() > 0)
			count++;
		if (proveedorsList.size() > 0) {
			pagination.setVisible(true);
			pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
			pagination.setPageCount(count);
			pagination.setPageFactory(new Callback<Integer, Node>() {
				public Node call(Integer pageIndex) {
					int fromIndex = pageIndex * rowsPerPage;
					int toIndex = Math.min(fromIndex + rowsPerPage, proveedorsList.size());
					tblLista.setItems(FXCollections.observableArrayList(proveedorsList.subList(fromIndex, toIndex)));
					return tblLista;
				}
			});

		} else {
			pagination.setVisible(false);
		}
	}

	private void crearTabla() {
		tblLista = new TableView<Proveedor>();
		TableColumn<Proveedor, TipoIdentificacion> tipoIdentificacionCol = new TableColumn<Proveedor, TipoIdentificacion>(
				"Tipo Identificación");
		tipoIdentificacionCol
				.setCellValueFactory(new PropertyValueFactory<Proveedor, TipoIdentificacion>("tipoIdentificacion"));
		tipoIdentificacionCol.setPrefWidth(80);
		tblLista.getColumns().add(tipoIdentificacionCol);
		TableColumn<Proveedor, String> identificacionCol = new TableColumn<Proveedor, String>("Identificacion");
		identificacionCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("identificacion"));
		identificacionCol.setPrefWidth(110);
		tblLista.getColumns().add(identificacionCol);
		TableColumn<Proveedor, String> razonSocialCol = new TableColumn<Proveedor, String>("RazónSocial");
		razonSocialCol.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("razonSocial"));
		razonSocialCol.setPrefWidth(200);
		tblLista.getColumns().add(razonSocialCol);
		proveedorsList = FXCollections.observableList(proveedorController.proveedorList());
		tblLista.setItems(proveedorsList);
		final ObservableList<Proveedor> proveedorsListSelected = tblLista.getSelectionModel().getSelectedItems();
		proveedorsListSelected.addListener(escuchaCambiosEnTabla);
		tblLista.setOnKeyReleased(event -> {
			TableViewUtils.tblListaReleased(event, txtBuscar);
		});
	}

	private final ListChangeListener<Proveedor> escuchaCambiosEnTabla = new ListChangeListener<Proveedor>() {
		@Override
		public void onChanged(ListChangeListener.Change<? extends Proveedor> c) {
			cargarObjetoSeleccionadaEnFormulario();
		}
	};

	public Object getObjetoSeleccionadoDeTabla() {
		if (tblLista != null) {
			if (tblLista.getSelectionModel().getSelectedItems().size() >= 1) {
				return tblLista.getSelectionModel().getSelectedItems().get(0);
			}
		}
		return null;
	}

	private void cargarComboTipoIdentificacion() {
		TipoIdentificacionController tipoIdenficacionController = new TipoIdentificacionController();
		tipoIdentificacionsList = FXCollections.observableList(tipoIdenficacionController.tipoIdentificacionList());
		cmbTipoIdentificacion.setItems(tipoIdentificacionsList);
		System.out.println("hola " + tipoIdentificacionsList);
	}

	@FXML
	private void cmbTipoIdentificacionClick(ActionEvent event) {
		//cmbTipoIdentificacion.getValue().getNombre();
		//System.out.println(" "+ cmbTipoIdentificacion);
		
		
		
		/*cmbTipoIdentificacion.setValue(tipoIdentificacion);
		System.out.println("hola combo " + tipoIdentificacion);
		//System.out.println("hola combo " + tipoIdentificacion);
	    System.out.println (cmbTipoIdentificacion.getValue ());*/
	}
	
	
	private void cargarObjetoSeleccionadaEnFormulario() {
		proveedor = (Proveedor) getObjetoSeleccionadoDeTabla();
		if (proveedor != null) {
			posicionObjetoEnTabla = proveedorsList.indexOf(proveedor);
			cmbTipoIdentificacion.getSelectionModel().select(proveedor.getTipoIdentificacion());
			txtIdentificacion.setText(proveedor.getIdentificacion());
			txtRazonSocial.setText(proveedor.getRazonSocial());
			txtDiasCredito.setText(Integer.toString(proveedor.getDiasCredito()));
			txtPorcentajeDescuento.setText(Double.toString(proveedor.getPorcentajeDescuento()));
			txtDescripcion.setText(proveedor.getDescripcion());
			pnlDireccion.setDisable(false);
			btnGuardar.setText("Actualizar");
			btnGuardar.setDisable(false);
			btnEliminar.setDisable(false);
			crearTablaDireccion(proveedor);			
		}
	}

	private void guardarNuevo() {
		error = proveedorController.createProveedor(proveedor);
		if (error == null) {
			proveedorsList.add(proveedor);
			txtBuscar.setText(txtRazonSocial.getText());
			btnBuscarClick(null);
			AlertsUtil.alertExito("Guardado correctamente");
		} else {
			AlertsUtil.alertError(error);
		}
	}

	private void actualizar() {
		error = proveedorController.updateProveedor(proveedor);
		if (error == null) {
			proveedorsList.set(posicionObjetoEnTabla, proveedor);
			txtBuscar.setText(txtRazonSocial.getText());
			btnBuscarClick(null);
			AlertsUtil.alertExito("Actualizado correctamente");
		} else {
			AlertsUtil.alertError(error);
		}
	}	
	
	private void eliminar() {			
		for (DireccionProveedor direccionProveedor : direccionProveedorsList) {		
			direccionProveedorController.deleteDireccionProveedor(direccionProveedor);			
		}		
		error = proveedorController.deleteProveedor(proveedor);	
		if (error == null) {			
			proveedorsList.remove(getObjetoSeleccionadoDeTabla());			
			tblLista.getItems().remove(tblLista.getSelectionModel().getSelectedIndex());
		} else {
			AlertsUtil.alertError(error);
		}
	}
	

	private void llenarEntidadAntesDeGuardar() {
		tipoIdentificacion = new TipoIdentificacion();
		proveedor.setTipoIdentificacion((TipoIdentificacion) cmbTipoIdentificacion.getValue());
		proveedor.setIdentificacion(txtIdentificacion.getText());
		proveedor.setRazonSocial(txtRazonSocial.getText());
		proveedor.setDiasCredito(Integer.parseInt(txtDiasCredito.getText().toString()));
		proveedor.setPorcentajeDescuento(Double.parseDouble(txtPorcentajeDescuento.getText().toString()));
		proveedor.setDescripcion(txtDescripcion.getText());

	}

	private void limpiarCampos() {
		proveedor = new Proveedor();
		txtIdentificacion.setText("");
		txtRazonSocial.setText("");
		txtDiasCredito.setText("0");
		txtPorcentajeDescuento.setText("0");
		txtDescripcion.setText("");
		btnGuardar.setText("Guardar");
		btnEliminar.setDisable(true);
		btnGuardar.setDisable(false);
		cmbTipoIdentificacion.requestFocus();
		actualizarDireccion = false;
		pnlDireccion.setDisable(true);
		crearTablaDireccion(null);		
	}

	private boolean camposLlenos() {
		boolean llenos = true;
		if (txtIdentificacion.getText().isEmpty() || txtRazonSocial.getText().isEmpty()
				|| txtDiasCredito.getText().isEmpty())
			llenos = false;
		return llenos;
	}

	@FXML
	private void btnNuevoClick(ActionEvent event) {
		limpiarCampos();
		limpiarCamposDireccion();
		SingleSelectionModel<Tab> selectionModel;
		selectionModel = tpnlProveedor.getSelectionModel();
		selectionModel.select(pnlDatos);
	}

	@FXML
	private void btnGuardarClick(ActionEvent event) {
		llenarEntidadAntesDeGuardar();
		if (camposLlenos()) {
			if (btnGuardar.getText().toLowerCase().equals("actualizar")) {
				actualizar();
			} else {
				guardarNuevo();
				pnlDireccion.setDisable(false);
				SingleSelectionModel<Tab> selectionModel;
				selectionModel = tpnlProveedor.getSelectionModel();
				selectionModel.select(pnlDireccion);
				txtCiudad.requestFocus();
				//tpnlProveedor.setEnabledAt(1, true);
				//tpnlProveedor.setSelectedIndex(1);				
			}
		} else {
			AlertsUtil.alertWarning("Datos incompletos, no es posible guardar");
		}
	}

	@FXML
	private void btnEliminarClick(ActionEvent event) {
		Optional<ButtonType> result = AlertsUtil
				.alertConfirmation("Está seguro que desea eliminar: \n" + proveedor.getRazonSocial());
		if (result.get() == ButtonType.OK) {
			eliminar();
			pnlDireccion.setDisable(true);
			tblLista.refresh();
			limpiarCampos();
			SingleSelectionModel<Tab> selectionModel;
			selectionModel = tpnlProveedor.getSelectionModel();
			selectionModel.select(pnlDatos);
		}
	}

	@FXML
	private void btnCancelarClick(ActionEvent event) {
		Button btnCloseTab = (Button) event.getSource();
		Scene btnScene = btnCloseTab.getScene();
		TabPane thisTabPane = (TabPane) btnScene.lookup("#tpPrincipal");
		thisTabPane.getTabs().remove(tabIndex);
	}

	@FXML
	private void btnBuscarClick(ActionEvent event) {
		List<Proveedor> proveedorList = proveedorController.getProveedorList(txtBuscar.getText());
		if (proveedorList != null) {
			proveedorsList = FXCollections.observableList(proveedorList);
			tblLista.setItems(proveedorsList);
		} else {
			proveedorsList.clear();
		}
		proveedor = proveedorList.get(0);		
		tblLista.requestFocus();
	}

	private void crearTablaDireccion(Proveedor proveedorList) {
		direccionProveedorsList = FXCollections
				.observableList(direccionProveedorController.direccionProveedorIdList(proveedorList));
		tblDireccionProveedor.setItems(direccionProveedorsList);
		ciudadCol.setCellValueFactory(new PropertyValueFactory<DireccionProveedor, String>("ciudad"));
		nombreCol.setCellValueFactory(new PropertyValueFactory<DireccionProveedor, String>("nombre"));
		direccionCol.setCellValueFactory(new PropertyValueFactory<DireccionProveedor, String>("direccion"));
		telefonoCol.setCellValueFactory(new PropertyValueFactory<DireccionProveedor, String>("telefono"));
		porDefectoCol.setCellValueFactory(new PropertyValueFactory<DireccionProveedor, Boolean>("porDefecto"));
		porDefectoCol.setCellFactory(CheckBoxTableCell.forTableColumn(porDefectoCol));
		limpiarCamposDireccion();
		final ObservableList<DireccionProveedor> direccionProveedorsListSeleccionar = tblDireccionProveedor
				.getSelectionModel().getSelectedItems();
		direccionProveedorsListSeleccionar.addListener(escuchaCambiosEnTablaDireccion);
	}

	private final ListChangeListener<DireccionProveedor> escuchaCambiosEnTablaDireccion = new ListChangeListener<DireccionProveedor>() {
		@Override
		public void onChanged(ListChangeListener.Change<? extends DireccionProveedor> c) {
			cargarObjetoSeleccionadaEnFormularioDireccion();
			actualizarDireccion = true;			
			if (direccionProveedor.getPorDefecto()== true)
			{
				btnEliminarDireccion.setDisable(true);
				chkPorDefecto.setSelected(true);
				chkPorDefecto.setDisable(true);
			}		
		}
	};

	public Object getObjetoSeleccionadoDeTablaDireccion() {
		if (tblDireccionProveedor != null) {
			if (tblDireccionProveedor.getSelectionModel().getSelectedItems().size() >= 1) {
				return tblDireccionProveedor.getSelectionModel().getSelectedItems().get(0);
			}
		}
		return null;
	}

	private void cargarObjetoSeleccionadaEnFormularioDireccion() {
		direccionProveedor = (DireccionProveedor) getObjetoSeleccionadoDeTablaDireccion();
		if (direccionProveedor != null) {
			posicionObjetoEnTablaDireccion = direccionProveedorsList.indexOf(direccionProveedor);
			txtCiudad.setText(direccionProveedor.getCiudad().getNombre());
			txtNombre.setText(direccionProveedor.getNombre());
			txtDireccion.setText(direccionProveedor.getDireccion());
			txtTelefono.setText(direccionProveedor.getTelefono());
			txtCelular.setText(direccionProveedor.getCelular());
			txtEmail.setText(direccionProveedor.getEmail());
			txtCodigoPostal.setText(direccionProveedor.getCodigoPostal());
			chkPorDefecto.setSelected(direccionProveedor.getPorDefecto());
			btnAgregar.setText("Actualizar");
			btnAgregar.setDisable(false);
			btnEliminarDireccion.setDisable(false);			
		}
	}

	private void guardarNuevoDireccion(Proveedor proveedorEntrada) {
		direccionProveedor = llenarEntidadAntesDeGuardarDireccion(proveedorEntrada, false);
		error = direccionProveedorController.createDireccionProveedor(direccionProveedor);
		if (error == null) {
			direccionProveedorsList.add(direccionProveedor);
			AlertsUtil.alertExito("Guardado correctamente");			
		} else {
			AlertsUtil.alertError(error);
		}
	}

	private void actualizarDireccion(DireccionProveedor direccionProveedor) {
		direccionProveedor = llenarEntidadAntesDeGuardarDireccion(proveedor, true);
		direccionProveedorController = new DireccionProveedorController();
		error = direccionProveedorController.updateDireccionProveedor(direccionProveedor);
		if (error == null) {
			direccionProveedorsList.set(posicionObjetoEnTablaDireccion, direccionProveedor);
			AlertsUtil.alertExito("Actualizado correctamente");			
		} else {
			AlertsUtil.alertError(error);
		}
	}

	private void eliminarDireccion() {
		if (direccionProveedor.getPorDefecto()== false){
		error = direccionProveedorController.deleteDireccionProveedor(direccionProveedor);
			if (error == null) {				
				direccionProveedorsList.remove(getObjetoSeleccionadoDeTablaDireccion());				
				limpiarCamposDireccion();
			} else {
				AlertsUtil.alertError(error);
			}			
		}	
	}	

	private DireccionProveedor llenarEntidadAntesDeGuardarDireccion(Proveedor proveedor, Boolean actualizarDireccion) {
		if (actualizarDireccion == false) {
			direccionProveedor = new DireccionProveedor();
		}
		direccionProveedor.setProveedor(proveedor);
		direccionProveedor.setCiudad(ciudad);					
		direccionProveedor.setNombre(txtNombre.getText());
		direccionProveedor.setDireccion(txtDireccion.getText().trim());
		direccionProveedor.setTelefono(txtTelefono.getText());
		direccionProveedor.setCelular(txtCelular.getText());
		direccionProveedor.setEmail(txtEmail.getText());
		direccionProveedor.setCodigoPostal(txtCodigoPostal.getText());
		direccionProveedor.setPorDefecto(chkPorDefecto.isSelected());
		return direccionProveedor;
	}

	private void limpiarCamposDireccion() {
		txtNombre.setText("");
		txtDireccion.setText("");
		txtTelefono.setText("");
		txtCelular.setText("");
		txtEmail.setText("");
		txtCodigoPostal.setText("");
		chkPorDefecto.setSelected(false);
		btnAgregar.setText("Agregar");
		btnEliminarDireccion.setDisable(true);
		btnAgregar.setDisable(false);
		txtNombre.requestFocus();
		actualizarDireccion = false;
	}

	private boolean camposLlenosDireccion() {
		boolean llenosDireccion = true;
		if (txtNombre.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtTelefono.getText().isEmpty())
			llenosDireccion = false;
		return llenosDireccion;
	}

	@FXML
	private void btnNuevoDireccionClick(ActionEvent event) {
		limpiarCamposDireccion();
	}

	@FXML
	private void btnAgregarClick(ActionEvent event) {

		if (camposLlenosDireccion()) {
			if (btnAgregar.getText().toLowerCase().equals("actualizar")) {
				actualizarDireccion(direccionProveedor);
				 limpiarCamposDireccion();
			} else {
				guardarNuevoDireccion(proveedor);
				limpiarCamposDireccion();
				crearTablaDireccion(proveedor);
			}
		} else {
			AlertsUtil.alertWarning("Datos incompletos, no es posible guardar");
		}
	}

	@FXML
	private void btnEliminarDireccionClick(ActionEvent event) {
		Optional<ButtonType> result = AlertsUtil
				.alertConfirmation("Está seguro que desea eliminar: \n" + direccionProveedor.getNombre());
		if (result.get() == ButtonType.OK) {
			eliminarDireccion();
		}
	}

	@FXML
	private void btnBuscarCiudadClick(ActionEvent event) {		
		Parent parent = null;
		Stage stage = new Stage();
		CiudadListModalController control = new CiudadListModalController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../designs/CiudadListModalFx.fxml"));
		loader.setController(control);
		try{
			parent = (Parent) loader.load();
			stage.setTitle("Lista de Ciudad");
			stage.setScene(new Scene(parent));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			ciudad = control.getCiudad();	
			txtCiudad.setText(ciudad.getNombre());
		}	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
}
