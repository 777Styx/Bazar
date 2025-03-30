package resources;

import javax.swing.*;

import org.puerta.bazargui.InventarioForm;
import org.puerta.bazargui.MenuPrincipal;
import org.puerta.bazargui.ProveedoresForm;
import org.puerta.bazargui.VentaForm;
import org.puerta.bazargui.UsuariosForm;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HeaderPanel extends JPanel {

    public enum SeccionActual {
        VENTAS, INVENTARIO, PROVEEDORES, NINGUNA, USUARIOS
    }

    public HeaderPanel(JFrame owner, SeccionActual actual) {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 33, 43));

        JLabel lblTitulo = new JLabel("  Bazar Esthela");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.WEST);

        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelNavegacion.setBackground(Color.WHITE);

        addBoton(panelNavegacion, "venta_blanco.png", SeccionActual.VENTAS, actual, owner, VentaForm.class);
        addBoton(panelNavegacion, "inventario_blanco.png", SeccionActual.INVENTARIO, actual, owner, InventarioForm.class);
        addBoton(panelNavegacion, "proveedores_blanco.png", SeccionActual.PROVEEDORES, actual, owner, ProveedoresForm.class);
        addBoton(panelNavegacion, "devolver_blanco.png", SeccionActual.NINGUNA, actual, owner, MenuPrincipal.class);
        addBoton(panelNavegacion, "user.png", SeccionActual.USUARIOS, actual, owner, UsuariosForm.class);


        add(panelNavegacion, BorderLayout.EAST);
    }

    private void addBoton(JPanel panel, String icono, SeccionActual seccion, SeccionActual actual, JFrame owner, Class<? extends JFrame> destino) {
        JLabel label = new JLabel(escalarIcono("resources/" + icono, 40, 40));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (seccion != actual || seccion == SeccionActual.NINGUNA) {
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        JFrame nuevaVentana = destino.getDeclaredConstructor().newInstance();
                        nuevaVentana.setVisible(true);
                        owner.dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } else {
            label.setEnabled(false);
        }

        panel.add(label);
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
