package model;

import java.io.Serializable;
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
 * @author Bradley Oliva
 */
@Entity
@Table(name = "Usuarios")
public class Usuario implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private int idUsuario;
    
    @Column(name = "nombreUsuario", nullable = false, length = 50)
    private String nombreUsuario;
    
    @Column(name = "apellidoUsuario", nullable = false, length = 50)
    private String apellidoUsuario;
    
    @Column(name = "emailUsuario", nullable = false, unique = true, length = 100)
    private String emailUsuario;
    
    @Column(name = "telefonoUsuario", length = 20)
    private String telefonoUsuario;
    
    @Column(name = "direccionUsuario", length = 255)
    private String direccionUsuario;
    
    @Column(name = "fechaRegistro", nullable = false)
    @Temporal(TemporalType.TIMESTAMP) 
    private Date fechaRegistro;
    
    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String apellidoUsuario, String emailUsuario, String telefonoUsuario, String direccionUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.emailUsuario = emailUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.direccionUsuario = direccionUsuario;
        this.contrasena = contrasena;
        this.fechaRegistro = new Date(); 
    }

    // --- Getters y Setters ---

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public void setTelefonoUsuario(String telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }

    public String getDireccionUsuario() {
        return direccionUsuario;
    }

    public void setDireccionUsuario(String direccionUsuario) {
        this.direccionUsuario = direccionUsuario;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", nombreUsuario=" + nombreUsuario + '}';
    }
}