package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import resources.HeaderPanel;
import resources.RoundedButton;

public class EditarProductoForm extends JFrame {

    private JTable tablaProductos;
    private DefaultTableModel modelo;

    public EditarProductoForm() {
        setTitle("Editar Producto");
        setSize(1100, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(new HeaderPanel(this, HeaderPanel.SeccionActual.INVENTARIO), BorderLayout.NORTH);

        // TABLA
        String[] columnas = { "Artículo#", "Nombre", "Precio", "Stock", "Proveedor", "Costo de Compra", "Eliminar" };
        modelo = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modelo);

        tablaProductos.setRowHeight(40);
        tablaProductos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof ImageIcon) {
                    JLabel label = new JLabel((ImageIcon) value);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    return label;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        add(scrollPane, BorderLayout.CENTER);

        // PANEL DERECHO
        RoundedButton btnGuardar = new RoundedButton("Guardar Cambios");
        RoundedButton btnCancelar = new RoundedButton("Cancelar");

        btnGuardar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setBackground(Color.DARK_GRAY);
        btnCancelar.setForeground(Color.WHITE);

        btnGuardar.addActionListener(_ -> {
            JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.");
            new InventarioForm();
            dispose();
        });

        btnCancelar.addActionListener(_ -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Cancelar los cambios?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new InventarioForm();
                dispose();
            }
        });

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDerecho.add(Box.createVerticalGlue());
        panelDerecho.add(btnGuardar);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDerecho.add(btnCancelar);
        panelDerecho.add(Box.createVerticalGlue());

        add(panelDerecho, BorderLayout.EAST);

        // Fila de ejemplo
        modelo.addRow(new Object[] {
                "182", "Zapatos Negros", "$300", "12", "Nike", "$200", escalarIcono("resources/delete.png", 18, 18)
        });

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditarProductoForm::new);
    }
}
