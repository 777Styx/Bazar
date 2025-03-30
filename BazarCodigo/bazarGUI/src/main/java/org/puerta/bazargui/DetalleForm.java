package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.puerta.bazardependecias.dto.DetalleDTO;
import org.puerta.bazardependecias.dto.VentaDTO;

import resources.RoundedButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DetalleForm extends JFrame {

    private JTable tablaDetalle;
    private DefaultTableModel modelo;
    private static final int COL_EDITAR = 5;

    private VentaDTO ventaDTO;

    public DetalleForm(VentaDTO ventaDTO) {
        this.ventaDTO = ventaDTO;

        setTitle("Detalle de Venta");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 33, 43));

        JLabel lblTitulo = new JLabel("  Bazar - Detalle de Venta");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.add(lblTitulo, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // TABLA
        String[] columnas = { "Producto", "Precio", "Cantidad", "Descuento %", "Importe", "Editar" };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDetalle = new JTable(modelo);
        tablaDetalle.setRowHeight(40);

        // RENDER DE ICONOS
        tablaDetalle.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        // SCROLL
        JScrollPane scrollPane = new JScrollPane(tablaDetalle);
        add(scrollPane, BorderLayout.CENTER);

        // ICONOS
        ImageIcon iconEditar = escalarIcono("resources/editar.png", 20, 20);

        // Cargar los detalles en la tabla

        try {
            for (DetalleDTO detalle : ventaDTO.getDetalles()) {
                float precio = detalle.getPrecio();
                int cantidad = detalle.getCantidad();
                int descuento = detalle.getCanDes();
                float importeConDescuento = (precio * cantidad) * (1 - descuento / 100f);

                modelo.addRow(new Object[] {
                        detalle.getNombreProducto(),
                        "$" + detalle.getPrecio(),
                        detalle.getCantidad(),
                        detalle.getCanDes(),
                        String.format("$%.2f", importeConDescuento),
                        escalarIcono("resources/editar.png", 20, 20)
                });

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar detalles: " + e.getMessage());
        }

        // Listener para editar
        tablaDetalle.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaDetalle.rowAtPoint(e.getPoint());
                int columna = tablaDetalle.columnAtPoint(e.getPoint());

                if (columna == COL_EDITAR) {
                    DetalleDTO detalleSeleccionado = ventaDTO.getDetalles().get(fila);
                    dispose();
                    new EditarDetalleForm(detalleSeleccionado, ventaDTO);
                }

            }
        });

        // FOOTER con botón Salir
        JPanel footer = new JPanel();
        RoundedButton btnSalir = new RoundedButton("Salir");
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentaForm();
                dispose();
            }
        });
        footer.add(btnSalir);
        add(footer, BorderLayout.SOUTH);

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

}
