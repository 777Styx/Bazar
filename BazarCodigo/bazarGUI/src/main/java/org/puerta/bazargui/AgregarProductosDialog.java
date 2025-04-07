package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.*;

import org.puerta.bazardependecias.dto.ProductoDTO;
import org.puerta.bazarnegocio.bo.ProductosBO;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import resources.RoundedButton;

public class AgregarProductosDialog extends JDialog {

    private JTable tablaProductos;
    private DefaultTableModel modelo;

    public AgregarProductosDialog(Frame owner) {
        super(owner, "Agregar Productos (a la venta)", true);
        setSize(700, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // Panel superior con campo de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblBuscar = new JLabel("Buscar / ID");
        JTextField txtBuscar = new JTextField(25);
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        // Tabla con checkbox de selección
        String[] columnas = { "Seleccionar", "Artículo#", "Nombre", "Precio", "Stock", "Descuento (%)" };

        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
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

        btnAgregar.addActionListener(e -> {
            if (obtenerSeleccionados().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecciona al menos un producto para continuar.");
                return;
            }
            dispose();
        });

        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(Color.WHITE);
        panelInferior.add(btnAgregar);
        add(panelInferior, BorderLayout.SOUTH);

        // Cargar productos reales
        ProductosBO productosBO = new ProductosBO();
        try {
            List<ProductoDTO> productos = productosBO.obtenerTodosLosProductos();
            for (ProductoDTO dto : productos) {
                modelo.addRow(new Object[] {
                        false,
                        dto.getId(),
                        dto.getNombre(),
                        dto.getPrecio(),
                        dto.getStock(),
                        dto.getCanDes()
                });

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);
    }

    public List<Object[]> obtenerSeleccionados() {
        List<Object[]> productosSeleccionados = new ArrayList<>();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Boolean seleccionado = (Boolean) modelo.getValueAt(i, 0);
            if (seleccionado != null && seleccionado) {
                Object[] datos = new Object[] {
                        modelo.getValueAt(i, 1),
                        modelo.getValueAt(i, 2),
                        modelo.getValueAt(i, 3),
                        modelo.getValueAt(i, 4),
                        modelo.getValueAt(i, 5)
                };
                productosSeleccionados.add(datos);
            }
        }
        return productosSeleccionados;
    }

    public static void main(String[] args) {

    }
}
