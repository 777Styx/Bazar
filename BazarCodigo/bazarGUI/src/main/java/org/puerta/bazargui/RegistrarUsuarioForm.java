package org.puerta.bazargui;

import javax.swing.*;
import java.awt.*;
import org.puerta.bazardependecias.dto.UsuarioAuthDTO;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazarnegocio.bo.UsuariosBO;
import resources.RoundedButton;
import resources.RoundedPasswordField;
import resources.RoundedTextField;

public class RegistrarUsuarioForm extends JFrame {

    private JTextField txtNombre;
    private JPasswordField txtContrasena;

    public RegistrarUsuarioForm() {
        setTitle("Registrar Usuario");
        setSize(400, 370);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panelForm = new JPanel(new GridLayout(4, 1, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panelForm.setBackground(Color.WHITE);

        txtNombre = new RoundedTextField();
        txtContrasena = new RoundedPasswordField();

        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Contraseña:"));
        panelForm.add(txtContrasena);

        RoundedButton btnRegistrar = new RoundedButton("Registrar");
        RoundedButton btnCancelar = new RoundedButton("Cancelar");
        btnCancelar.setBackground(Color.DARK_GRAY);
        btnCancelar.setForeground(Color.WHITE);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnRegistrar);
        panelBotones.add(Box.createRigidArea(new Dimension(20, 0)));
        panelBotones.add(btnCancelar);

        add(panelForm, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(_ -> registrarUsuario());
        btnCancelar.addActionListener(_ -> {
            new UsuariosForm().setVisible(true);
            dispose();

        });

        // Imagen superior centrada
        JLabel lblIcono = new JLabel(escalarIcono("resources/user.png", 80, 80));
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblIcono, BorderLayout.NORTH);

        setVisible(true);
    }

    private void registrarUsuario() {
        try {
            String nombre = txtNombre.getText().trim();
            String contrasena = new String(txtContrasena.getPassword()).trim();

            if (nombre.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            if (contrasena.length() < 8) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 8 caracteres.");
                return;
            }

            UsuarioAuthDTO dto = new UsuarioAuthDTO();
            dto.setNombre(nombre);
            dto.setContrasena(contrasena);

            new UsuariosBO().registrarUsuario(dto);
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
            dispose();

        } catch (NegociosException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ImageIcon escalarIcono(String ruta, int ancho, int alto) {
        java.net.URL url = getClass().getClassLoader().getResource(ruta);
        if (url == null) {
            System.err.println("No se encontró la imagen: " + ruta);
            return null;
        }
        ImageIcon icono = new ImageIcon(url);
        Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagen);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrarUsuarioForm::new);
    }
}
