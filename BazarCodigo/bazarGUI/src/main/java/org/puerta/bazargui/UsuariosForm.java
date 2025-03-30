package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import org.puerta.bazardependecias.dto.UsuarioDTO;
import org.puerta.bazarnegocio.bo.UsuariosBO;
import org.puerta.bazardependecias.excepciones.NegociosException;
import resources.HeaderPanel;
import resources.RoundedButton;

public class UsuariosForm extends JFrame {

    public UsuariosForm() {
        setTitle("Usuarios");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new HeaderPanel(this, HeaderPanel.SeccionActual.USUARIOS), BorderLayout.NORTH);

        // Tabla
        String[] columnas = { "ID", "Nombre" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        tabla.setDefaultEditor(Object.class, null);
        tabla.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // Cargar datos reales
        try {
            List<UsuarioDTO> usuarios = new UsuariosBO().obtenerTodosLosUsuarios();
            for (UsuarioDTO u : usuarios) {
                modelo.addRow(new Object[] { u.getId(), u.getNombre() });
            }
        } catch (NegociosException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }

        // BOTÓN REGISTRAR
        RoundedButton btnRegistrar = new RoundedButton("Registrar Usuario");
        btnRegistrar.setIcon(escalarIcono("resources/more.png", 20, 20));
        btnRegistrar.setHorizontalTextPosition(SwingConstants.CENTER);
        btnRegistrar.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnRegistrar.setPreferredSize(new Dimension(150, 60));
        btnRegistrar.setBackground(Color.WHITE);
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.addActionListener(_ -> {
            new RegistrarUsuarioForm();
            dispose();
        });

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.add(btnRegistrar);
        add(panelDerecho, BorderLayout.EAST);

        setVisible(true);
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
}
