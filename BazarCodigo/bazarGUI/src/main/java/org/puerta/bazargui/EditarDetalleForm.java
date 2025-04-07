package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.*;

import org.puerta.bazardependecias.dto.DetalleDTO;
import org.puerta.bazardependecias.dto.ProductoDTO;
import org.puerta.bazardependecias.dto.VentaDTO;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazarnegocio.bo.DetallesBO;
import org.puerta.bazarnegocio.bo.ProductosBO;

import java.awt.*;
import resources.RoundedButton;

public class EditarDetalleForm extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private DetalleDTO detalleOriginal;
    private VentaDTO ventaOriginal;
    private int stockOriginal;
    private ProductoDTO productoSeleccionado;

    public EditarDetalleForm(DetalleDTO detalleOriginal, VentaDTO ventaOriginal) {
        this.detalleOriginal = detalleOriginal;
        this.ventaOriginal = ventaOriginal;
        this.stockOriginal = detalleOriginal.getCantidad();
        ProductoDTO productoDTO = null;
        try {
            productoDTO = new ProductosBO().obtenerProductoPorId(detalleOriginal.getProductoId());
        } catch (NegociosException e) {
            e.printStackTrace();
        }
        productoSeleccionado = productoDTO;
        int stockActual = productoDTO.getStock();

        setTitle("Editar Detalle");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 33, 43));
        JLabel lblTitulo = new JLabel("  Bazar - Editar Detalle");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.add(lblTitulo, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Tabla
        String[] columnas = { "Producto", "Precio", "Cantidad", "Descuento %", "Importe", "Editar" };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 5;
            }

        };

        modelo.addTableModelListener(e -> {
            int fila = e.getFirstRow();
            int columna = e.getColumn();

            if (columna == 2) {
                try {
                    int cantidad = Integer.parseInt(modelo.getValueAt(fila, 2).toString());
                    float precio = Float.parseFloat(modelo.getValueAt(fila, 1).toString().replace("$", ""));
                    int descuento = Integer.parseInt(modelo.getValueAt(fila, 3).toString());
                    float total = (precio * cantidad) * (1 - descuento / 100f);
                    modelo.setValueAt(String.format("$%.2f", total), fila, 4);
                } catch (Exception ignored) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero válido.");
                    modelo.setValueAt("1", fila, 2);
                }
            }
        });

        tabla = new JTable(modelo);
        tabla.setRowHeight(40);

        // Renderer para íconos
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
        });

        // Acción al hacer clic en el ícono de editar
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tabla.rowAtPoint(evt.getPoint());
                int columna = tabla.columnAtPoint(evt.getPoint());

                if (columna == 5) {
                    String productoActual = modelo.getValueAt(fila, 0).toString();
                    SeleccionarProductoDialog dialog = new SeleccionarProductoDialog(EditarDetalleForm.this,
                            productoActual);
                    SeleccionarProductoDialog.Producto seleccionado = dialog.getProductoSeleccionado();

                    if (seleccionado != null) {
                        // Actualizar valores en la tabla
                        modelo.setValueAt(seleccionado.nombre, fila, 0);
                        modelo.setValueAt("$" + seleccionado.precio, fila, 1);
                        modelo.setValueAt("1", fila, 2);
                        modelo.setValueAt(String.valueOf(seleccionado.canDes), fila, 3);

                        float precio = seleccionado.precio;
                        int canDes = seleccionado.canDes;
                        float importe = precio * (1 - canDes / 100f);
                        modelo.setValueAt(String.format("$%.2f", importe), fila, 4);

                        // Actualizar productoSeleccionado correctamente
                        productoSeleccionado = new ProductoDTO();
                        productoSeleccionado.setId(seleccionado.id);
                        productoSeleccionado.setNombre(seleccionado.nombre);
                        productoSeleccionado.setPrecio(precio);
                        productoSeleccionado.setCanDes(canDes);
                        productoSeleccionado.setStock(seleccionado.stock);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        float precio = detalleOriginal.getPrecio();
        int cantidad = detalleOriginal.getCantidad();
        int descuento1 = detalleOriginal.getCanDes();
        float importeConDescuento = (precio * cantidad) * (1 - descuento1 / 100f);

        modelo.addRow(new Object[] {
                detalleOriginal.getNombreProducto(),
                "$" + precio,
                cantidad,
                descuento1,
                String.format("$%.2f", importeConDescuento),
                escalarIcono("editar.png", 20, 20)
        });

        // Botones
        RoundedButton btnConfirmar = new RoundedButton("Confirmar");
        RoundedButton btnSalir = new RoundedButton("Salir");

        btnConfirmar.setPreferredSize(new Dimension(120, 40));
        btnConfirmar.addActionListener(e -> {
            try {
                int fila = 0;
                String nombreNuevo = modelo.getValueAt(fila, 0).toString();
                float nuevoPrecio = Float.parseFloat(modelo.getValueAt(fila, 1).toString().replace("$", ""));
                int nuevaCantidad = Integer.parseInt(modelo.getValueAt(fila, 2).toString());
                int descuento = Integer.parseInt(modelo.getValueAt(fila, 3).toString());

                // Validaciones básicas
                if (nuevaCantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.");
                    return;
                }
                if (nuevoPrecio <= 0) {
                    JOptionPane.showMessageDialog(this, "El precio debe ser un valor positivo.");
                    return;
                }
                if (descuento < 0 || descuento > 100) {
                    JOptionPane.showMessageDialog(this, "El descuento debe estar entre 0% y 100%.");
                    return;
                }

                DetalleDTO actualizado = new DetalleDTO();
                actualizado.setId(detalleOriginal.getId());
                actualizado.setVentaId(ventaOriginal.getId());
                actualizado.setCantidad(nuevaCantidad);
                actualizado.setProductoId(productoSeleccionado.getId());
                actualizado.setPrecio(productoSeleccionado.getPrecio());
                actualizado.setCanDes(productoSeleccionado.getCanDes());

                ProductosBO productosBO = new ProductosBO();
                DetallesBO detallesBO = new DetallesBO();

                Long idOriginal = detalleOriginal.getProductoId();
                Long idNuevo = productoSeleccionado.getId();

                if (!idOriginal.equals(idNuevo)) {
                    // Si se cambió el producto

                    // 1. Devolver stock al producto original
                    ProductoDTO productoOriginal = productosBO.obtenerProductoPorId(idOriginal);
                    productoOriginal.setStock(productoOriginal.getStock() + detalleOriginal.getCantidad());
                    productosBO.actualizarProducto(productoOriginal);

                    // 2. Quitar stock del nuevo producto
                    ProductoDTO productoNuevo = productosBO.obtenerProductoPorId(idNuevo);
                    if (nuevaCantidad > productoNuevo.getStock()) {
                        JOptionPane.showMessageDialog(this, "No hay suficiente stock para el nuevo producto.");
                        return;
                    }
                    productoNuevo.setStock(productoNuevo.getStock() - nuevaCantidad);
                    productosBO.actualizarProducto(productoNuevo);

                } else {
                    // Si no se cambió el producto, solo ajustamos stock por diferencia
                    int cantidadAnterior = detalleOriginal.getCantidad();
                    int diferencia = nuevaCantidad - cantidadAnterior;

                    ProductoDTO productoActualizado = productosBO.obtenerProductoPorId(idOriginal);
                    if (diferencia > 0) {
                        if (diferencia > stockOriginal) {
                            JOptionPane.showMessageDialog(this, "No hay suficiente stock disponible.");
                            return;
                        }
                        productoActualizado.setStock(productoActualizado.getStock() - diferencia);
                    } else if (diferencia < 0) {
                        productoActualizado.setStock(productoActualizado.getStock() + Math.abs(diferencia));
                    }
                    productosBO.actualizarProducto(productoActualizado);
                }

                detallesBO.actualizarDetalle(actualizado);
                detallesBO.recalcularTotalesVenta(ventaOriginal.getId());

                JOptionPane.showMessageDialog(this, "Detalle actualizado y venta recalculada.");
                dispose();
                new VentaForm().setVisible(true);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Formato inválido en cantidad, precio o descuento.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
            }
            dispose();
        });

        btnSalir.setPreferredSize(new Dimension(120, 40));
        btnSalir.setBackground(Color.DARK_GRAY);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.addActionListener(e -> {
            dispose();
            new VentaForm().setVisible(true);
        });

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelBotones.add(btnConfirmar);
        panelBotones.add(Box.createRigidArea(new Dimension(20, 0)));
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private ImageIcon escalarIcono(String ruta, int ancho, int alto) {
        java.net.URL url = getClass().getClassLoader().getResource(ruta);
        if (url == null) {
            System.err.println("No se encontró el ícono: " + ruta);
            return null;
        }
        ImageIcon icono = new ImageIcon(url);
        Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagen);
    }

}
