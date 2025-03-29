package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import resources.RoundedButton;

public class EditarDetalleForm extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;

    public EditarDetalleForm() {
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
                return false;
            }
        };

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

                if (columna == 5) { // columna Editar
                    String productoActual = modelo.getValueAt(fila, 0).toString();
                    SeleccionarProductoDialog dialog = new SeleccionarProductoDialog(EditarDetalleForm.this, productoActual);
                    SeleccionarProductoDialog.Producto seleccionado = dialog.getProductoSeleccionado();
                    if (seleccionado != null) {
                        modelo.setValueAt(seleccionado.nombre, fila, 0);
                        modelo.setValueAt(seleccionado.precio, fila, 1);
                        modelo.setValueAt("1", fila, 2);
                        modelo.setValueAt("0", fila, 3);
                        modelo.setValueAt(seleccionado.precio, fila, 4);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Datos de prueba
        modelo.addRow(new Object[] { "Camisa Verano", "$200", "1", "0", "$200", escalarIcono("resources/editar.png", 20, 20) });

        // Botones
        RoundedButton btnConfirmar = new RoundedButton("Confirmar");
        RoundedButton btnSalir = new RoundedButton("Salir");

        btnConfirmar.setPreferredSize(new Dimension(120, 40));
        btnSalir.setPreferredSize(new Dimension(120, 40));
        btnSalir.setBackground(Color.DARK_GRAY);
        btnSalir.setForeground(Color.WHITE);
        btnSalir.addActionListener(_ -> dispose());

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditarDetalleForm::new);
    }
}
