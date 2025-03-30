package org.puerta.bazardependecias.dto;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    // No incluimos la contraseña por seguridad en el DTO

    // Constructores
    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public UsuarioDTO(String nombre) {
        this.nombre = nombre;
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
}