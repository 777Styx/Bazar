/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package org.puerta.bazarnegocio;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.puerta.bazardependecias.dto.ProductoDTO;
import org.puerta.bazardependecias.excepciones.NegociosException;
import org.puerta.bazarnegocio.bo.ProductosBO;
import org.puerta.bazarnegocio.bo.ProveedoresBO;

/**
 *
 * @author julli
 */
public class BazarNegocio {

    public static void main(String[] args) {
        try {
            ProductosBO productosBO = new ProductosBO();
//
//        try {
//            productosBO.registrarProducto(new ProductoDTO("Papa", 10.0F, 20, 0, 1L));
//
//            List<ProductoDTO> productos = productosBO.obtenerTodosLosProductos();
//            System.out.println("Productos registrados:");
//            productos.forEach(System.out::println);
//
//            System.out.println("\nBuscando producto con ID = 1:");
//            ProductoDTO producto = productosBO.obtenerProductoPorId(1L);
//            System.out.println(producto);
//
//            System.out.println("\nActualizando producto con ID = 1:");
//            ProductoDTO productoActualizado = new ProductoDTO(1L, "Papa Actualizada", 15.0F, 25, 5, 1L);
//            productosBO.actualizarProducto(productoActualizado);
//
//            System.out.println("\nProducto actualizado:");
//            ProductoDTO productoModificado = productosBO.obtenerProductoPorId(1L);
//            System.out.println(productoModificado);
//
//            System.out.println("\nEliminando producto con ID = 2:");
//            productosBO.borrarProducto(2L);
//
//            System.out.println("\nProductos después de eliminar:");
//            List<ProductoDTO> productosDespuesDeEliminar = productosBO.obtenerTodosLosProductos();
//            productosDespuesDeEliminar.forEach(System.out::println);

            ProveedoresBO proveedoresBO = new ProveedoresBO();

            proveedoresBO.obtenerTodos().forEach(proveedor -> {
                System.out.println(proveedor.getNombre());
            });
        } catch (NegociosException ex) {
            Logger.getLogger(BazarNegocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
