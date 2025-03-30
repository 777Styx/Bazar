package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.puerta.bazardependecias.dto.ProductoDTO;
import org.puerta.bazardependecias.dto.ProveedorDTO;
import org.puerta.bazarnegocio.bo.ProductosBO;

import java.awt.*;
import java.awt.event.*;

import resources.HeaderPanel;
import resources.RoundedButton;

public class RegistrarProductoForm extends JFrame {

    private JTable tablaProductos;
    private DefaultTableModel modelo;

    public RegistrarProductoForm() {
        setTitle("Registrar Producto");
        setSize(1100, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER
        add(new HeaderPanel(this, HeaderPanel.SeccionActual.INVENTARIO), BorderLayout.NORTH);

        // TABLA DE PRODUCTOS
        String[] columnas = { "ID", "Descuento (%)", "Nombre", "Precio", "Stock", "Proveedor", "Eliminar" };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
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

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        add(scrollPane, BorderLayout.CENTER);

        // BOTÓN AGREGAR FILA
        RoundedButton btnAgregar = new RoundedButton("Registrar producto");
        btnAgregar.setIcon(escalarIcono("resources/more.png", 20, 20));
        btnAgregar.setHorizontalTextPosition(SwingConstants.CENTER);
        btnAgregar.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnAgregar.setPreferredSize(new Dimension(150, 60));
        btnAgregar.setBackground(Color.WHITE);
        btnAgregar.setForeground(Color.BLACK);

        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelo.addRow(new Object[] {
                        "",
                        "",
                        "",
                        "",
                        "",
                        escalarIcono("resources/more.png", 20, 20),
                        escalarIcono("resources/delete.png", 18, 18)
                });
            }
        });

        // DETECTAR CLIC EN COLUMNA DE PROVEEDOR O BORRADO
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int fila = tablaProductos.rowAtPoint(evt.getPoint());
                int columna = tablaProductos.columnAtPoint(evt.getPoint());

                if (fila == -1 || columna == -1)
                    return;

                // Botón de proveedor
                if (columna == 5) {
                    SeleccionarProveedorDialog dialog = new SeleccionarProveedorDialog();
                    ProveedorDTO proveedor = dialog.getProveedorSeleccionado();
                    if (proveedor != null) {
                        modelo.setValueAt(proveedor.getNombre(), fila, columna);
                        tablaProductos.putClientProperty("proveedor_" + fila, proveedor);
                    }
                }

                // Botón eliminar
                if (columna == 6) {
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar este producto?", "Confirmar",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        ((DefaultTableModel) tablaProductos.getModel()).removeRow(fila);
                        tablaProductos.putClientProperty("proveedor_" + fila, null);
                    }
                }
            }
        });

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

        // PANEL DERECHO
        RoundedButton btnConfirmar = new RoundedButton("Confirmar");
        RoundedButton btnCancelar = new RoundedButton("Cancelar");

        btnConfirmar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setBackground(Color.DARK_GRAY);
        btnCancelar.setForeground(Color.WHITE);

        btnConfirmar.addActionListener(_ -> {
            ProductosBO productosBO = new ProductosBO();

            for (int i = 0; i < modelo.getRowCount(); i++) {
                try {
                    String nombre = modelo.getValueAt(i, 2).toString().trim();
                    String precioStr = modelo.getValueAt(i, 3).toString().trim();
                    String stockStr = modelo.getValueAt(i, 4).toString().trim();
                    String canDesStr = modelo.getValueAt(i, 1).toString().trim();

                    // Validar nombre (solo letras y espacios)
                    if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                        JOptionPane.showMessageDialog(null,
                                "El nombre del producto solo debe contener letras (fila " + (i + 1) + ")",
                                "Nombre inválido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Validar precio (positivo y con hasta 2 decimales)
                    if (!precioStr.matches("^\\d+(\\.\\d{1,2})?$")) {
                        JOptionPane.showMessageDialog(null,
                                "El precio debe ser un número positivo válido (fila " + (i + 1) + ")",
                                "Precio inválido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    float precio = Float.parseFloat(precioStr);

                    // Validar stock (entero positivo)
                    if (!stockStr.matches("^\\d+$")) {
                        JOptionPane.showMessageDialog(null,
                                "El stock debe ser un número entero positivo (fila " + (i + 1) + ")",
                                "Stock inválido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int stock = Integer.parseInt(stockStr);

                    // Validar descuento (entre 0 y 100)
                    if (!canDesStr.matches("^\\d+$")) {
                        JOptionPane.showMessageDialog(null,
                                "El descuento debe ser un número entero (fila " + (i + 1) + ")",
                                "Descuento inválido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int canDes = Integer.parseInt(canDesStr);
                    if (canDes < 0 || canDes > 100) {
                        JOptionPane.showMessageDialog(null,
                                "El descuento debe estar entre 0 y 100 (fila " + (i + 1) + ")",
                                "Descuento fuera de rango", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Validar proveedor
                    ProveedorDTO proveedor = (ProveedorDTO) tablaProductos.getClientProperty("proveedor_" + i);
                    if (proveedor == null) {
                        JOptionPane.showMessageDialog(null,
                                "Debe seleccionar un proveedor para el producto en la fila " + (i + 1),
                                "Proveedor requerido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Crear DTO
                    ProductoDTO dto = new ProductoDTO();
                    dto.setNombre(nombre);
                    dto.setPrecio(precio);
                    dto.setStock(stock);
                    dto.setProveedorId(proveedor.getId());
                    dto.setCanDes(canDes);

                    // Guardar producto
                    productosBO.registrarProducto(dto);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error al registrar el producto en la fila " + (i + 1) + ":\n" + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Confirmación
            ImageIcon iconoCheck = new ImageIcon(getClass().getClassLoader().getResource("resources/check.gif"));
            JLabel mensaje = new JLabel("<html><center>El producto ha sido agregado<br>correctamente</center></html>",
                    SwingConstants.CENTER);
            mensaje.setFont(new Font("SansSerif", Font.PLAIN, 14));
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.add(mensaje, BorderLayout.NORTH);
            panel.add(new JLabel(iconoCheck), BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(250, 120));
            JOptionPane.showMessageDialog(null, panel, "Confirmación", JOptionPane.PLAIN_MESSAGE);

            modelo.setRowCount(0);
            new InventarioForm().setVisible(true);
            dispose();
        });

        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaProductos.rowAtPoint(e.getPoint());
                int columna = tablaProductos.columnAtPoint(e.getPoint());
                if (fila == -1 || columna == -1)
                    return;

                if (columna == 6) {
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar este producto?", "Confirmar",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            Long idProducto = Long.parseLong(modelo.getValueAt(fila, 0).toString());
                            ProductosBO productosBO = new ProductosBO();
                            productosBO.borrarProducto(idProducto);

                            modelo.removeRow(fila);
                            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        btnCancelar.addActionListener(_ -> {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea cancelar?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new InventarioForm().setVisible(true);
                dispose();
            }
        });

        JPanel panelDerecho = new JPanel();
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        btnAgregar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelDerecho.add(btnAgregar);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 20)));
        panelDerecho.add(btnConfirmar);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDerecho.add(btnCancelar);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrarProductoForm::new);
    }
}
