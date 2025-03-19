package org.puerta.bazardependecias.dto;

public class DetalleDTO {
    
    private Long id;
    private Float importe;
    private Float precio;
    private Integer cantidad;
    private Integer canDes;
    private Long ventaId;
    private Long productoId;
    private String nombreProducto;
    
    // Constructores
    public DetalleDTO() {
    }
    
    public DetalleDTO(Long id, Float importe, Float precio, Integer cantidad, Integer canDes, Long productoId) {
        this.id = id;
        this.importe = importe;
        this.precio = precio;
        this.cantidad = cantidad;
        this.canDes = canDes;
        this.productoId = productoId;
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Float getImporte() {
        return importe;
    }
    
    public void setImporte(Float importe) {
        this.importe = importe;
    }
    
    public Float getPrecio() {
        return precio;
    }
    
    public void setPrecio(Float precio) {
        this.precio = precio;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public Integer getCanDes() {
        return canDes;
    }
    
    public void setCanDes(Integer canDes) {
        this.canDes = canDes;
    }
    
    public Long getVentaId() {
        return ventaId;
    }
    
    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }
    
    public Long getProductoId() {
        return productoId;
    }
    
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
}