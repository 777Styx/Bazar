package org.puerta.bazargui;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.puerta.bazardependecias.dto.ProveedorDTO;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazarnegocio.bo.ProveedoresBO;

import resources.RoundedButton;

import java.awt.*;
import java.util.List;

public class SeleccionarProveedorDialog extends JDialog {

    private ProveedorDTO proveedorSeleccionado;

    public SeleccionarProveedorDialog() {
        setTitle("Seleccionar Proveedor");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 33, 43));
        JLabel titulo = new JLabel("  Proveedores disponibles");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setLayout(new FlowLayout(FlowLayout.LEFT));
        header.add(titulo);
        add(header, BorderLayout.NORTH);

        // Constante para la columna del checkbox
        final int COLUMNA_CHECKBOX = 0;

        // Crear modelo de tabla con una columna de checkboxes
        DefaultTableModel modelo = new DefaultTableModel(null,
                new Object[] { "Seleccionar", "Nombre", "Representante", "Teléfono" }) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == COLUMNA_CHECKBOX ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == COLUMNA_CHECKBOX;
            }
        };

        // Crear tabla con el modelo
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(30);

        // Agregar listener para asegurar que solo un checkbox esté seleccionado
        modelo.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column == COLUMNA_CHECKBOX && Boolean.TRUE.equals(modelo.getValueAt(row, column))) {
                    // Desactivar los demás checkboxes
                    modelo.removeTableModelListener(this);
                    for (int i = 0; i < modelo.getRowCount(); i++) {
                        if (i != row && Boolean.TRUE.equals(modelo.getValueAt(i, COLUMNA_CHECKBOX))) {
                            modelo.setValueAt(false, i, COLUMNA_CHECKBOX);
                        }
                    }
                    modelo.addTableModelListener(this);
                }
            }
        });

        // ScrollPane y agregado al layout
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);

        JButton btnCancelar = new RoundedButton("Cancelar");
        JButton btnConfirmar = new RoundedButton("Confirmar");
        btnConfirmar.setBackground(new Color(0, 153, 76));
        btnConfirmar.setForeground(Color.WHITE);

        footer.add(btnCancelar);
        footer.add(btnConfirmar);
        add(footer, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dispose());

        btnConfirmar.addActionListener(e -> {
            for (int i = 0; i < modelo.getRowCount(); i++) {
                Boolean seleccionado = (Boolean) modelo.getValueAt(i, 0);
                if (Boolean.TRUE.equals(seleccionado)) {
                    String nombre = modelo.getValueAt(i, 1).toString();
                    try {
                        ProveedoresBO bo = new ProveedoresBO();
                        proveedorSeleccionado = bo.encontrarPorNombre(nombre);
                        dispose();
                        return;
                    } catch (NegociosException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor", "Advertencia", JOptionPane.WARNING_MESSAGE);
        });

        // Doble clic
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tabla.rowAtPoint(e.getPoint());
                if (e.getClickCount() == 2 && fila != -1) {
                    String nombre = modelo.getValueAt(fila, 1).toString();
                    try {
                        ProveedoresBO bo = new ProveedoresBO();
                        proveedorSeleccionado = bo.encontrarPorNombre(nombre);
                        dispose();
                    } catch (NegociosException ex) {
                        JOptionPane.showMessageDialog(null, "Error al encontrar proveedor: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Cargar proveedores
        try {
            ProveedoresBO bo = new ProveedoresBO();
            List<ProveedorDTO> proveedores = bo.obtenerTodos();
            for (ProveedorDTO p : proveedores) {
                modelo.addRow(new Object[] { false, p.getNombre(), p.getRepresentante(), p.getTelefono() });
            }
        } catch (NegociosException e1) {
            e1.printStackTrace();
        }

        setVisible(true);
    }

    public ProveedorDTO getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }
}
