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

    public ProductosBO() {
        this.productosDAO = new ProductosDAO();
        this.proveedoresDAO = new ProveedoresDAO();
    }

    public void registrarProducto(ProductoDTO productoDTO) throws NegociosException {
        try {
            Proveedor proveedor = proveedoresDAO.findById(productoDTO.getProveedorId());
            if (proveedor == null) {
                throw new NegociosException("Proveedor con ID " + productoDTO.getProveedorId() + " no encontrado");
            }
            Producto producto = Convertor.toProducto(productoDTO, proveedor);
            productosDAO.save(producto);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al registrar el producto: " + e.getMessage(), e);
        }
    }

    public void actualizarProducto(ProductoDTO productoDTO) throws NegociosException {
        try {
            Proveedor proveedor = proveedoresDAO.findById(productoDTO.getProveedorId());
            if (proveedor == null) {
                throw new NegociosException("Proveedor con ID " + productoDTO.getProveedorId() + " no encontrado");
            }
            Producto producto = Convertor.toProducto(productoDTO, proveedor);
            productosDAO.update(producto);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    public void borrarProducto(Long id) throws NegociosException {
        try {
            productosDAO.delete(id);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al eliminar el producto: " + e.getMessage(), e);
        }
    }

    public ProductoDTO obtenerProductoPorId(Long id) throws NegociosException {
        try {
            Producto producto = productosDAO.findById(id);
            return Convertor.toProductoDTO(producto);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener el producto por ID: " + e.getMessage(), e);
        }
    }

    public List<ProductoDTO> obtenerTodosLosProductos() throws NegociosException {
        try {
            List<Producto> productos = productosDAO.findAll();
            return Convertor.toProductoDTOList(productos);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener todos los productos: " + e.getMessage(), e);
        }
    }
}