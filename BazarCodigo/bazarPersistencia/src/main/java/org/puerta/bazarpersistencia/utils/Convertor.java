/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazarpersistencia.utils;

import org.puerta.bazarpersistencia.dominio.Producto;
import org.puerta.bazarpersistencia.dominio.Detalle;
import org.puerta.bazarpersistencia.dominio.Venta;
import org.puerta.bazarpersistencia.dominio.Proveedor;
import org.puerta.bazarpersistencia.dominio.Usuario;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import org.puerta.bazardependecias.dto.*;

public class Convertor {

    // Mapeo de Venta a VentaDTO
    public static VentaDTO toVentaDTO(Venta venta) {
        if (venta == null) {
            return null;
        }

        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setId(venta.getId());
        ventaDTO.setTotal(venta.getTotal());
        ventaDTO.setTotalDescuento(venta.getTotalDescuento());
        ventaDTO.setFecha(venta.getFecha());

        if (venta.getUsuario() != null) {
            ventaDTO.setUsuarioId(venta.getUsuario().getId());
            ventaDTO.setNombreUsuario(venta.getUsuario().getNombre());
        }

        if (venta.getDetalles() != null) {
            ventaDTO.setDetalles(venta.getDetalles().stream()
                    .map(Convertor::toDetalleDTO)
                    .collect(Collectors.toList()));
        }

        return ventaDTO;
    }

    // Mapeo de Usuario a UsuarioDTO
    public static UsuarioDTO toUsuarioDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());

        return usuarioDTO;
    }

    // Mapeo de Detalle a DetalleDTO
    public static DetalleDTO toDetalleDTO(Detalle detalle) {
        if (detalle == null) {
            return null;
        }

        DetalleDTO detalleDTO = new DetalleDTO();
        detalleDTO.setId(detalle.getId());
        detalleDTO.setImporte(detalle.getImporte());
        detalleDTO.setPrecio(detalle.getPrecio());
        detalleDTO.setCantidad(detalle.getCantidad());
        detalleDTO.setCanDes(detalle.getCanDes());

        if (detalle.getVenta() != null) {
            detalleDTO.setVentaId(detalle.getVenta().getId());
        }

        if (detalle.getProducto() != null) {
            detalleDTO.setProductoId(detalle.getProducto().getId());
            detalleDTO.setNombreProducto(detalle.getProducto().getNombre());
        }

        return detalleDTO;
    }

    // Mapeo de Producto a ProductoDTO
    public static ProductoDTO toProductoDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(producto.getId());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setStock(producto.getStock());
        productoDTO.setCanDes(producto.getCanDes());

        if (producto.getProveedor() != null) {
            productoDTO.setProveedorId(producto.getProveedor().getId());
            productoDTO.setNombreProveedor(producto.getProveedor().getNombre());
        }

        return productoDTO;
    }

    // Mapeo de Proveedor a ProveedorDTO
    public static ProveedorDTO toProveedorDTO(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }

        ProveedorDTO proveedorDTO = new ProveedorDTO();
        proveedorDTO.setId(proveedor.getId());
        proveedorDTO.setNombre(proveedor.getNombre());
        proveedorDTO.setRepresentante(proveedor.getRepresentante());
        proveedorDTO.setCorreo(proveedor.getCorreo());
        proveedorDTO.setDireccion(proveedor.getDireccion());
        proveedorDTO.setTelefono(proveedor.getTelefono());

        return proveedorDTO;
    }

    // Mapeo de VentaDTO a Venta (para operaciones de creación/actualización)
    public static Venta toVenta(VentaDTO ventaDTO, Usuario usuario) {
        if (ventaDTO == null) {
            return null;
        }

        Venta venta = new Venta();
        venta.setId(ventaDTO.getId());
        venta.setTotal(ventaDTO.getTotal());
        venta.setTotalDescuento(ventaDTO.getTotalDescuento());
        venta.setFecha(ventaDTO.getFecha());
        venta.setUsuario(usuario);

        // Mapear detalles (si hay)
        if (ventaDTO.getDetalles() != null) {
            List<Detalle> detalles = new ArrayList<>();
            for (DetalleDTO detalleDTO : ventaDTO.getDetalles()) {
                Producto producto = new Producto();
                producto.setId(detalleDTO.getProductoId());
                Detalle detalle = toDetalle(detalleDTO, venta, producto);
                detalles.add(detalle);
            }
            venta.setDetalles(detalles);
        }

        return venta;
    }

    // Mapeo de DetalleDTO a Detalle
    public static Detalle toDetalle(DetalleDTO detalleDTO, Venta venta, Producto producto) {
        if (detalleDTO == null) {
            return null;
        }

        Detalle detalle = new Detalle();
        detalle.setId(detalleDTO.getId());
        detalle.setImporte(detalleDTO.getImporte());
        detalle.setPrecio(detalleDTO.getPrecio());
        detalle.setCantidad(detalleDTO.getCantidad());
        detalle.setCanDes(detalleDTO.getCanDes());
        detalle.setVenta(venta);
        detalle.setProducto(producto);

        return detalle;
    }

    // Mapeo de ProductoDTO a Producto
    public static Producto toProducto(ProductoDTO productoDTO, Proveedor proveedor) {
        if (productoDTO == null) {
            return null;
        }

        Producto producto = new Producto();
        producto.setId(productoDTO.getId());
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setCanDes(productoDTO.getCanDes());
        producto.setProveedor(proveedor);

        return producto;
    }

    // Mapeo de ProveedorDTO a Proveedor
    public static Proveedor toProveedor(ProveedorDTO proveedorDTO) {
        if (proveedorDTO == null) {
            return null;
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setId(proveedorDTO.getId());
        proveedor.setNombre(proveedorDTO.getNombre());
        proveedor.setRepresentante(proveedorDTO.getRepresentante());
        proveedor.setCorreo(proveedorDTO.getCorreo());
        proveedor.setDireccion(proveedorDTO.getDireccion());
        proveedor.setTelefono(proveedorDTO.getTelefono());

        return proveedor;
    }

    // Mapeo de lista de Ventas a lista de VentasDTO
    public static List<VentaDTO> toVentaDTOList(List<Venta> ventas) {
        if (ventas == null) {
            return new ArrayList<>();
        }

        return ventas.stream()
                .map(Convertor::toVentaDTO)
                .collect(Collectors.toList());
    }

    // Métodos similares para las demás listas
    public static List<ProductoDTO> toProductoDTOList(List<Producto> productos) {
        if (productos == null) {
            return new ArrayList<>();
        }

        return productos.stream()
                .map(Convertor::toProductoDTO)
                .collect(Collectors.toList());
    }

    public static List<ProveedorDTO> toProveedorDTOList(List<Proveedor> proveedores) {
        if (proveedores == null) {
            return new ArrayList<>();
        }

        return proveedores.stream()
                .map(Convertor::toProveedorDTO)
                .collect(Collectors.toList());
    }

    public static List<UsuarioDTO> toUsuarioDTOList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return new ArrayList<>();
        }

        return usuarios.stream()
                .map(Convertor::toUsuarioDTO)
                .collect(Collectors.toList());
    }
}
