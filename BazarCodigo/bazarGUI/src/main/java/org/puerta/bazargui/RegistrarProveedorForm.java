package org.puerta.bazargui;

import javax.swing.*;

import org.puerta.bazardependecias.dto.ProveedorDTO;
import org.puerta.bazarnegocio.bo.ProveedoresBO;

import java.awt.*;

import resources.HeaderPanel;
import resources.RoundedButton;
import resources.RoundedTextField;

public class RegistrarProveedorForm extends JFrame {

    public RegistrarProveedorForm() {
        setTitle("Registrar Proveedor");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        add(new HeaderPanel(this, HeaderPanel.SeccionActual.PROVEEDORES), BorderLayout.NORTH);

        // FORMULARIO CENTRAL
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel lblNombre = new JLabel("Nombre del proveedor:");
        JLabel lblRepresentante = new JLabel("Representante:");
        JLabel lblTelefono = new JLabel("Teléfono:");
        JLabel lblCorreo = new JLabel("Correo:");
        JLabel lblDireccion = new JLabel("Dirección:");

        RoundedTextField txtNombre = new RoundedTextField();
        RoundedTextField txtRepresentante = new RoundedTextField();
        RoundedTextField txtTelefono = new RoundedTextField();
        RoundedTextField txtCorreo = new RoundedTextField();
        RoundedTextField txtDireccion = new RoundedTextField();

        formPanel.add(lblNombre);
        formPanel.add(txtNombre);
        formPanel.add(lblRepresentante);
        formPanel.add(txtRepresentante);
        formPanel.add(lblTelefono);
        formPanel.add(txtTelefono);
        formPanel.add(lblCorreo);
        formPanel.add(txtCorreo);
        formPanel.add(lblDireccion);
        formPanel.add(txtDireccion);

        add(formPanel, BorderLayout.CENTER);

        // BOTONES
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        botones.setBackground(Color.WHITE);

        RoundedButton btnConfirmar = new RoundedButton("Confirmar");
        RoundedButton btnCancelar = new RoundedButton("Cancelar");

        btnConfirmar.setPreferredSize(new Dimension(140, 40));
        btnConfirmar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String representante = txtRepresentante.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String correo = txtCorreo.getText().trim();
            String direccion = txtDireccion.getText().trim();

            // Validaciones
            if (nombre.isEmpty() || !nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio y solo puede contener letras.", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (representante.isEmpty() || !representante.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                JOptionPane.showMessageDialog(this, "El representante es obligatorio y solo puede contener letras.",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!telefono.matches("^\\d{7,15}$")) {
                JOptionPane.showMessageDialog(this, "El teléfono debe contener solo dígitos (7 a 15).", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!correo.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(this, "Correo electrónico inválido.", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (direccion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La dirección es obligatoria.", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Registro
            try {
                ProveedorDTO dto = new ProveedorDTO();
                dto.setNombre(nombre);
                dto.setRepresentante(representante);
                dto.setTelefono(telefono);
                dto.setCorreo(correo);
                dto.setDireccion(direccion);

                new ProveedoresBO().registrarProveedor(dto);

                JOptionPane.showMessageDialog(this, "Proveedor registrado correctamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                new ProveedoresForm().setVisible(true);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar proveedor: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                  new ProveedoresForm().setVisible(true);
                 dispose();
            }
        });

        btnCancelar.setPreferredSize(new Dimension(140, 40));
        btnCancelar.setBackground(Color.DARK_GRAY);
        btnCancelar.setForeground(Color.WHITE);

        botones.add(btnConfirmar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

      
      
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrarProveedorForm::new);
    }
}
