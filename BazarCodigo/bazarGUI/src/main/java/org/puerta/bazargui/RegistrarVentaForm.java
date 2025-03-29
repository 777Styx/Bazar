package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.List;

import resources.HeaderPanel;
import resources.RoundedButton;

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
        btnAgregarProductos.addActionListener(_ -> {
            AgregarProductosDialog dialog = new AgregarProductosDialog(this);
            List<AgregarProductosDialog.Producto> seleccionados = dialog.obtenerSeleccionados();

            for (AgregarProductosDialog.Producto p : seleccionados) {
                modelo.addRow(new Object[] {
                        new ImageIcon(getClass().getClassLoader().getResource("resources/delete.png")),
                        p.id,
                        p.nombre,
                        p.precio,
                        "",
                        "",
                        p.precio
                });
            }
        });

        // TABLA
        String[] columnas = { "Borrar", "Artículo #", "Nombre", "Precio", "Cantidad", "Desc %", "Total" };
        modelo = new DefaultTableModel(columnas, 0);
        tablaVenta = new JTable(modelo);
        tablaVenta.setRowHeight(40);
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
        RoundedButton btnCancelar = new RoundedButton("Cancelar Venta");

        panelDerecho.add(lblTextoTotal);
        panelDerecho.add(lblTotal);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 20)));
        panelDerecho.add(btnConfirmar);
        panelDerecho.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDerecho.add(btnCancelar);

        add(panelDerecho, BorderLayout.EAST);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrarVentaForm::new);
    }
}
