package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import resources.HeaderPanel;
import resources.RoundedButton;

public class ProveedoresForm extends JFrame {

    private JTable tablaProveedores;
    private DefaultTableModel modelo;

    public ProveedoresForm() {
        setTitle("Proveedores");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // PANEL SUPERIOR QUE CONTIENE HEADER + FILTRO
        JPanel panelSuperior = new JPanel(new BorderLayout());

        // HEADER
        panelSuperior.add(new HeaderPanel(this, HeaderPanel.SeccionActual.PROVEEDORES), BorderLayout.NORTH);

        // FILTRO
        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtroPanel.setBackground(new Color(240, 240, 240));
        filtroPanel.add(new JLabel("Buscar"));
        filtroPanel.add(new JTextField(20));

        // Agregamos filtro debajo del header
        panelSuperior.add(filtroPanel, BorderLayout.SOUTH);

        // Finalmente se agrega el panel superior completo al frame
        add(panelSuperior, BorderLayout.NORTH);

        // TABLA
        String[] columnas = { "Nombre", "Representante", "Teléfono", "Correo", "Dirección", "Editar", "Eliminar" };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProveedores = new JTable(modelo);
        tablaProveedores.setRowHeight(40);

        tablaProveedores.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        // Íconos
        ImageIcon iconEditar = escalarIcono("resources/editar.png", 20, 20);
        ImageIcon iconEliminar = escalarIcono("resources/delete.png", 20, 20);

        // FILA DE EJEMPLO
        modelo.addRow(new Object[] {
                "CCP", "Juan Perez", "6441-000-0000", "ccp.juan@gmail.com", "Tabasco #283",
                iconEditar,
                iconEliminar
        });

        // ACCIONES
        tablaProveedores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tablaProveedores.rowAtPoint(e.getPoint());
                int columna = tablaProveedores.columnAtPoint(e.getPoint());

                if (columna == 5) {
                    new EditarProveedorForm(); // Vincular al form de edición
                } else if (columna == 6) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "¿Eliminar este proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        modelo.removeRow(fila);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaProveedores);
        add(scrollPane, BorderLayout.CENTER);

        // BOTÓN REGISTRAR
        RoundedButton btnRegistrar = new RoundedButton("Registrar Proveedor");
        btnRegistrar.setIcon(escalarIcono("resources/more.png", 20, 20));
        btnRegistrar.setHorizontalTextPosition(SwingConstants.CENTER);
        btnRegistrar.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnRegistrar.setPreferredSize(new Dimension(150, 60));
        btnRegistrar.setBackground(Color.WHITE);
        btnRegistrar.setForeground(Color.BLACK);

        btnRegistrar.addActionListener(_ -> {
            new RegistrarProveedorForm(); // Vincular al form de registro
        });

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.add(btnRegistrar);
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
        SwingUtilities.invokeLater(ProveedoresForm::new);
    }
}
