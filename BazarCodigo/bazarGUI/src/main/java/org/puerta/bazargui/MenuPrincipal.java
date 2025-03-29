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

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(Color.WHITE);
        JButton btnDevolver = new JButton(escalarIcono("resources/devolver.png", 60, 60));
        btnDevolver.setContentAreaFilled(false);
        btnDevolver.setBorderPainted(false);
        header.add(btnDevolver);
        add(header, BorderLayout.NORTH);

        btnDevolver.addActionListener(e -> {
            Login login = new Login();
            login.setVisible(true);
            dispose();

        });

        JPanel grid = new JPanel(new GridLayout(2, 4, 30, 30));
        grid.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        grid.setBackground(Color.WHITE);

        grid.add(crearBoton("resources/venta.png", "Venta"));
        grid.add(crearBoton("resources/inventario.png", "Inventario"));
        grid.add(crearBoton("resources/proveedores.png", "Proveedores"));
        grid.add(crearBoton("resources/pendiente.png", "Pendiente"));
        grid.add(crearBoton("resources/pendiente.png", "Pendiente"));
        grid.add(crearBoton("resources/pendiente.png", "Pendiente"));
        grid.add(crearBoton("resources/pendiente.png", "Pendiente"));
        grid.add(crearBoton("resources/pendiente.png", "Pendiente"));
        
        add(grid, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel crearBoton(String rutaImagen, String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 169, 178)); // Color cyan
        panel.setPreferredSize(new Dimension(180, 200));
    
        JLabel icono = new JLabel(escalarIcono(rutaImagen, 80, 80));
        icono.setHorizontalAlignment(SwingConstants.CENTER);
    
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        etiqueta.setForeground(Color.BLACK);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 24));
    
        panel.add(icono, BorderLayout.CENTER);
        panel.add(etiqueta, BorderLayout.SOUTH);
    
        return panel;
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
        SwingUtilities.invokeLater(MenuPrincipal::new);
    }
}
