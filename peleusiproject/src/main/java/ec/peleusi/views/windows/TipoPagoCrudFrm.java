package ec.peleusi.views.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ec.peleusi.controllers.TipoPagoController;
import ec.peleusi.models.entities.TipoPago;

public class TipoPagoCrudFrm extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	private JButton btnEliminar;
	private JButton btnGuardar;
	private JButton btnNuevo;
	private JButton btnCancelar;
	private JTextField txtNombre;
	private TipoPago tipoPagoReturn;
	final int limitecaja = 15;
	public TipoPago geTipoPago;

	public TipoPagoCrudFrm() {
		setTitle("Tipo Pago");
		crearControles();
		crearEventos();
		limpiarCampos();
		}

	private void crearControles() {
		
		setBounds(100, 100, 611, 225);

		JPanel panelCabecera = new JPanel();
		panelCabecera.setPreferredSize(new Dimension(200, 70));
		panelCabecera.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(panelCabecera, BorderLayout.NORTH);
		panelCabecera.setLayout(null);

		btnNuevo = new JButton("Nuevo");
		btnNuevo.setIcon(new ImageIcon(TipoPagoCrudFrm.class.getResource("/ec/peleusi/utils/images/new.png")));
		btnNuevo.setBounds(10, 11, 130, 39);
		panelCabecera.add(btnNuevo);

		btnGuardar = new JButton("Guardar");
		btnGuardar.setIcon(new ImageIcon(TipoPagoCrudFrm.class.getResource("/ec/peleusi/utils/images/save.png")));
		btnGuardar.setBounds(150, 11, 130, 39);
		panelCabecera.add(btnGuardar);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setIcon(new ImageIcon(TipoPagoCrudFrm.class.getResource("/ec/peleusi/utils/images/delete.png")));
		btnEliminar.setBounds(290, 11, 130, 39);
		panelCabecera.add(btnEliminar);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setIcon(new ImageIcon(TipoPagoCrudFrm.class.getResource("/ec/peleusi/utils/images/cancel.png")));
		btnCancelar.setBounds(430, 11, 130, 39);
		panelCabecera.add(btnCancelar);

		JPanel panelCuerpo = new JPanel();
		getContentPane().add(panelCuerpo, BorderLayout.CENTER);
		panelCuerpo.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre*");
		lblNombre.setBounds(31, 39, 65, 14);
		panelCuerpo.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setColumns(10);
		txtNombre.setBounds(106, 39, 210, 20);
		panelCuerpo.add(txtNombre);
	
		
			
				
		
		}
	
	private void guardarNuevoTipoPago(){
		
		TipoPago tipoPago = new TipoPago(txtNombre.getText());
		TipoPagoController tipoPagoController =new TipoPagoController();
		String error = tipoPagoController.createTipoPago(tipoPago);
		if(error==null){
			JOptionPane.showMessageDialog(null, "Guardado correctamete", "Exito", JOptionPane.PLAIN_MESSAGE);
			 
			tipoPagoReturn = tipoPago;
			limpiarCampos();
			
			dispose();
		} else{
			JOptionPane.showMessageDialog(null, error, "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	
	private void crearEventos() {
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpiarCampos();
			}
		});
		
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isCamposLlenos()) {
					JOptionPane.showMessageDialog(null, "Datos incompletos, no es posible guardar", "Atención",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				guardarNuevoTipoPago();
			}
		});
		
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void limpiarCampos() {
		txtNombre.setText(" ");
	}

	private boolean isCamposLlenos() {
		boolean llenos = true;
		if (txtNombre.getText().isEmpty())
			llenos = false;
		return llenos;
	}

	public TipoPago getTipoPagoReturn() {
		return tipoPagoReturn;
	}
}
	
	
	
		
	
