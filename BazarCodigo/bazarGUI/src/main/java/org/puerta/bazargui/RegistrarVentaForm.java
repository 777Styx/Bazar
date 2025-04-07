package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.puerta.bazardependecias.dto.DetalleDTO;
import org.puerta.bazardependecias.dto.UsuarioDTO;
import org.puerta.bazardependecias.dto.VentaDTO;
import org.puerta.bazarnegocio.bo.VentasBO;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import resources.HeaderPanel;
import resources.RoundedButton;
import resources.Sesion;

public class RegistrarVentaForm extends JFrame {

    private JTable tablaVenta;
    private DefaultTableModel modelo;
    private JLabel lblTotal;

    public RegistrarVentaForm() {
        setTitle("Registrar Venta");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // AGRUPAR HEADER Y FILTRO EN UN SOLO PANEL
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(new HeaderPanel(this, HeaderPanel.SeccionActual.VENTAS), BorderLayout.NORTH);

        // PANEL DE FILTRO
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroPanel.setBackground(new Color(240, 240, 240));
        filtroPanel.add(new JLabel("Buscar / ID"));
        filtroPanel.add(new JTextField(20));

        RoundedButton btnAgregarProductos = new RoundedButton("Agregar productos");
        filtroPanel.add(btnAgregarProductos);

        panelSuperior.add(filtroPanel, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // ACCIÓN DE BOTÓN
        btnAgregarProductos.addActionListener(e -> {
            AgregarProductosDialog dialog = new AgregarProductosDialog(this);
            List<Object[]> seleccionados = dialog.obtenerSeleccionados();
            for (Object[] datos : seleccionados) {
                Long id = Long.parseLong(datos[0].toString());
                String nombre = datos[1].toString();
                float precio = Float.parseFloat(datos[2].toString());
                int stock = Integer.parseInt(datos[3].toString());
                int canDes = Integer.parseInt(datos[4].toString());

                ImageIcon iconoBorrar = new ImageIcon(getClass().getClassLoader().getResource("delete.png"));
                modelo.addRow(new Object[] {
                        iconoBorrar,
                        id,
                        nombre,
                        precio,
                        "",
                        canDes,
                        ""
                });

            }
        });

        // TABLA
        String[] columnas = { "Borrar", "Artículo #", "Nombre", "Precio", "Cantidad", "Desc %",
                "Total(Descuento incluido)" };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        modelo.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 4) {
                try {
                    int cantidad = Integer.parseInt(modelo.getValueAt(row, 4).toString());
                    float precio = Float.parseFloat(modelo.getValueAt(row, 3).toString());
                    int descuento = Integer.parseInt(modelo.getValueAt(row, 5).toString());
                    int stock = obtenerStockDeProducto(row);

                    if (cantidad > stock) {
                        JOptionPane.showMessageDialog(null, "Cantidad excede el stock disponible.");
                        modelo.setValueAt("", row, 4);
                        modelo.setValueAt("", row, 6);
                    } else {
                        float totalProducto = (precio * cantidad) * (1 - descuento / 100f);
                        modelo.setValueAt(String.format("%.2f", totalProducto), row, 6);
                        actualizarTotal();
                    }

                } catch (Exception ignored) {
                }
            }
        });

        tablaVenta = new JTable(modelo);
        tablaVenta.getColumnModel().getColumn(0).setCellRenderer(new IconCellRenderer());

        tablaVenta.setRowHeight(40);
        tablaVenta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaVenta.rowAtPoint(e.getPoint());
                int columna = tablaVenta.columnAtPoint(e.getPoint());

                if (columna == 0) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "¿Desea eliminar este producto de la venta?", "Confirmar",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        modelo.removeRow(fila);
                        actualizarTotal();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaVenta);
        add(scrollPane, BorderLayout.CENTER);

        // PANEL DERECHO CON TOTAL Y BOTONES
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTextoTotal = new JLabel("Total");
        lblTextoTotal.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTotal = new JLabel("0.00");
        lblTotal.setFont(new Font("SansSerif", Font.PLAIN, 24));

        RoundedButton btnConfirmar = new RoundedButton("Confirmar");

        btnConfirmar.addActionListener(e -> {
            try {
                if (modelo.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto.");
                    return;
                }

                float total = 0f;
                float totalDescuento = 0f;
                List<DetalleDTO> detalles = new ArrayList<>();

                for (int i = 0; i < modelo.getRowCount(); i++) {
                    Long productoId = Long.parseLong(modelo.getValueAt(i, 1).toString());
                    String nombre = modelo.getValueAt(i, 2).toString();
                    float precio = Float.parseFloat(modelo.getValueAt(i, 3).toString());

                    String cantidadStr = modelo.getValueAt(i, 4).toString();
                    String canDesStr = modelo.getValueAt(i, 5).toString();

                    if (!cantidadStr.matches("\\d+") || !canDesStr.matches("\\d+")) {
                        JOptionPane.showMessageDialog(this,
                                "La cantidad y el descuento deben ser números válidos. Fila " + (i + 1));
                        return;
                    }

                    int cantidad = Integer.parseInt(cantidadStr);
                    int canDes = Integer.parseInt(canDesStr);
                    if (cantidad <= 0 || canDes < 0 || canDes > 100) {
                        JOptionPane.showMessageDialog(this, "Cantidad o descuento inválido. Fila " + (i + 1));
                        return;
                    }

                    float importe = precio * cantidad;
                    float descuento = importe * canDes / 100f;

                    total += importe;
                    totalDescuento += descuento;

                    DetalleDTO detalle = new DetalleDTO();
                    detalle.setProductoId(productoId);
                    detalle.setCantidad(cantidad);
                    detalle.setPrecio(precio);
                    detalle.setCanDes(canDes);
                    detalle.setImporte(importe);
                    detalles.add(detalle);
                }

                UsuarioDTO usuario = Sesion.getUsuarioActual();

                if (usuario == null) {
                    JOptionPane.showMessageDialog(this, "No hay un usuario logueado. No se puede registrar la venta.");
                    return;
                }

                VentaDTO ventaDTO = new VentaDTO();
                ventaDTO.setFecha(new Date());
                ventaDTO.setTotal(total - totalDescuento);
                ventaDTO.setTotalDescuento(totalDescuento);
                ventaDTO.setUsuarioId(usuario.getId());
                ventaDTO.setDetalles(detalles);

                new VentasBO().registrarVenta(ventaDTO);

                JOptionPane.showMessageDialog(this, "Venta registrada exitosamente.");
                new VentaForm().setVisible(true);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar venta: " + ex.getMessage());
            }
        });

        RoundedButton btnCancelar = new RoundedButton("Cancelar Venta");

        btnCancelar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de cancelar la venta?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                VentaForm ventaForm = new VentaForm();
                ventaForm.setVisible(true);
                dispose();
            }
        });

        panelDerecho.add(lblTextoTotal);
        panelDerecho.add(lblTotal);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 20)));
        panelDerecho.add(btnConfirmar);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDerecho.add(btnCancelar);

        add(panelDerecho, BorderLayout.EAST);

        setVisible(true);
    }

    private void actualizarTotal() {
        float total = 0f;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            try {
                total += Float.parseFloat(modelo.getValueAt(i, 6).toString());
            } catch (Exception ignored) {
            }
        }
        lblTotal.setText(String.format("$%.2f", total));
    }

    private class IconCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Icon) {
                JLabel label = new JLabel((Icon) value);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private int obtenerStockDeProducto(int fila) {
        try {
            return Integer.parseInt(modelo.getValueAt(fila, 3 + 1).toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrarVentaForm::new);
    }
}
