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

    /**
     * Método encargado de registrar un detalle en el sistema.
     *
     * @param detalleDTO Objeto que contiene la información del detalle a
     * registrar.
     * @throws NegociosException Si ocurre un error al registrar el detalle.
     */
    public void registrarDetalle(DetalleDTO detalleDTO) throws NegociosException {
        try {
            // Verifica si el importe del detalle es negativo, lanzando una excepción si es el caso
            if (detalleDTO.getImporte() < 0) {
                throw new NegociosException("El importe del detalle no puede ser negativo");
            }

            // Convierte el objeto DetalleDTO a un objeto Detalle usando un convertidor
            Detalle detalle = Convertor.toDetalle(detalleDTO, null, null);

            // Guarda el objeto Detalle en la base de datos a través del DAO
            detallesDAO.save(detalle);
        } catch (PersistenciaException e) {
            // Si ocurre un error en la persistencia, lanza una excepción personalizada
            throw new NegociosException("Error al registrar el detalle", e);
        }
    }

    /**
     * Método encargado de obtener un detalle a partir de su ID.
     *
     * @param id El ID del detalle que se desea obtener.
     * @return Un objeto DetalleDTO que contiene la información del detalle
     * encontrado.
     * @throws NegociosException Si ocurre un error al obtener el detalle, como
     * no encontrarlo o un error en la persistencia.
     */
    public DetalleDTO obtenerDetallePorId(Long id) throws NegociosException {
        try {
            // Intenta buscar el detalle en la base de datos usando el ID proporcionado
            Detalle detalle = detallesDAO.findById(id);

            // Si no se encuentra el detalle, lanza una excepción NegociosException con el mensaje correspondiente
            if (detalle == null) {
                throw new NegociosException("Detalle no encontrado");
            }

            // Convierte el objeto Detalle a un DetalleDTO y lo retorna
            return Convertor.toDetalleDTO(detalle);
        } catch (PersistenciaException e) {
            // Si ocurre un error al acceder a la base de datos, lanza una excepción NegociosException con el mensaje de error
            throw new NegociosException("Error al obtener el detalle", e);
        }
    }

    /**
     * Método encargado de actualizar un detalle en el sistema.
     *
     * @param dto El objeto DetalleDTO que contiene los datos a actualizar.
     * @throws NegociosException Si ocurre un error al actualizar el detalle,
     * como un error de persistencia.
     */
    public void actualizarDetalle(DetalleDTO dto) throws NegociosException {
        try {
            // Recupera el Producto y la Venta relacionados con el detalle a partir de los IDs proporcionados en el DTO
            Producto producto = productoDAO.findById(dto.getProductoId());
            Venta venta = ventaDAO.findById(dto.getVentaId());

            // Calcula el importe del detalle aplicando el precio, la cantidad y el descuento
            float importe = (dto.getPrecio() * dto.getCantidad()) * (1 - dto.getCanDes() / 100f);
            dto.setImporte(importe);  // Establece el importe calculado en el DTO

            // Convierte el DTO a un objeto Detalle, asociado a la Venta y al Producto recuperados
            Detalle detalle = Convertor.toDetalle(dto, venta, producto);

            // Actualiza el detalle en la base de datos
            detallesDAO.update(detalle);
        } catch (PersistenciaException e) {
            // Si ocurre un error durante el acceso a la base de datos, lanza una excepción NegociosException
            throw new NegociosException("Error al actualizar el detalle", e);
        }
    }

    /**
     * Método encargado de recalcular los totales de una venta, considerando los
     * detalles de la venta.
     *
     * @param ventaId El ID de la venta para la cual se desea recalcular los
     * totales.
     * @throws NegociosException Si ocurre un error al recalcular los totales de
     * la venta, como un error de persistencia.
     */
    public void recalcularTotalesVenta(Long ventaId) throws NegociosException {
        try {
            // Recupera la venta desde la base de datos utilizando el ID proporcionado
            Venta venta = ventaDAO.findById(ventaId);

            // Inicializa las variables para el total y el total de descuentos
            float total = 0;
            float totalDescuento = 0;

            // Recorre cada detalle de la venta para calcular el total y el descuento total
            for (Detalle d : venta.getDetalles()) {
                // Calcula el importe del detalle (precio * cantidad)
                float importe = d.getPrecio() * d.getCantidad();

                // Calcula el descuento del detalle (descuento * importe)
                float descuento = importe * d.getCanDes() / 100f;

                // Acumula el importe total y el descuento total
                total += importe;
                totalDescuento += descuento;
            }

            // Establece los nuevos valores calculados en la venta
            venta.setTotal(total - totalDescuento);  // Total final después de aplicar los descuentos
            venta.setTotalDescuento(totalDescuento); // Total acumulado de descuentos

            // Actualiza la venta en la base de datos con los nuevos totales
            ventaDAO.update(venta);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al recalcular la venta", e);
        }
    }

}
