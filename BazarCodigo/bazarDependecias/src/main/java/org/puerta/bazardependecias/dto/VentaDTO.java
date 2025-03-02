package org.puerta.bazardependecias.dto;

import java.util.Date;
import java.util.List;

/**
 *
 * @author julli
 */
public class VentaDTO {
     private Long id;
    private Float total;
    private Float totalDescuento;
    private Date fecha;
    private List<DetalleDTO> detalles;

    public VentaDTO() {
    }

    public VentaDTO(Long id, Float total, Float totalDescuento, Date fecha, List<DetalleDTO> detalles) {
        this.id = id;
        this.total = total;
        this.totalDescuento = totalDescuento;
        this.fecha = fecha;
        this.detalles = detalles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(Float totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<DetalleDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleDTO> detalles) {
        this.detalles = detalles;
    }
    
    
}
