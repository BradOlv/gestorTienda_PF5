package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

/**
 * @author Kevin Velasquez
 */
@Entity
@Table(name = "DetalleVentas")
public class DetalleVenta implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalleVenta")
    private int idDetalleVenta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idVenta", nullable = false)
    private Venta venta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;
    
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
    
    @Column(name = "precioUnitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    // Constructor vacío
    public DetalleVenta() {
    }
    
    // Constructor sin ID (para crear nuevos detalles)
    public DetalleVenta(Venta venta, Producto producto, int cantidad, BigDecimal precioUnitario) {
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    // Constructor completo con ID
    public DetalleVenta(int idDetalleVenta, Venta venta, Producto producto, int cantidad, BigDecimal precioUnitario) {
        this.idDetalleVenta = idDetalleVenta;
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    // --- Getters y Setters ---
    
    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    /**
     * Método auxiliar para calcular el subtotal del detalle.
     * @return cantidad * precioUnitario
     */
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    @Override
    public String toString() {
        return "DetalleVenta{" +  "idDetalleVenta=" + idDetalleVenta + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + ", subtotal=" + getSubtotal() + '}';
    }
}