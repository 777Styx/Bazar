package org.puerta.bazargui;

import javax.swing.*;
import java.awt.*;

import resources.HeaderPanel;
import resources.RoundedButton;

public class EditarVentaForm extends JFrame {

    private JTextField txtTotal, txtTotalDescuento, txtFecha, txtUsuario;

    public EditarVentaForm() {
        setTitle("Editar Venta");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        add(new HeaderPanel(this, HeaderPanel.SeccionActual.VENTAS), BorderLayout.NORTH);

        // FORMULARIO CENTRAL
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panelForm.setBackground(Color.WHITE);

        panelForm.add(new JLabel("Total:"));
        txtTotal = new JTextField();
        panelForm.add(txtTotal);

        panelForm.add(new JLabel("Total Descuento:"));
        txtTotalDescuento = new JTextField();
        panelForm.add(txtTotalDescuento);

        panelForm.add(new JLabel("Fecha:"));
        txtFecha = new JTextField("dd/MM/yyyy");
        panelForm.add(txtFecha);

        panelForm.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        panelForm.add(txtUsuario);

        add(panelForm, BorderLayout.CENTER);

        // BOTONES
        RoundedButton btnConfirmar = new RoundedButton("Confirmar");
        RoundedButton btnCancelar = new RoundedButton("Cancelar");
        btnCancelar.setBackground(Color.DARK_GRAY);
        btnCancelar.setForeground(Color.WHITE);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        // Acciones de botones
        btnCancelar.addActionListener(_ -> {
            dispose();
            new VentaForm().setVisible(true);
        });
        btnConfirmar.addActionListener(_ -> {
            JOptionPane.showMessageDialog(this, "Venta editada correctamente", "Confirmación",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new VentaForm().setVisible(true);
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditarVentaForm::new);
    }
}
