package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class SeleccionarProductosDialog extends JDialog {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private ArrayList<ProductoSeleccionado> productosSeleccionados = new ArrayList<>();

    public SeleccionarProductosDialog(JFrame parent) {
        super(parent, "Agregar productos a la venta", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 173, 181));
        JLabel lblTitulo = new JLabel("  Bazar");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        topPanel.add(lblTitulo, BorderLayout.WEST);

        JButton btnCerrar = new JButton("X");
        btnCerrar.setFocusable(false);
        btnCerrar.addActionListener(e -> dispose());
        topPanel.add(btnCerrar, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Buscar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblBuscar = new JLabel("Buscar / ID");
        txtBuscar = new JTextField(20);
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        add(searchPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Tabla
        String[] columnas = { "", "ID", "Nombre", "Precio", "Stock" };
        modelo = new DefaultTableModel(columnas, 0) {
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }

            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Cargar datos de prueba
        agregarProducto("17293412", "Camisa Verano", "$200", "7");
        agregarProducto("17293413", "Chaquetón invernal", "$700", "1");

        // Botón agregar
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(Color.BLACK);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.addActionListener(e -> {
            productosSeleccionados.clear();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                Boolean seleccionado = (Boolean) modelo.getValueAt(i, 0);
                if (seleccionado != null && seleccionado) {
                    String id = modelo.getValueAt(i, 1).toString();
                    String nombre = modelo.getValueAt(i, 2).toString();
                    String precio = modelo.getValueAt(i, 3).toString();
                    String stock = modelo.getValueAt(i, 4).toString();
                    productosSeleccionados.add(new ProductoSeleccionado(id, nombre, precio, stock));
                }
            }
            dispose();
        });

        JPanel bottom = new JPanel();
        bottom.add(btnAgregar);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void agregarProducto(String id, String nombre, String precio, String stock) {
        modelo.addRow(new Object[] { false, id, nombre, precio, stock });
    }

    public ArrayList<ProductoSeleccionado> getProductosSeleccionados() {
        return productosSeleccionados;
    }

    // Clase interna para representar producto
    public static class ProductoSeleccionado {
        public String id, nombre, precio, stock;

        public ProductoSeleccionado(String id, String nombre, String precio, String stock) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
        }
    }
}
