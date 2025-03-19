package org.puerta.bazardependecias.dto;

public class ProductoDTO {
    private Long id;
    private String nombre;
    private Float precio;
    private Integer stock;
    private Integer canDes;
    private Long proveedorId;
    private String nombreProveedor;
    
    // Constructores
    public ProductoDTO() {
    }
    
    public ProductoDTO(Long id, String nombre, Float precio, Integer stock, Integer canDes, Long proveedorId) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.canDes = canDes;
        this.proveedorId = proveedorId;
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Float getPrecio() {
        return precio;
    }
    
    public void setPrecio(Float precio) {
        this.precio = precio;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    public Integer getCanDes() {
        return canDes;
    }
    
    public void setCanDes(Integer canDes) {
        this.canDes = canDes;
    }
    
    public Long getProveedorId() {
        return proveedorId;
    }
    
    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }
    
    public String getNombreProveedor() {
        return nombreProveedor;
    }
    
    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }
}