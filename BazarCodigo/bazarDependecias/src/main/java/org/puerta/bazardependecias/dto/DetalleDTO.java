package org.puerta.bazardependecias.dto;

/**
 *
 * @author julli
 */
public class DetalleDTO {

    private Long id;
    private Integer canDes;
    private Float importe;
    private Float precio;
    private Integer cantidad;

    public DetalleDTO() {
    }

    public DetalleDTO(Long id, Integer canDes, Float importe, Float precio, Integer cantidad) {
        this.id = id;
        this.canDes = canDes;
        this.importe = importe;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCanDes() {
        return canDes;
    }

    public void setCanDes(Integer canDes) {
        this.canDes = canDes;
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
    
}
