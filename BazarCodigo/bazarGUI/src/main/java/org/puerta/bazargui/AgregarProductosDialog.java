package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import resources.RoundedButton;

public class AgregarProductosDialog extends JDialog {

    private JTable tablaProductos;
    private DefaultTableModel modelo;
    private List<Producto> productosSeleccionados;

    public AgregarProductosDialog(Frame owner) {
        super(owner, "Agregar Productos (a la venta)", true);
        setSize(700, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        productosSeleccionados = new ArrayList<>();

        // Panel superior con campo de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblBuscar = new JLabel("Buscar / ID");
        JTextField txtBuscar = new JTextField(25);
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        // Tabla con checkbox de selección
        String[] columnas = { "Seleccionar", "Artículo#", "Nombre", "Precio", "Stock" };
        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // solo el checkbox es editable
            }
        };

        tablaProductos = new JTable(modelo);
        tablaProductos.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(tablaProductos);
        add(scroll, BorderLayout.CENTER);

        // Botón Agregar
        RoundedButton btnAgregar = new RoundedButton("Agregar");
        btnAgregar.setPreferredSize(new Dimension(120, 40));
        btnAgregar.setBackground(Color.BLACK);
        btnAgregar.setForeground(Color.WHITE);

        btnAgregar.addActionListener(_ -> {
            productosSeleccionados.clear();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                Boolean seleccionado = (Boolean) modelo.getValueAt(i, 0);
                if (seleccionado != null && seleccionado) {
                    String id = modelo.getValueAt(i, 1).toString();
                    String nombre = modelo.getValueAt(i, 2).toString();
                    String precio = modelo.getValueAt(i, 3).toString();
                    String stock = modelo.getValueAt(i, 4).toString();
                    productosSeleccionados.add(new Producto(id, nombre, precio, stock));
                }
            }
            dispose(); // cerrar el diálogo
        });

        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(Color.WHITE);
        panelInferior.add(btnAgregar);
        add(panelInferior, BorderLayout.SOUTH);

        // Datos de ejemplo
        agregarProducto("17293412", "Camisa Verano", "$200", "7");
        agregarProducto("17293413", "Chaquetón invernal", "$700", "1");

        setVisible(true);
    }

    private void agregarProducto(String id, String nombre, String precio, String stock) {
        modelo.addRow(new Object[] { false, id, nombre, precio, stock });
    }

    public List<Producto> obtenerSeleccionados() {
        return productosSeleccionados;
    }

    // Clase auxiliar simple
    public static class Producto {
        String id, nombre, precio, stock;

        public Producto(String id, String nombre, String precio, String stock) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
        }

        public String toString() {
            return nombre + " (" + id + ")";
        }
    }

    public static void main(String[] args) {
        AgregarProductosDialog dialog = new AgregarProductosDialog(null);
        for (Producto p : dialog.obtenerSeleccionados()) {
            System.out.println("Seleccionado: " + p);
        }
    }
}
