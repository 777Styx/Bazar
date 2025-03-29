package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import resources.RoundedButton;

import java.awt.*;

public class SeleccionarProductoDialog extends JDialog {

    private JTable tabla;
    private DefaultTableModel modelo;
    private Producto productoSeleccionado = null;

    public SeleccionarProductoDialog(Frame owner, String productoActual) {
        super(owner, "Seleccionar nuevo producto", true);
        setSize(600, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        String[] columnas = { "ID", "Nombre", "Precio", "Stock" };
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Botón Seleccionar
        RoundedButton btnSeleccionar = new RoundedButton("Seleccionar");
        btnSeleccionar.setPreferredSize(new Dimension(120, 40));
        btnSeleccionar.setBackground(Color.BLACK);
        btnSeleccionar.setForeground(Color.WHITE);

        btnSeleccionar.addActionListener(_ -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                String nombre = modelo.getValueAt(fila, 1).toString();
                if (!nombre.equalsIgnoreCase(productoActual)) {
                    String id = modelo.getValueAt(fila, 0).toString();
                    String precio = modelo.getValueAt(fila, 2).toString();
                    String stock = modelo.getValueAt(fila, 3).toString();
                    productoSeleccionado = new Producto(id, nombre, precio, stock);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Seleccione un producto diferente al actual.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(Color.WHITE);
        panelInferior.add(btnSeleccionar);
        add(panelInferior, BorderLayout.SOUTH);

        // Cargar productos de ejemplo
        agregarProducto("17293412", "Camisa Verano", "$200", "7");
        agregarProducto("17293413", "Pantalón Cargo", "$400", "3");
        agregarProducto("17293414", "Blusa Elegante", "$350", "5");

        setVisible(true);
    }

    private void agregarProducto(String id, String nombre, String precio, String stock) {
        modelo.addRow(new Object[] { id, nombre, precio, stock });
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public static class Producto {
        String id, nombre, precio, stock;

        public Producto(String id, String nombre, String precio, String stock) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
        }
    }
}
