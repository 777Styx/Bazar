/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarnegocio.bo;

import java.util.List;
import org.puerta.bazardependecias.dominio.Producto;
import org.puerta.bazardependecias.dto.ProductoDTO;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazardependecias.excepciones.PersistenciaException;
import org.puerta.bazardependecias.utils.Convertor;
import org.puerta.bazarpersistencia.dao.ProductosDAO;

/**
 *
 * @author olive
 */
public class ProductosBO {
    
    private ProductosDAO productosDAO = new ProductosDAO();

    public void registrarProducto(ProductoDTO productoDTO) throws NegociosException {
        try {
            if (productoDTO.getNombre() == null || productoDTO.getNombre().trim().isEmpty()) {
                throw new NegociosException("El nombre del producto no puede estar vacío");
            }

            Producto producto = Convertor.toProducto(productoDTO, null); // Proveedor se puede asignar después

            productosDAO.save(producto);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al registrar el producto", e);
        }
    }

    public ProductoDTO obtenerProductoPorId(Long id) throws NegociosException {
        try {
            Producto producto = productosDAO.findById(id);
            if (producto == null) {
                throw new NegociosException("Producto no encontrado");
            }

            return Convertor.toProductoDTO(producto);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener el producto", e);
        }
    }

    public List<ProductoDTO> obtenerTodos() throws NegociosException {
        try {
            List<Producto> productos = productosDAO.findAll();
            return Convertor.toProductoDTOList(productos);
        } catch (PersistenciaException e) {
            throw new NegociosException("Error al obtener todos los productos", e);
        }
    }
}
