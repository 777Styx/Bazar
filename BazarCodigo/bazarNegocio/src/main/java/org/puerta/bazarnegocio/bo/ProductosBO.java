package org.puerta.bazarnegocio.bo;

import org.puerta.bazarpersistencia.dao.ProductosDAO;
import org.puerta.bazarpersistencia.dao.ProveedoresDAO;
import org.puerta.bazardependecias.dto.ProductoDTO;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazarpersistencia.dominio.Producto;
import org.puerta.bazarpersistencia.dominio.Proveedor;
import org.puerta.bazarpersistencia.utils.Convertor;

import java.util.List;
import org.puerta.bazardependecias.excepciones.NegociosException;

public class ProductosBO {

    private ProductosDAO productosDAO;
    private ProveedoresDAO proveedoresDAO;

    /**
     *
     */
    public ProductosBO() {
        this.productosDAO = new ProductosDAO();
        this.proveedoresDAO = new ProveedoresDAO();
    }

    /**
     * Método encargado de registrar un producto en el sistema.
     *
     * @param productoDTO El objeto ProductoDTO que contiene la información del
     * producto a registrar.
     * @throws NegociosException Si ocurre un error al registrar el producto,
     * como no encontrar el proveedor o un error de persistencia.
     */
    public void registrarProducto(ProductoDTO productoDTO) throws NegociosException {
        try {
            // Recupera el proveedor de la base de datos usando el ID proporcionado en el DTO
            Proveedor proveedor = proveedoresDAO.findById(productoDTO.getProveedorId());

            // Si el proveedor no existe, lanza una excepción NegociosException
            if (proveedor == null) {
                throw new NegociosException("Proveedor con ID " + productoDTO.getProveedorId() + " no encontrado");
            }

            // Convierte el DTO a un objeto Producto usando el proveedor recuperado
            Producto producto = Convertor.toProducto(productoDTO, proveedor);

            // Guarda el producto en la base de datos a través del DAO
            productosDAO.save(producto);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al registrar el producto: " + e.getMessage(), e);
        }
    }

    /**
     * Método encargado de actualizar los datos de un producto en el sistema.
     *
     * @param productoDTO El objeto ProductoDTO que contiene los nuevos datos
     * del producto a actualizar.
     * @throws NegociosException Si ocurre un error al actualizar el producto,
     * como no encontrar el proveedor o un error de persistencia.
     */
    public void actualizarProducto(ProductoDTO productoDTO) throws NegociosException {
        try {
            // Recupera el proveedor de la base de datos usando el ID proporcionado en el DTO
            Proveedor proveedor = proveedoresDAO.findById(productoDTO.getProveedorId());

            // Si el proveedor no existe, lanza una excepción NegociosException
            if (proveedor == null) {
                throw new NegociosException("Proveedor con ID " + productoDTO.getProveedorId() + " no encontrado");
            }

            // Convierte el DTO a un objeto Producto usando el proveedor recuperado
            Producto producto = Convertor.toProducto(productoDTO, proveedor);

            // Actualiza el producto en la base de datos a través del DAO
            productosDAO.update(producto);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    /**
     * Método encargado de borrar un producto del sistema.
     *
     * @param id El ID del producto que se desea eliminar.
     * @throws NegociosException Si ocurre un error al eliminar el producto,
     * como un error de persistencia.
     */
    public void borrarProducto(Long id) throws NegociosException {
        try {
            // Elimina el producto de la base de datos utilizando el ID proporcionado
            productosDAO.delete(id);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al eliminar el producto: " + e.getMessage(), e);
        }
    }

    /**
     * Método encargado de obtener un producto por su ID.
     *
     * @param id El ID del producto que se desea obtener.
     * @return Un objeto `ProductoDTO` que contiene los datos del producto
     * obtenido.
     * @throws NegociosException Si ocurre un error al obtener el producto, como
     * un error de persistencia.
     */
    public ProductoDTO obtenerProductoPorId(Long id) throws NegociosException {
        try {
            // Recupera el producto desde la base de datos usando el ID proporcionado
            Producto producto = productosDAO.findById(id);

            // Convierte el producto obtenido a un DTO y lo retorna
            return Convertor.toProductoDTO(producto);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al obtener el producto por ID: " + e.getMessage(), e);
        }
    }

    /**
     * Método encargado de obtener todos los productos del sistema.
     *
     * @return Una lista de objetos `ProductoDTO` que contiene los datos de
     * todos los productos disponibles.
     * @throws NegociosException Si ocurre un error al obtener los productos,
     * como un error de persistencia.
     */
    public List<ProductoDTO> obtenerTodosLosProductos() throws NegociosException {
        try {
            // Recupera todos los productos desde la base de datos
            List<Producto> productos = productosDAO.findAll();

            // Convierte la lista de productos a una lista de DTOs y la retorna
            return Convertor.toProductoDTOList(productos);
        } catch (PersistenciaException e) {
            // Si ocurre un error de persistencia, lanza una excepción NegociosException con el mensaje correspondiente
            throw new NegociosException("Error al obtener todos los productos: " + e.getMessage(), e);
        }
    }

}
