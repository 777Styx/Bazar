package org.puerta.bazargui;

import resources.RoundedButton;
import resources.RoundedPasswordField;
import resources.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import org.puerta.bazardependecias.dto.UsuarioAuthDTO;
import org.puerta.bazardependecias.dto.UsuarioDTO;
import org.puerta.bazarnegocio.bo.UsuariosBO;
import org.puerta.bazardependecias.excepciones.NegociosException;

public class Login extends JFrame {

    private RoundedTextField txtUsuario;
    private RoundedPasswordField txtContrasena;
    private JLabel lblMensajeError;

    public Login() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Login - Bazar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel contenedor con margen horizontal
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        contenedor.setBackground(Color.WHITE);
        add(contenedor);

        // Panel Izquierdo
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setPreferredSize(new Dimension(400, 500));

        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtUsuario = new RoundedTextField(20);
        txtUsuario.setMaximumSize(new Dimension(250, 35));
        txtUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtContrasena = new RoundedPasswordField(20);
        txtContrasena.setMaximumSize(new Dimension(250, 35));
        txtContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnAceptar = new RoundedButton("Aceptar");
        btnAceptar.setFocusPainted(false);
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setBackground(Color.BLACK);
        btnAceptar.setPreferredSize(new Dimension(120, 35));
        btnAceptar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAceptar.setMaximumSize(new Dimension(120, 35));
        btnAceptar.setMargin(new Insets(10, 10, 10, 10));
        btnAceptar.addActionListener(e -> autenticar());

        lblMensajeError = new JLabel(" ");
        lblMensajeError.setForeground(Color.RED);
        lblMensajeError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMensajeError.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Añadir al panel izquierdo
        panelIzquierdo.add(Box.createVerticalStrut(20));
        panelIzquierdo.add(lblTitulo);
        panelIzquierdo.add(lblUsuario);
        panelIzquierdo.add(Box.createVerticalStrut(5));
        panelIzquierdo.add(txtUsuario);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(lblContrasena);
        panelIzquierdo.add(Box.createVerticalStrut(5));
        panelIzquierdo.add(txtContrasena);
        panelIzquierdo.add(Box.createVerticalStrut(20));
        panelIzquierdo.add(btnAceptar);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(lblMensajeError);

        // Panel Derecho con Imagen
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(Color.WHITE);
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("resources/logo_Bazar.jpg"));
        Image scaledImage = icon.getImage().getScaledInstance(320, 320, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(scaledImage));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        panelDerecho.add(lblLogo, BorderLayout.CENTER);

        // Agregar ambos al contenedor principal
        contenedor.add(panelIzquierdo, BorderLayout.WEST);
        contenedor.add(panelDerecho, BorderLayout.CENTER);
    }

    private void autenticar() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        UsuarioAuthDTO auth = new UsuarioAuthDTO(usuario, contrasena);
        UsuariosBO usuariosBO = new UsuariosBO();

        try {
            UsuarioDTO resultado = usuariosBO.login(auth);
            lblMensajeError.setText("");
            JOptionPane.showMessageDialog(this, "Bienvenido " + resultado.getNombre());
            MenuPrincipal menu = new MenuPrincipal();
            menu.setVisible(true);
            this.dispose(); // Cierra la ventana de login
            
        } catch (NegociosException e) {
            lblMensajeError.setText("Error al autenticar el usuario");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
