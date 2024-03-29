package ec.peleusi.views.windows;

import java.awt.Image;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import ec.peleusi.controllers.CiudadController;
import ec.peleusi.controllers.EmpresaController;
import ec.peleusi.models.entities.Ciudad;
import ec.peleusi.models.entities.Empresa;
import javax.swing.border.LineBorder;

public class EmpresaCrudFrm extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JTextField txtNombreEmpresa;
	private JTextField txtIdentificacion;
	private JTextField txtDireccion;
	private JTextField txtTelefono;
	private JTextField txtEMail;
	private JTextField txtUrl;
	private JTextField txtFax;
	private JLabel lblFoto;
	@SuppressWarnings("rawtypes")
	private JComboBox cmbCiudad;
	private JButton btnSeleccionarFoto;
	private JButton btnGuardar;
	private JButton btnCancelar;
	private Empresa empresa;
	private File file = null;
	private JButton btnEliminarFoto;

	public EmpresaCrudFrm() {
		setTitle("Datos de la Empresa");
		crearControles();
		crearEventos();
		llenarCiudad();
		empresa = new Empresa();
		recuperarEmpresa();
		txtNombreEmpresa.requestFocus();
	}

	private void recuperarEmpresa() {
		EmpresaController empresaController = new EmpresaController();
		List<Empresa> listaEmpresa;
		listaEmpresa = empresaController.EmpresaList();
		if (listaEmpresa.size() != 0) {
			empresa = listaEmpresa.get(0);
			txtNombreEmpresa.setText(empresa.getNombre());
			txtIdentificacion.setText(empresa.getIdentificacion());
			txtDireccion.setText(empresa.getDireccion());
			txtTelefono.setText(empresa.getTelefono());
			txtFax.setText(empresa.getFax());
			txtEMail.setText(empresa.getEmail());
			txtUrl.setText(empresa.getUrl());
			cmbCiudad.getModel().setSelectedItem(empresa.getCiudad());
			if (empresa.getFoto() != null)
				lblFoto.setIcon(getPhotoIcon(empresa.getFoto()));
			else
				lblFoto.setIcon(new ImageIcon(EmpresaCrudFrm.class.getResource("/ec/peleusi/utils/images/foto.jpg")));
			btnGuardar.setText("Actualizar");
			this.setTitle("Actualizando Empresa");
		} else {
			this.setTitle("Creando nueva Empresa");
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void llenarCiudad() {
		CiudadController ciudadController = new CiudadController();
		List<Ciudad> lista;
		lista = ciudadController.ciudadList();
		cmbCiudad.setModel(new DefaultComboBoxModel(lista.toArray()));
	}

	private boolean isCamposLlenos() {
		boolean llenos = true;
		if (txtIdentificacion.getText().isEmpty() || txtNombreEmpresa.getText().isEmpty()
				|| txtDireccion.getText().isEmpty() || txtTelefono.getText().isEmpty())
			llenos = false;
		return llenos;
	}

	private static byte[] readBytesFromFile(String filePath) throws IOException {
		File inputFile = new File(filePath);
		FileInputStream inputStream = new FileInputStream(inputFile);

		byte[] fileBytes = new byte[(int) inputFile.length()];
		inputStream.read(fileBytes);
		inputStream.close();

		System.out.println("ruta " + fileBytes);
		return fileBytes;

	}

	private void guardarNuevaEmpresa() {
		empresa = new Empresa();
		llenarEntidadAntesDeGuardar();
		EmpresaController empresaController = new EmpresaController();
		String error = empresaController.createEmpresa(empresa);
		if (error == null) {
			JOptionPane.showMessageDialog(null, "Guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			recuperarEmpresa();
		} else {
			JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void actualizarEmpresa() {
		llenarEntidadAntesDeGuardar();
		EmpresaController empresaController = new EmpresaController();
		String error = empresaController.updateEmpresa(empresa);
		if (error == null) {
			JOptionPane.showMessageDialog(null, "Actualizado correctamente", "Éxito", JOptionPane.PLAIN_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private byte[] getPhotoBytes(String path) {
		byte[] photoBytes = null;
		try {
			photoBytes = readBytesFromFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return photoBytes;
	}

	private ImageIcon getPhotoIcon(byte[] image) {
		Image imagen = new ImageIcon(image).getImage();
		imagen = imagen.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
		return new ImageIcon(imagen);
	}

	private void llenarEntidadAntesDeGuardar() {
		Ciudad ciudad = (Ciudad) cmbCiudad.getSelectedItem();
		empresa.setNombre(txtNombreEmpresa.getText());
		empresa.setIdentificacion(txtIdentificacion.getText());
		empresa.setDireccion(txtDireccion.getText());
		empresa.setTelefono(txtTelefono.getText());
		empresa.setFax(txtFax.getText());
		empresa.setEmail(txtEMail.getText());
		empresa.setUrl(txtUrl.getText());
		empresa.setCiudad(ciudad);
		if (empresa.getId() == null)
			empresa.setFoto(getPhotoBytes(String.valueOf(file)));
	}

	private void crearEventos() {
		btnSeleccionarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FileNameExtensionFilter filtro = new FileNameExtensionFilter(
						"Formatos de Archivos JPEG (*.JPG; *.JPEG) ", "jpg", "jpeg");
				JFileChooser archivo = new JFileChooser();
				archivo.addChoosableFileFilter(filtro);
				archivo.setDialogTitle("Escoja una imagen");
				int ventana = archivo.showOpenDialog(null);
				if (ventana == JFileChooser.APPROVE_OPTION) {
					file = archivo.getSelectedFile();
					Image foto = getToolkit().getImage(String.valueOf(file));
					foto = foto.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
					lblFoto.setIcon(new ImageIcon(foto));
					empresa.setFoto(getPhotoBytes(String.valueOf(file)));
				}
			}
		});
		btnEliminarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				file = null;
				lblFoto.setIcon(new ImageIcon(EmpresaCrudFrm.class.getResource("/ec/peleusi/utils/images/foto.jpg")));
				empresa.setFoto(null);
			}
		});

		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!isCamposLlenos()) {
					JOptionPane.showMessageDialog(null, "Datos incompletos, no es posible guardar", "Atención",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (empresa.getId() == null) {
					guardarNuevaEmpresa();
				} else {
					actualizarEmpresa();
				}
			}
		});

		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void crearControles() {
		setIconifiable(true);
		setClosable(true);
		setBounds(100, 100, 599, 375);
		JPanel pnlCabecera = new JPanel();
		pnlCabecera.setLayout(null);
		pnlCabecera.setPreferredSize(new Dimension(200, 70));
		pnlCabecera.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(pnlCabecera, BorderLayout.NORTH);

		btnGuardar = new JButton("Guardar");

		btnGuardar.setIcon(new ImageIcon(EmpresaCrudFrm.class.getResource("/ec/peleusi/utils/images/save.png")));
		btnGuardar.setBounds(154, 20, 130, 39);
		pnlCabecera.add(btnGuardar);

		btnCancelar = new JButton("Cancelar");

		btnCancelar.setIcon(new ImageIcon(EmpresaCrudFrm.class.getResource("/ec/peleusi/utils/images/cancel.png")));
		btnCancelar.setBounds(294, 20, 130, 39);
		pnlCabecera.add(btnCancelar);

		JPanel pnlCuerpo = new JPanel();
		pnlCuerpo.setLayout(null);
		getContentPane().add(pnlCuerpo, BorderLayout.CENTER);

		JLabel lblNombreDeLa = new JLabel("Nombre*");
		lblNombreDeLa.setBounds(10, 27, 65, 14);
		pnlCuerpo.add(lblNombreDeLa);

		txtNombreEmpresa = new JTextField(13);

		txtNombreEmpresa.setColumns(10);
		txtNombreEmpresa.setBounds(79, 24, 304, 20);
		pnlCuerpo.add(txtNombreEmpresa);

		JLabel lblRuc = new JLabel("RUC*");
		lblRuc.setBounds(10, 58, 65, 14);
		pnlCuerpo.add(lblRuc);

		txtIdentificacion = new JTextField(10);
		txtIdentificacion.setBounds(79, 52, 304, 20);
		pnlCuerpo.add(txtIdentificacion);

		JLabel lblCuidad = new JLabel("Cuidad*");
		lblCuidad.setBounds(10, 90, 65, 14);
		pnlCuerpo.add(lblCuidad);

		cmbCiudad = new JComboBox<Ciudad>();
		cmbCiudad.setBounds(79, 84, 304, 20);
		pnlCuerpo.add(cmbCiudad);

		JLabel lblDirecci = new JLabel("Dirección*");
		lblDirecci.setBounds(10, 121, 65, 14);
		pnlCuerpo.add(lblDirecci);

		txtDireccion = new JTextField(10);
		txtDireccion.setBounds(79, 115, 304, 20);
		pnlCuerpo.add(txtDireccion);

		JLabel lblTelfono = new JLabel("Teléfono*");
		lblTelfono.setBounds(10, 152, 65, 14);
		pnlCuerpo.add(lblTelfono);

		txtTelefono = new JTextField(10);
		txtTelefono.setBounds(79, 146, 304, 20);
		pnlCuerpo.add(txtTelefono);

		JLabel lblEMail = new JLabel("E - mail");
		lblEMail.setBounds(10, 211, 65, 14);
		pnlCuerpo.add(lblEMail);

		txtEMail = new JTextField(10);
		txtEMail.setBounds(79, 208, 304, 20);
		pnlCuerpo.add(txtEMail);

		JLabel lblUrl = new JLabel("WebSite");
		lblUrl.setBounds(10, 245, 65, 14);
		pnlCuerpo.add(lblUrl);

		txtUrl = new JTextField(10);
		txtUrl.setBounds(79, 239, 304, 20);
		pnlCuerpo.add(txtUrl);

		JLabel lblFax = new JLabel("Fax");
		lblFax.setBounds(10, 183, 65, 14);
		pnlCuerpo.add(lblFax);

		txtFax = new JTextField(10);
		txtFax.setBounds(79, 177, 304, 20);
		pnlCuerpo.add(txtFax);

		btnSeleccionarFoto = new JButton("");
		btnSeleccionarFoto
				.setIcon(new ImageIcon(EmpresaCrudFrm.class.getResource("/ec/peleusi/utils/images/search.png")));
		btnSeleccionarFoto.setBounds(436, 200, 49, 44);
		pnlCuerpo.add(btnSeleccionarFoto);

		btnEliminarFoto = new JButton("");
		btnEliminarFoto.setIcon(new ImageIcon(EmpresaCrudFrm.class.getResource("/ec/peleusi/utils/images/delete.png")));
		btnEliminarFoto.setBounds(488, 200, 49, 44);
		pnlCuerpo.add(btnEliminarFoto);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(393, 27, 166, 162);
		pnlCuerpo.add(panel);

		lblFoto = new JLabel("");
		panel.add(lblFoto);
		lblFoto.setIcon(new ImageIcon(EmpresaCrudFrm.class.getResource("/ec/peleusi/utils/images/foto.jpg")));

	}
}
