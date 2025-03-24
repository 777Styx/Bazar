package org.puerta.bazarpersistencia.dominio;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle")
public class Detalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Float importe;
    private Float precio;
    private Integer cantidad;
    private Integer canDes;
    
    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;
    
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
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
    
    public Venta getVenta() {
        return venta;
    }
    
    public void setVenta(Venta venta) {
        this.venta = venta;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
