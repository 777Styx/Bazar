package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.puerta.bazardependecias.dto.ProductoDTO;
import org.puerta.bazarnegocio.bo.ProductosBO;
import org.puerta.bazardependecias.excepciones.NegociosException;

import resources.RoundedButton;

import java.awt.*;
import java.util.List;

public class SeleccionarProductoDialog extends JDialog {

    private JTable tabla;
    private DefaultTableModel modelo;
    private Producto productoSeleccionado = null;

    public SeleccionarProductoDialog(Frame owner, String productoActual) {
        super(owner, "Seleccionar nuevo producto", true);
        setSize(600, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        String[] columnas = { "ID", "Nombre", "Precio", "Descuento (%)", "Stock" };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        tabla.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Botón Seleccionar
        RoundedButton btnSeleccionar = new RoundedButton("Seleccionar");
        btnSeleccionar.setPreferredSize(new Dimension(120, 40));
        btnSeleccionar.setBackground(Color.BLACK);
        btnSeleccionar.setForeground(Color.WHITE);

        btnSeleccionar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                String nombre = modelo.getValueAt(fila, 1).toString();
                if (!nombre.equalsIgnoreCase(productoActual)) {
                    productoSeleccionado = new Producto();
                    productoSeleccionado.id = Long.parseLong(modelo.getValueAt(fila, 0).toString());
                    productoSeleccionado.nombre = nombre;
                    productoSeleccionado.precio = Float.parseFloat(modelo.getValueAt(fila, 2).toString());
                    productoSeleccionado.canDes = Integer.parseInt(modelo.getValueAt(fila, 3).toString());
                    productoSeleccionado.stock = Integer.parseInt(modelo.getValueAt(fila, 4).toString());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Seleccione un producto diferente al actual.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(Color.WHITE);
        panelInferior.add(btnSeleccionar);
        add(panelInferior, BorderLayout.SOUTH);

        // Cargar productos reales
        cargarProductos(productoActual);

        setVisible(true);
    }

    private void cargarProductos(String productoActual) {
        ProductosBO productosBO = new ProductosBO();
        try {
            List<ProductoDTO> productos = productosBO.obtenerTodosLosProductos();
            for (ProductoDTO dto : productos) {
                if (!dto.getNombre().equalsIgnoreCase(productoActual)) {
                    modelo.addRow(new Object[] {
                            dto.getId(),
                            dto.getNombre(),
                            dto.getPrecio(),
                            dto.getCanDes(),
                            dto.getStock()
                    });
                }
            }
        } catch (NegociosException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    // Clase interna para transportar datos seleccionados
    public static class Producto {
        public Long id;
        public String nombre;
        public float precio;
        public int canDes;
        public int stock;
    }
}
