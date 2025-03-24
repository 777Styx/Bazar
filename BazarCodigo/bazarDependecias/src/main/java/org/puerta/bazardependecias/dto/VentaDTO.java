package org.puerta.bazardependecias.dto;

import java.util.Date;
import java.util.List;

public class VentaDTO {
    private Long id;
    private Float total;
    private Float totalDescuento;
    private Date fecha;
    private Long usuarioId;
    private String nombreUsuario;
    private List<DetalleDTO> detalles;
    
    // Constructores
    public VentaDTO() {
    }
    
    public VentaDTO(Long id, Float total, Float totalDescuento, Date fecha, Long usuarioId) {
        this.id = id;
        this.total = total;
        this.totalDescuento = totalDescuento;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
    }
    
    public VentaDTO(Float total, Float totalDescuento, Date fecha, Long usuarioId) {
        this.total = total;
        this.totalDescuento = totalDescuento;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
    }
    
    // Getters y setters
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
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public List<DetalleDTO> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleDTO> detalles) {
        this.detalles = detalles;
    }
}