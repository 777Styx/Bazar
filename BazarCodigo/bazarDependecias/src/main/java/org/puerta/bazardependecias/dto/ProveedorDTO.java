package org.puerta.bazardependecias.dto;

public class ProveedorDTO {
    private Long id;
    private String nombre;
    private String representante;
    private String correo;
    private String direccion;
    private String telefono;
    
    // Constructores
    public ProveedorDTO() {
    }
    
    public ProveedorDTO(Long id, String nombre, String representante, String correo, String direccion, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.representante = representante;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
    }
    
    public ProveedorDTO(String nombre, String representante, String correo, String direccion, String telefono) {
        this.nombre = nombre;
        this.representante = representante;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
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
    
    public String getRepresentante() {
        return representante;
    }
    
    public void setRepresentante(String representante) {
        this.representante = representante;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}