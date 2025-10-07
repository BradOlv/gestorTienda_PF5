package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entidad JPA para la tabla Clientes
 *
 * @author Bradley Oliva
 */

@Entity
@Table(name = "Clientes")
public class Cliente implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente")
    private int idCliente;
    
    @Column(name = "nombreCliente", nullable = false, length = 100)
    private String nombreCliente;
    
    @Column(name = "apellidoCliente", nullable = false, length = 100)
    private String apellidoCliente;
    
    @Column(name = "emailCliente", unique = true, length = 100)
    private String emailCliente;
    
    @Column(name = "telefonoCliente", length = 20)
    private String telefonoCliente;
    
    @Column(name = "direccionCliente", length = 255)
    private String direccionCliente;
    
    @Column(name = "nitCliente", length = 20)
    private String nitCliente;

    public Cliente() {
    }

    public Cliente(String nombreCliente, String apellidoCliente, String emailCliente, String telefonoCliente, String direccionCliente, String nitCliente) {
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.emailCliente = emailCliente;
        this.telefonoCliente = telefonoCliente;
        this.direccionCliente = direccionCliente;
        this.nitCliente = nitCliente;
    }

    // --- Getters y Setters ---

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getApellidoCliente() { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }
    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }
    public String getDireccionCliente() { return direccionCliente; }
    public void setDireccionCliente(String direccionCliente) { this.direccionCliente = direccionCliente; }
    public String getNitCliente() { return nitCliente; }
    public void setNitCliente(String nitCliente) { this.nitCliente = nitCliente; }

    @Override
    public String toString() {
        return "Cliente{" + "idCliente=" + idCliente + ", nombreCliente=" + nombreCliente + '}';
    }
}