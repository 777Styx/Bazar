/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarnegocio.bo;

import java.util.ArrayList;
import java.util.List;

import org.puerta.bazardependecias.dto.DetalleDTO;
import org.puerta.bazardependecias.dto.VentaDTO;
import org.puerta.bazarpersistencia.dominio.Detalle;
import org.puerta.bazarpersistencia.dominio.Producto;
import org.puerta.bazarpersistencia.dominio.Usuario;
import org.puerta.bazarpersistencia.dominio.Venta;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazarpersistencia.utils.Convertor;
import org.puerta.bazarpersistencia.dao.ProductosDAO;
import org.puerta.bazarpersistencia.dao.UsuariosDAO;
import org.puerta.bazarpersistencia.dao.VentasDAO;

public class VentasBO {

    private VentasDAO ventaDAO = new VentasDAO();
    private UsuariosDAO usuarioDAO = new UsuariosDAO();
    private ProductosDAO productoDAO = new ProductosDAO();

    public void registrarVenta(VentaDTO ventaDTO) throws NegociosException {
        try {
            if (ventaDTO.getTotal() < 0) {
                throw new NegociosException("El total de la venta no puede ser negativo");
            }

            Usuario usuario = usuarioDAO.findById(ventaDTO.getUsuarioId());
            Venta venta = Convertor.toVenta(ventaDTO, usuario);

            List<Detalle> detalles = new ArrayList<>();

            for (DetalleDTO dto : ventaDTO.getDetalles()) {

                Producto producto = productoDAO.findById(dto.getProductoId());

                if (producto.getStock() < dto.getCantidad()) {
                    throw new NegociosException("El stock de " + producto.getNombre() + " no es suficiente.");
                }

                producto.setStock(producto.getStock() - dto.getCantidad());
                productoDAO.update(producto);

                Detalle detalle = Convertor.toDetalle(dto, venta, producto);
                detalles.add(detalle);
            }

            venta.setDetalles(detalles);
            ventaDAO.save(venta);

        } catch (PersistenciaException e) {
            throw new NegociosException("Error al registrar la venta", e);
        }
    }

    public void actualizarVenta(VentaDTO ventaDTO) throws NegociosException {
        try {
            Venta venta = ventaDAO.findById(ventaDTO.getId());

            if (venta == null) {
                throw new NegociosException("Venta no encontrada");
            }

            venta.setFecha(ventaDTO.getFecha());

            ventaDAO.update(venta);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al actualizar la venta", e);
        }
    }

    public VentaDTO obtenerVentaPorId(Long id) throws NegociosException {
        try {
            Venta venta = ventaDAO.findById(id);
            if (venta == null) {
                throw new NegociosException("Venta no encontrada");
            }

            return Convertor.toVentaDTO(venta);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener la venta", e);
        }
    }

    public void eliminarVenta(Long id) throws NegociosException {
        try {
            Venta venta = ventaDAO.findById(id);
            if (venta == null) {
                throw new NegociosException("Venta no encontrada");
            }

            ventaDAO.delete(id);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al eliminar la venta", e);
        }
    }

    public List<VentaDTO> obtenerTodos() throws NegociosException {
        try {
            List<Venta> ventas = ventaDAO.findAll();
            return Convertor.toVentaDTOList(ventas);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener todas las ventas", e);
        }
    }

}