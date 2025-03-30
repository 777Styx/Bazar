package org.puerta.bazargui;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Menu Principal");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER: Botón para volver a Login
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(Color.WHITE);
        JButton btnDevolver = new JButton(escalarIcono("resources/devolver.png", 60, 60));
        btnDevolver.setContentAreaFilled(false);
        btnDevolver.setBorderPainted(false);
        header.add(btnDevolver);
        add(header, BorderLayout.NORTH);

        btnDevolver.addActionListener(_ -> {
            new Login().setVisible(true);
            dispose();
        });

        // GRID de opciones
        JPanel grid = new JPanel(new GridLayout(2, 4, 30, 30));
        grid.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        grid.setBackground(Color.WHITE);

        grid.add(crearBoton("resources/venta.png", "Venta"));
        grid.add(crearBoton("resources/inventario.png", "Inventario"));
        grid.add(crearBoton("resources/proveedores.png", "Proveedores"));
        grid.add(crearBoton("resources/user.png", "Usuarios"));
        grid.add(crearBoton("resources/pendiente.png", "Pendiente"));
        grid.add(crearBoton("resources/pendiente.png", "Pendiente"));
        grid.add(crearBoton("resources/pendiente.png", "Pendiente"));
        grid.add(crearBoton("resources/pendiente.png", "Pendiente"));

        add(grid, BorderLayout.CENTER);
        setVisible(true);
    }

    // Método para crear un panel que actúa como botón
    private JPanel crearBoton(String rutaImagen, String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 169, 178));
        panel.setPreferredSize(new Dimension(180, 200));

        JLabel icono = new JLabel(escalarIcono(rutaImagen, 80, 80));
        icono.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        etiqueta.setForeground(Color.BLACK);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 24));

        panel.add(icono, BorderLayout.CENTER);
        panel.add(etiqueta, BorderLayout.SOUTH);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                redireccionar(texto);
            }
        });

        return panel;
    }

    // Redirecciona a las pantallas correspondientes según la opción
    private void redireccionar(String opcion) {
        switch (opcion.toLowerCase()) {
            case "venta":
                new VentaForm().setVisible(true);
                dispose();
                break;
            case "inventario":
                new InventarioForm().setVisible(true);
                dispose();
                break;
            case "proveedores":
                new ProveedoresForm().setVisible(true);
                dispose();
                break;
            case "usuarios":
                UsuariosForm reg = new UsuariosForm();
                reg.setVisible(true);
                dispose();
                break;
            default:
                JOptionPane.showMessageDialog(this, "La opción \"" + opcion + "\" está en construcción.");
                break;
        }
    }

    // Método para escalar imágenes
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
        SwingUtilities.invokeLater(MenuPrincipal::new);
    }
}
