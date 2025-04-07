
package org.puerta.bazargui;

import javax.swing.*;

import org.puerta.bazardependecias.dto.ProveedorDTO;
import org.puerta.bazarnegocio.bo.ProveedoresBO;

import java.awt.*;

import resources.HeaderPanel;
import resources.RoundedButton;

public class EditarProveedorForm extends JFrame {

    private JTextField txtNombre, txtRepresentante, txtCorreo, txtDireccion, txtTelefono;

    private ProveedorDTO proveedor;

    public EditarProveedorForm(ProveedorDTO proveedor) {
        this.proveedor = proveedor;

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
        txtNombre.setText(proveedor.getNombre());
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Representante:"));
        txtRepresentante = new JTextField();
        txtRepresentante.setText(proveedor.getRepresentante());
        panelForm.add(txtRepresentante);

        panelForm.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        txtCorreo.setText(proveedor.getCorreo());
        panelForm.add(txtCorreo);

        panelForm.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        txtDireccion.setText(proveedor.getDireccion());
        panelForm.add(txtDireccion);

        panelForm.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        txtTelefono.setText(proveedor.getTelefono());
        panelForm.add(txtTelefono);

        add(panelForm, BorderLayout.CENTER);

        // BOTONES
        RoundedButton btnGuardar = new RoundedButton("Guardar");
        btnGuardar.addActionListener(_e -> {
            String nombre = txtNombre.getText().trim();
            String representante = txtRepresentante.getText().trim();
            String correo = txtCorreo.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();

            // Validaciones simples
            if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                JOptionPane.showMessageDialog(this, "Nombre inválido (solo letras).");
                return;
            }

            if (!representante.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                JOptionPane.showMessageDialog(this, "Representante inválido (solo letras).");
                return;
            }

            if (!correo.contains("@") || !correo.contains(".")) {
                JOptionPane.showMessageDialog(this, "Correo inválido.");
                return;
            }

            if (!telefono.matches("^\\d{7,15}$")) {
                JOptionPane.showMessageDialog(this, "Teléfono inválido (solo números, 7-15 dígitos).");
                return;
            }

            // Comparar cambios
            boolean huboCambios = !nombre.equals(proveedor.getNombre()) ||
                    !representante.equals(proveedor.getRepresentante()) ||
                    !correo.equals(proveedor.getCorreo()) ||
                    !direccion.equals(proveedor.getDireccion()) ||
                    !telefono.equals(proveedor.getTelefono());

            if (!huboCambios) {
                JOptionPane.showMessageDialog(this, "No se detectaron cambios.");
                return;
            }

            try {
                // Actualizar DTO
                proveedor.setNombre(nombre);
                proveedor.setRepresentante(representante);
                proveedor.setCorreo(correo);
                proveedor.setDireccion(direccion);
                proveedor.setTelefono(telefono);

                new ProveedoresBO().actualizarProveedor(proveedor);

                JOptionPane.showMessageDialog(this, "Proveedor actualizado correctamente.");
                new ProveedoresForm().setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar proveedor: " + ex.getMessage());
            }
        });

        RoundedButton btnCancelar = new RoundedButton("Cancelar");
        btnCancelar.setBackground(Color.DARK_GRAY);
        btnCancelar.setForeground(Color.WHITE);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        btnCancelar.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea cancelar?", "Cancelar",
                    JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                new ProveedoresForm().setVisible(true);
                dispose();
            }
        });

        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

}
