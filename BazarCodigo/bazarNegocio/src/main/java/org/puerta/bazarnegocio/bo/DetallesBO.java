/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarnegocio.bo;

import org.puerta.bazarpersistencia.dominio.Detalle;
import org.puerta.bazarpersistencia.dominio.Producto;
import org.puerta.bazarpersistencia.dominio.Venta;
import org.puerta.bazardependecias.dto.DetalleDTO;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazarpersistencia.utils.Convertor;
import org.puerta.bazarpersistencia.dao.DetallesDAO;
import org.puerta.bazarpersistencia.dao.ProductosDAO;
import org.puerta.bazarpersistencia.dao.VentasDAO;

/**
 *
 * @author olive
 */
public class DetallesBO {

    private DetallesDAO detallesDAO = new DetallesDAO();
    VentasDAO ventaDAO = new VentasDAO();
    ProductosDAO productoDAO = new ProductosDAO();

    public void registrarDetalle(DetalleDTO detalleDTO) throws NegociosException {
        try {
            if (detalleDTO.getImporte() < 0) {
                throw new NegociosException("El importe del detalle no puede ser negativo");
            }

            Detalle detalle = Convertor.toDetalle(detalleDTO, null, null);

            detallesDAO.save(detalle);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al registrar el detalle", e);
        }
    }

    public DetalleDTO obtenerDetallePorId(Long id) throws NegociosException {
        try {
            Detalle detalle = detallesDAO.findById(id);
            if (detalle == null) {
                throw new NegociosException("Detalle no encontrado");
            }

            return Convertor.toDetalleDTO(detalle);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener el detalle", e);
        }
    }

    public void actualizarDetalle(DetalleDTO dto) throws NegociosException {
        try {
            Producto producto = productoDAO.findById(dto.getProductoId());
            Venta venta = ventaDAO.findById(dto.getVentaId());

            float importe = (dto.getPrecio() * dto.getCantidad()) * (1 - dto.getCanDes() / 100f);
            dto.setImporte(importe);

            Detalle detalle = Convertor.toDetalle(dto, venta, producto);
            detallesDAO.update(detalle);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al actualizar el detalle", e);
        }
    }

    public void recalcularTotalesVenta(Long ventaId) throws NegociosException {
        try {
            Venta venta = ventaDAO.findById(ventaId);
            float total = 0;
            float totalDescuento = 0;

            for (Detalle d : venta.getDetalles()) {
                float importe = d.getPrecio() * d.getCantidad();
                float descuento = importe * d.getCanDes() / 100f;
                total += importe;
                totalDescuento += descuento;
            }

            venta.setTotal(total - totalDescuento);
            venta.setTotalDescuento(totalDescuento);

            ventaDAO.update(venta);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al recalcular la venta", e);
        }
    }

}
