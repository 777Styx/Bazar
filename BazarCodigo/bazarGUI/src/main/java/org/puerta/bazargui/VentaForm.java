package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.puerta.bazardependecias.dto.VentaDTO;
import org.puerta.bazarnegocio.bo.ProductosBO;
import org.puerta.bazarnegocio.bo.VentasBO;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import resources.HeaderPanel;
import resources.RoundedButton;

public class VentaForm extends JFrame {

    private JTable tablaVentas;
    private DefaultTableModel modelo;

    public VentaForm() {
        setTitle("Ventas");
        setSize(1100, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header
        add(new HeaderPanel(this, HeaderPanel.SeccionActual.VENTAS), BorderLayout.NORTH);

        // Tabla de ventas
        String[] columnas = { "ID", "Fecha", "Total", "Descuento Total", "Usuario", "Detalle", "Eliminar" };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };

        tablaVentas = new JTable(modelo);
        tablaVentas.setRowHeight(40);

        // Renderizadores de íconos
        tablaVentas.getColumnModel().getColumn(5).setCellRenderer(new IconCellRenderer());
        tablaVentas.getColumnModel().getColumn(6).setCellRenderer(new IconCellRenderer());

        // Listener único para manejar todas las acciones de la tabla
        tablaVentas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaVentas.rowAtPoint(e.getPoint());
                int columna = tablaVentas.columnAtPoint(e.getPoint());

                if (fila == -1 || columna == -1)
                    return;

                final int DETALLE_COL = 5;
                final int ELIMINAR_COL = 6;

                if (columna == DETALLE_COL) {
                    try {
                        Long idVenta = Long.parseLong(modelo.getValueAt(fila, 0).toString());
                        VentasBO ventasBO = new VentasBO();
                        VentaDTO venta = ventasBO.obtenerVentaPorId(idVenta);

                        if (venta != null) {
                            new DetalleForm(venta);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Venta no encontrada.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al cargar detalles de la venta: " + ex.getMessage());
                    }
                }
                if (columna == ELIMINAR_COL) {
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar esta venta?", "Confirmar",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            Long idVenta = Long.parseLong(modelo.getValueAt(fila, 0).toString());
                            VentasBO ventasBO = new VentasBO();
                            ventasBO.eliminarVenta(idVenta);
                            modelo.removeRow(fila);
                            JOptionPane.showMessageDialog(null, "Venta eliminada correctamente.");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error al eliminar la venta: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        add(scrollPane, BorderLayout.CENTER);

        // Panel lateral con botón Agregar venta
        RoundedButton btnAgregarVenta = new RoundedButton("Agregar venta");
        btnAgregarVenta.setPreferredSize(new Dimension(150, 60));
        btnAgregarVenta.setIcon(escalarIcono("more.png", 24, 24));
        btnAgregarVenta.setHorizontalTextPosition(SwingConstants.CENTER);
        btnAgregarVenta.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnAgregarVenta.setBackground(Color.WHITE);
        btnAgregarVenta.setForeground(Color.BLACK);

        btnAgregarVenta.addActionListener(e -> {
            RegistrarVentaForm registrar = new RegistrarVentaForm();
            registrar.setVisible(true);
            dispose();
        });

        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        btnAgregarVenta.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDerecho.add(btnAgregarVenta);

        add(panelDerecho, BorderLayout.EAST);

        try {
            ProductosBO productosBO = new ProductosBO();
            VentasBO ventasBO = new VentasBO();
            List<VentaDTO> ventas = ventasBO.obtenerTodos();

            for (VentaDTO v : ventas) {
                String fechaFormateada = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(v.getFecha());
                modelo.addRow(new Object[] {
                        v.getId(),
                        fechaFormateada,
                        "$" + v.getTotal(),
                        "$" + v.getTotalDescuento(),
                        v.getNombreUsuario(),
                        "", "", ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar ventas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentaForm::new);
    }

    // Renderizador para las celdas de la tabla
    private static final int DETALLE_COL_INDEX = 5;
    private static final int ELIMINAR_COL_INDEX = 6;

    private class IconCellRenderer extends DefaultTableCellRenderer {
        private final ImageIcon detalleIcon = escalarIcono("detalle.png", 16, 16);
        private final ImageIcon eliminarIcon = escalarIcono("delete.png", 16, 16);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            JLabel label = new JLabel();
            label.setHorizontalAlignment(SwingConstants.CENTER);

            if (column == DETALLE_COL_INDEX) {
                label.setIcon(detalleIcon);
            } else if (column == ELIMINAR_COL_INDEX) {
                label.setIcon(eliminarIcon);
            }

            return label;
        }
    }
}
