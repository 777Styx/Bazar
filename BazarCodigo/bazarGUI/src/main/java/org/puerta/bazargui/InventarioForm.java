package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.puerta.bazarnegocio.bo.ProductosBO;
import org.puerta.bazardependecias.dto.ProductoDTO;
import org.puerta.bazardependecias.excepciones.NegociosException;

import resources.HeaderPanel;
import resources.RoundedButton;

public class InventarioForm extends JFrame {

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
        DefaultTableModel modelo = new DefaultTableModel(null,
                new Object[] { "ID", "Descuento (%)", "Nombre", "Precio", "Stock", "Proveedor", "Editar",
                        "Eliminar" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaProductos = new JTable(modelo);
        tablaProductos.setRowHeight(40);

        // Renderizador para íconos en las columnas de Editar y Eliminar
        tablaProductos.getColumnModel().getColumn(6).setCellRenderer(new IconCellRenderer());
        tablaProductos.getColumnModel().getColumn(7).setCellRenderer(new IconCellRenderer());

        // LÓGICA PARA CARGAR LOS PRODUCTOS
        ProductosBO productosBO = new ProductosBO();
        List<ProductoDTO> productos = null;
        try {
            productos = productosBO.obtenerTodosLosProductos();
        } catch (NegociosException e) {
            e.printStackTrace();
        }

        for (ProductoDTO dto : productos) {
            modelo.addRow(new Object[] {
                    dto.getId(),
                    dto.getCanDes() + "%",
                    dto.getNombre(),
                    "$" + dto.getPrecio(),
                    dto.getStock(),
                    dto.getNombreProveedor(),
                    escalarIcono("editar.png", 20, 20),
                    escalarIcono("delete.png", 20, 20)
            });

        }

        // Agregar listener para editar o eliminar
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaProductos.rowAtPoint(e.getPoint());
                int columna = tablaProductos.columnAtPoint(e.getPoint());
                if (fila == -1 || columna == -1)
                    return;

                if (columna == 6) {
                    Long idProducto = Long.parseLong(modelo.getValueAt(fila, 0).toString());
                    ProductoDTO producto = null;
                    try {
                        producto = new ProductosBO().obtenerProductoPorId(idProducto);
                    } catch (NegociosException e1) {
                        e1.printStackTrace();
                    }
                    new EditarProductoForm(producto).setVisible(true);
                    dispose();
                } else if (columna == 7) {
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar este producto?", "Confirmar",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            String idStr = modelo.getValueAt(fila, 0).toString();
                            if (idStr == null || idStr.trim().isEmpty()) {
                                throw new Exception("El producto no tiene ID asignado.");
                            }

                            Long idProducto = Long.parseLong(idStr);
                            ProductosBO productosBO = new ProductosBO();
                            productosBO.borrarProducto(idProducto);

                            modelo.removeRow(fila);
                            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Error al eliminar el producto: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        add(scrollPane, BorderLayout.CENTER);

        // PANEL LATERAL: Botón para Registrar Producto
        RoundedButton btnRegistrarProducto = new RoundedButton("Registrar producto");
        btnRegistrarProducto.setIcon(escalarIcono("more.png", 20, 20));
        btnRegistrarProducto.setHorizontalTextPosition(SwingConstants.CENTER);
        btnRegistrarProducto.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnRegistrarProducto.setPreferredSize(new Dimension(150, 60));
        btnRegistrarProducto.setBackground(Color.WHITE);
        btnRegistrarProducto.setForeground(Color.BLACK);
        btnRegistrarProducto.addActionListener(_e -> {
            new RegistrarProductoForm().setVisible(true);
            dispose();
        });

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.add(btnRegistrarProducto);
        add(panelDerecho, BorderLayout.EAST);

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
