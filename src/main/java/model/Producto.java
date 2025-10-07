/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Kevin Velasquez
 */

@Entity
@Table(name = "Productos")
public class Producto implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProducto")
    private int idProducto;
    
    @Column(name = "nombreProducto", nullable = false, length = 255)
    private String nombreProducto;
    
    @Column(name = "descripcionProducto", nullable = false)
    private String descripcionProducto;
    
    @Column(name = "precio", nullable = false)
    private double precio;

    @Column(name = "stock", nullable = false)
    private int stock;
    
    @Column(name = "categoria", nullable = false, length = 255)
    private String categoria;
    
    @Column(name = "marca", nullable = false, length = 255)
    private String marca;

    @Column(name = "fechaCreacion", nullable = false, updatable = false)
    private Timestamp fechaCreacion;

    public Producto() {
    }

    public Producto(String nombreProducto, String descripcionProducto, double precio, int stock, String categoria, String marca, Timestamp fechaCreacion) {
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.marca = marca;
        fechaCreacion = Timestamp.from(Instant.now());
    }
    
    
    public Producto(int idProducto, String nombreProducto, String descripcionProducto, double precio, int stock, String categoria, String marca, Timestamp fechaCreacion) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.marca = marca;
        this.fechaCreacion = fechaCreacion;
    }
    
    // --- Getters y Setters ---

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Producto{" + "idProducto=" + idProducto + ", nombreProducto=" + nombreProducto + ", precio=" + precio + '}';
    }
    
    
    
    
    
    
}
