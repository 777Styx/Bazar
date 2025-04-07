package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.puerta.bazardependecias.dto.ProductoDTO;
import org.puerta.bazardependecias.dto.ProveedorDTO;
import org.puerta.bazarnegocio.bo.ProductosBO;
import org.puerta.bazarnegocio.bo.ProveedoresBO;

import resources.HeaderPanel;
import resources.RoundedButton;

import java.awt.*;
import java.awt.event.*;

public class EditarProductoForm extends JFrame {

    private JTable tablaProductos;
    private DefaultTableModel modelo;
    private ProductoDTO producto;

    public EditarProductoForm(ProductoDTO producto) {
        this.producto = producto;
        setTitle("Editar Producto");
        setSize(1100, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(new HeaderPanel(this, HeaderPanel.SeccionActual.INVENTARIO), BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[] {
                "ID", "Descuento (%)", "Nombre", "Precio", "Stock", "Proveedor", "Eliminar"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != 6;
            }
        };

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

        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int fila = tablaProductos.rowAtPoint(evt.getPoint());
                int columna = tablaProductos.columnAtPoint(evt.getPoint());

                if (columna == 5) {
                    SeleccionarProveedorDialog dialog = new SeleccionarProveedorDialog();
                    ProveedorDTO proveedor = dialog.getProveedorSeleccionado();
                    if (proveedor != null) {
                        modelo.setValueAt(proveedor.getNombre(), fila, columna);
                        tablaProductos.putClientProperty("proveedor_" + fila, proveedor);
                    }
                } else if (columna == 6) {
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar este producto?", "Confirmar",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            Long id = Long.parseLong(modelo.getValueAt(fila, 0).toString());
                            new ProductosBO().borrarProducto(id);
                            modelo.removeRow(fila);
                            JOptionPane.showMessageDialog(null, "Producto eliminado.");
                            new InventarioForm().setVisible(true);
                            dispose();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error al eliminar: " + ex.getMessage());
                        }
                    }
                }
            }
        });

        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // Cargar el producto
        try {
            String nombreProveedor = "Desconocido";
            try {
                nombreProveedor = new ProveedoresBO().obtenerProveedorPorId(producto.getProveedorId()).getNombre();
            } catch (Exception ignored) {
            }

            modelo.addRow(new Object[] {
                    producto.getId(),
                    producto.getCanDes(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getStock(),
                    nombreProveedor,
                    escalarIcono("delete.png", 18, 18)
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar producto: " + e.getMessage());
        }

        // BOTONES
        RoundedButton btnGuardar = new RoundedButton("Guardar Cambios");
        RoundedButton btnCancelar = new RoundedButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            try {
                int fila = 0;

                String nombre = modelo.getValueAt(fila, 2).toString().trim();
                String precioStr = modelo.getValueAt(fila, 3).toString().trim();
                String stockStr = modelo.getValueAt(fila, 4).toString().trim();
                String canDesStr = modelo.getValueAt(fila, 1).toString().trim();

                // Validaciones
                if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                    JOptionPane.showMessageDialog(null, "Nombre inválido.");
                    return;
                }

                if (!precioStr.matches("^\\d+(\\.\\d{1,2})?$")) {
                    JOptionPane.showMessageDialog(null, "Precio inválido.");
                    return;
                }

                if (!stockStr.matches("^\\d+$")) {
                    JOptionPane.showMessageDialog(null, "Stock inválido.");
                    return;
                }

                if (!canDesStr.matches("^\\d+$")) {
                    JOptionPane.showMessageDialog(null, "Descuento inválido.");
                    return;
                }

                int canDes = Integer.parseInt(canDesStr);
                if (canDes < 0 || canDes > 100) {
                    JOptionPane.showMessageDialog(null, "El descuento debe estar entre 0 y 100.");
                    return;
                }

                // Obtener proveedor seleccionado
                ProveedorDTO proveedor = (ProveedorDTO) tablaProductos.getClientProperty("proveedor_" + fila);
                if (proveedor == null) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un proveedor para el producto.");
                    return;
                }

                // Verifica cambios
                boolean huboCambios = !producto.getNombre().equals(nombre)
                        || producto.getPrecio() != Float.parseFloat(precioStr)
                        || producto.getStock() != Integer.parseInt(stockStr)
                        || producto.getCanDes() != canDes
                        || !producto.getProveedorId().equals(proveedor.getId());

                if (!huboCambios) {
                    JOptionPane.showMessageDialog(null, "No se detectaron cambios.");
                    return;
                }

                // Actualiza DTO
                producto.setNombre(nombre);
                producto.setPrecio(Float.parseFloat(precioStr));
                producto.setStock(Integer.parseInt(stockStr));
                producto.setCanDes(canDes);
                producto.setProveedorId(proveedor.getId());

                new ProductosBO().actualizarProducto(producto);

                JOptionPane.showMessageDialog(null, "Producto actualizado correctamente.");
                new InventarioForm().setVisible(true);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Cancelar los cambios?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new InventarioForm().setVisible(true);
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
