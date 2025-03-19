/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.puerta.bazardependecias.dto;

/**
 *
 * @author olive
 */
public class UsuarioAuthDTO {
    private String nombre;
    private String contrasena;
    // No incluimos la contraseña por seguridad en el DTO
    
    // Constructores
    public UsuarioAuthDTO() {
    }
    
    public UsuarioAuthDTO(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }
    
    // Getters y setters
    
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
