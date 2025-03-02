package org.puerta.bazardependecias.dto;

/**
 *
 * @author julli
 */
public class ProductoDTO {

    private Long id;
    private String nombre;
    private Float precio;
    private Integer stock;

    public ProductoDTO() {
    }

    public ProductoDTO(Long id, String nombre, Float precio, Integer stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
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

}
