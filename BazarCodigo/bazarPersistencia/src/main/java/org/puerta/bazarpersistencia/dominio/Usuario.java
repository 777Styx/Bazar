package org.puerta.bazarpersistencia.dominio;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String contrasena;
    
    @OneToMany(mappedBy = "usuario")
    private List<Venta> ventas;
    
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
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public List<Venta> getVentas() {
        return ventas;
    }
    
    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }
}
