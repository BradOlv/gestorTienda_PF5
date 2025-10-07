package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    
    // CAMPOS NIT Y ROL AGREGADOS
    // Se ha ajustado la longitud del NIT a 64 para ser consistente con el JSP
    @Column(name = "nit", nullable = false, length = 64) 
    private String nit;
    
    @Column(name = "rol", nullable = false)
    @Enumerated(EnumType.STRING) // Mapeo correcto para Enum
    private RolUsuario rol = RolUsuario.Cliente; // Valor por defecto

    /**
     * Constructor vacío requerido por JPA.
     */
    public Usuario() {
    }

    /**
     * Constructor para AGREGAR NUEVOS USUARIOS desde el Servlet.
     * Este constructor tiene 8 argumentos (sin idUsuario ni fechaRegistro, ya que se generan).
     */
    public Usuario(String nombreUsuario, String apellidoUsuario, String emailUsuario, 
                   String telefonoUsuario, String direccionUsuario, String contrasena,
                   String nit, RolUsuario rol) {
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.emailUsuario = emailUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.direccionUsuario = direccionUsuario;
        this.contrasena = contrasena;
        this.nit = nit;
        this.rol = rol;
        this.fechaRegistro = new Date(); // Establecer la fecha aquí o dejar que el DAO lo haga
    }
    
    /**
     * Constructor completo para obtener o cargar usuarios.
     * Este constructor tiene 10 argumentos.
     */
    public Usuario(int idUsuario, String nombreUsuario, String apellidoUsuario, String emailUsuario, 
                   String telefonoUsuario, String direccionUsuario, Date fechaRegistro, 
                   String contrasena, String nit, RolUsuario rol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.emailUsuario = emailUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.direccionUsuario = direccionUsuario;
        this.fechaRegistro = fechaRegistro;
        this.contrasena = contrasena;
        this.nit = nit;
        this.rol = rol;
    }

    // El constructor de 6 argumentos fue eliminado ya que es obsoleto al añadir nit y rol.

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

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", nombreUsuario=" + nombreUsuario + '}';
    }
}