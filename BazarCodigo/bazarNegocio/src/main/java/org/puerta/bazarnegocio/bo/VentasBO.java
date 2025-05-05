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

    /**
     * Método encargado de registrar una nueva venta en el sistema.
     *
     * @param ventaDTO El objeto `VentaDTO` que contiene los datos de la venta
     * que se desea registrar.
     * @throws NegociosException Si ocurre un error durante el proceso de
     * registro de la venta, como si el total es negativo, o si no hay
     * suficiente stock de un producto, o si ocurre un error de persistencia al
     * guardar la venta.
     */
    public void registrarVenta(VentaDTO ventaDTO) throws NegociosException {
        try {
            // Verifica que el total de la venta no sea negativo
            if (ventaDTO.getTotal() < 0) {
                throw new NegociosException("El total de la venta no puede ser negativo");
            }

            // Obtiene el usuario asociado a la venta utilizando su ID
            Usuario usuario = usuarioDAO.findById(ventaDTO.getUsuarioId());

            // Convierte el DTO de la venta en una entidad de venta y la asocia al usuario
            Venta venta = Convertor.toVenta(ventaDTO, usuario);

            // Crea una lista de detalles de la venta
            List<Detalle> detalles = new ArrayList<>();

            // Itera sobre los detalles de la venta (productos y cantidades) en el DTO
            for (DetalleDTO dto : ventaDTO.getDetalles()) {

                // Busca el producto asociado a cada detalle
                Producto producto = productoDAO.findById(dto.getProductoId());

                // Verifica que haya suficiente stock del producto para realizar la venta
                if (producto.getStock() < dto.getCantidad()) {
                    throw new NegociosException("El stock de " + producto.getNombre() + " no es suficiente.");
                }

                // Actualiza el stock del producto, restando la cantidad vendida
                producto.setStock(producto.getStock() - dto.getCantidad());
                productoDAO.update(producto);

                // Convierte el detalle del DTO en una entidad Detalle y lo agrega a la lista de detalles
                Detalle detalle = Convertor.toDetalle(dto, venta, producto);
                detalles.add(detalle);
            }

            // Asocia la lista de detalles a la venta y guarda la venta en la base de datos
            venta.setDetalles(detalles);
            ventaDAO.save(venta);

        } catch (PersistenciaException e) {
            // Si ocurre un error en la persistencia, lanza una excepción de negocio con el mensaje de error
            throw new NegociosException("Error al registrar la venta", e);
        }
    }

    /**
     * Método encargado de actualizar los datos de una venta existente.
     *
     * @param ventaDTO El objeto `VentaDTO` que contiene los nuevos datos para
     * actualizar la venta.
     * @throws NegociosException Si ocurre un error durante el proceso de
     * actualización de la venta, como si la venta no existe o si ocurre un
     * error de persistencia al actualizar la venta en la base de datos.
     */
    public void actualizarVenta(VentaDTO ventaDTO) throws NegociosException {
        try {
            // Obtiene la venta existente a partir de su ID
            Venta venta = ventaDAO.findById(ventaDTO.getId());

            // Verifica si la venta existe
            if (venta == null) {
                throw new NegociosException("Venta no encontrada");
            }

            // Actualiza la fecha de la venta con el valor proporcionado en el DTO
            venta.setFecha(ventaDTO.getFecha());

            // Actualiza la venta en la base de datos
            ventaDAO.update(venta);

        } catch (PersistenciaException e) {
            // Si ocurre un error en la persistencia, lanza una excepción de negocio con el mensaje de error
            throw new NegociosException("Error al actualizar la venta", e);
        }
    }

    /**
     * Método encargado de obtener una venta por su ID.
     *
     * @param id El ID de la venta que se desea obtener.
     * @return Un objeto `VentaDTO` que contiene los datos de la venta
     * solicitada.
     * @throws NegociosException Si ocurre un error durante el proceso de
     * obtención de la venta, como si la venta no se encuentra en la base de
     * datos o si ocurre un error de persistencia al acceder a la base de datos.
     */
    public VentaDTO obtenerVentaPorId(Long id) throws NegociosException {
        try {
            // Busca la venta en la base de datos utilizando el ID proporcionado
            Venta venta = ventaDAO.findById(id);

            // Verifica si la venta existe
            if (venta == null) {
                throw new NegociosException("Venta no encontrada");
            }

            // Convierte la entidad Venta a un DTO y lo devuelve
            return Convertor.toVentaDTO(venta);
        } catch (PersistenciaException e) {
            // Si ocurre un error en la persistencia, lanza una excepción de negocio con el mensaje de error
            throw new NegociosException("Error al obtener la venta", e);
        }
    }

    /**
     * Método encargado de eliminar una venta de la base de datos.
     *
     * @param id El ID de la venta que se desea eliminar.
     * @throws NegociosException Si ocurre un error durante el proceso de
     * eliminación de la venta, como si la venta no se encuentra en la base de
     * datos o si ocurre un error de persistencia al eliminar la venta.
     */
    public void eliminarVenta(Long id) throws NegociosException {
        try {
            // Busca la venta en la base de datos utilizando el ID proporcionado
            Venta venta = ventaDAO.findById(id);

            // Verifica si la venta existe
            if (venta == null) {
                throw new NegociosException("Venta no encontrada");
            }

            // Elimina la venta de la base de datos
            ventaDAO.delete(id);
        } catch (PersistenciaException e) {
            // Si ocurre un error en la persistencia, lanza una excepción de negocio con el mensaje de error
            throw new NegociosException("Error al eliminar la venta", e);
        }
    }

    /**
     * Método encargado de obtener todas las ventas registradas en el sistema.
     *
     * @return Una lista de objetos `VentaDTO` que contienen la información de
     * todas las ventas registradas.
     * @throws NegociosException Si ocurre un error durante el proceso de
     * obtención de las ventas, como un error de persistencia al acceder a la
     * base de datos.
     */
    public List<VentaDTO> obtenerTodos() throws NegociosException {
        try {
            // Recupera todas las ventas de la base de datos
            List<Venta> ventas = ventaDAO.findAll();

            // Convierte la lista de ventas a una lista de DTOs y la devuelve
            return Convertor.toVentaDTOList(ventas);
        } catch (PersistenciaException e) {
            // Si ocurre un error en la persistencia, lanza una excepción de negocio con el mensaje de error
            throw new NegociosException("Error al obtener todas las ventas", e);
        }
    }

}
