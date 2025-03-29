
package org.puerta.bazargui;

import javax.swing.*;
import java.awt.*;

import resources.HeaderPanel;
import resources.RoundedButton;

public class EditarProveedorForm extends JFrame {

    private JTextField txtNombre, txtRepresentante, txtCorreo, txtDireccion, txtTelefono;

    public EditarProveedorForm() {
        setTitle("Editar Proveedor");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        add(new HeaderPanel(this, HeaderPanel.SeccionActual.PROVEEDORES), BorderLayout.NORTH);

        // FORMULARIO CENTRAL
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panelForm.setBackground(Color.WHITE);

        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Representante:"));
        txtRepresentante = new JTextField();
        panelForm.add(txtRepresentante);

        panelForm.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelForm.add(txtCorreo);

        panelForm.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panelForm.add(txtDireccion);

        panelForm.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelForm.add(txtTelefono);

        add(panelForm, BorderLayout.CENTER);

        // BOTONES
        RoundedButton btnGuardar = new RoundedButton("Guardar");
        RoundedButton btnCancelar = new RoundedButton("Cancelar");
        btnCancelar.setBackground(Color.DARK_GRAY);
        btnCancelar.setForeground(Color.WHITE);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        btnCancelar.addActionListener(_ -> dispose());

        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditarProveedorForm::new);
    }
}
