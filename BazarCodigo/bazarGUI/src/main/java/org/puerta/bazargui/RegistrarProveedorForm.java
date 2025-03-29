package org.puerta.bazargui;

import javax.swing.*;
import java.awt.*;

import resources.HeaderPanel;
import resources.RoundedButton;

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

        JTextField txtNombre = new JTextField();
        JTextField txtRepresentante = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtDireccion = new JTextField();

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
        btnCancelar.setPreferredSize(new Dimension(140, 40));
        btnCancelar.setBackground(Color.DARK_GRAY);
        btnCancelar.setForeground(Color.WHITE);

        botones.add(btnConfirmar);
        botones.add(btnCancelar);
        add(botones, BorderLayout.SOUTH);

        // Acciones
        btnCancelar.addActionListener(_ -> dispose());

        btnConfirmar.addActionListener(_ -> {
            JOptionPane.showMessageDialog(this,
                    "Proveedor registrado correctamente.",
                    "Confirmación",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrarProveedorForm::new);
    }
}
