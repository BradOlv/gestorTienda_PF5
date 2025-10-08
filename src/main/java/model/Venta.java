package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;

/**
 * Entidad JPA para la tabla Ventas, con relaciones ManyToOne a Cliente y Usuario.
 * @author Bradley Oliva
 */

@Entity
@Table(name = "Ventas")
public class Venta implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVenta")
    private int idVenta;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;
    
    @Column(name = "fechaVenta", nullable = false)
    private LocalDateTime fechaVenta;
    
    @Column(name = "totalVenta", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalVenta; 
    
    @Column(name = "estadoVenta", nullable = false, length = 50)
    private String estadoVenta;

    public Venta() {
        this.fechaVenta = LocalDateTime.now(); 
        this.estadoVenta = "Pendiente"; 
    }
    
    public Venta(Cliente cliente, Usuario usuario, BigDecimal totalVenta, String estadoVenta) {
        this.cliente = cliente;
        this.usuario = usuario;
        this.totalVenta = totalVenta;
        this.estadoVenta = estadoVenta;
        this.fechaVenta = LocalDateTime.now();
    }
    
    public Venta(int idVenta, Cliente cliente, Usuario usuario, LocalDateTime fechaVenta, BigDecimal totalVenta, String estadoVenta) {
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.usuario = usuario;
        this.fechaVenta = fechaVenta;
        this.totalVenta = totalVenta;
        this.estadoVenta = estadoVenta;
    }

    // --- Getters y Setters ---

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }
    
    public BigDecimal getTotalVenta() { return totalVenta; }
    public void setTotalVenta(BigDecimal totalVenta) { this.totalVenta = totalVenta; }
    
    public String getEstadoVenta() { return estadoVenta; }
    public void setEstadoVenta(String estadoVenta) { this.estadoVenta = estadoVenta; }

    @Override
    public String toString() {
        return "Venta{" + "idVenta=" + idVenta + ", totalVenta=" + totalVenta + ", estadoVenta=" + estadoVenta + '}';
    }
}