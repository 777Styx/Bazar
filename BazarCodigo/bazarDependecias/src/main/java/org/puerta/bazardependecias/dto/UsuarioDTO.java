package org.puerta.bazardependecias.dto;

/**
 *
 * @author julli
 */
public class UsuarioDTO {
     private Long id;
    private String nombre;
    private String contrasena; 

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String nombre, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    
}
