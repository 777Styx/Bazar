package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import resources.HeaderPanel;
import resources.RoundedButton;

public class InventarioForm extends JFrame {

    private JTable tablaProductos;
    private DefaultTableModel modelo;

    public InventarioForm() {
        setTitle("Inventario");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // CREAMOS HEADER Y FILTRO EN UN SOLO PANEL
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(new HeaderPanel(this, HeaderPanel.SeccionActual.INVENTARIO), BorderLayout.NORTH);

        // PANEL DE FILTRO
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroPanel.setBackground(new Color(240, 240, 240));
        filtroPanel.add(new JLabel("Buscar / ID"));
        filtroPanel.add(new JTextField(20));

        panelSuperior.add(filtroPanel, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // TABLA DE PRODUCTOS
        String[] columnas = { "ID Producto", "Nombre", "Precio", "Stock", "Editar", "Eliminar" };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Ninguna celda es editable directamente
            }
        };

        tablaProductos = new JTable(modelo);
        tablaProductos.setRowHeight(40);

        // Renderizador para íconos en las columnas de Editar y Eliminar
        tablaProductos.getColumnModel().getColumn(4).setCellRenderer(new IconCellRenderer());
        tablaProductos.getColumnModel().getColumn(5).setCellRenderer(new IconCellRenderer());

        // Agregar listener para editar o eliminar
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaProductos.rowAtPoint(e.getPoint());
                int columna = tablaProductos.columnAtPoint(e.getPoint());
                if (fila == -1 || columna == -1)
                    return;
                if (columna == 4) {
                    new EditarProductoForm().setVisible(true); // Aquí se abre la pantalla de edición de producto
                    dispose(); // Cierra la ventana actual
                    
                } else if (columna == 5) {
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar este producto?", "Confirmar",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        modelo.removeRow(fila);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        add(scrollPane, BorderLayout.CENTER);

        // PANEL LATERAL: Botón para Registrar Producto
        RoundedButton btnRegistrarProducto = new RoundedButton("Registrar producto");
        btnRegistrarProducto.setIcon(escalarIcono("resources/more.png", 20, 20));
        btnRegistrarProducto.setHorizontalTextPosition(SwingConstants.CENTER);
        btnRegistrarProducto.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnRegistrarProducto.setPreferredSize(new Dimension(150, 60));
        btnRegistrarProducto.setBackground(Color.WHITE);
        btnRegistrarProducto.setForeground(Color.BLACK);
        btnRegistrarProducto.addActionListener(_ -> {
            new RegistrarProductoForm().setVisible(true);
            dispose();
        });

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.add(btnRegistrarProducto);
        add(panelDerecho, BorderLayout.EAST);

        // Datos de ejemplo
        modelo.addRow(new Object[] {
                "101", "Camisa Verano", "$200", "15",
                escalarIcono("resources/editar.png", 20, 20),
                escalarIcono("resources/delete.png", 20, 20)
        });

        setVisible(true);
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

    // Renderizador para columnas de íconos
    private class IconCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (value instanceof Icon) {
                JLabel label = new JLabel((Icon) value);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventarioForm::new);
    }
}
