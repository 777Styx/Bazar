package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
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
        String[] columnas = { "Articulo#", "Nombre", "Precio", "Stock", "Proveedor", "Costo de Compra", "Eliminar Producto" };
        modelo = new DefaultTableModel(columnas, 0);
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

        btnAgregar.addActionListener(_ -> {
            modelo.addRow(new Object[] {
                "", "", "", "", "", "", escalarIcono("resources/delete.png", 18, 18)
            });
        });

        // DETECTAR CLIC EN COLUMNA DE BORRADO
        tablaProductos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int fila = tablaProductos.rowAtPoint(evt.getPoint());
                int columna = tablaProductos.columnAtPoint(evt.getPoint());
                if (columna == tablaProductos.getColumnCount() - 1) {
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        modelo.removeRow(fila);
                    }
                }
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
            ImageIcon iconoCheck = new ImageIcon(getClass().getClassLoader().getResource("resources/check.gif"));
            JLabel mensaje = new JLabel("<html><center>El producto ha sido agregado<br>correctamente</center></html>", SwingConstants.CENTER);
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

        btnCancelar.addActionListener(_ -> {
            int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea cancelar?", "Confirmar", JOptionPane.YES_NO_OPTION);
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
