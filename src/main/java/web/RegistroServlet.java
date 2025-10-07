package web;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import model.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.RolUsuario;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    private EntityManagerFactory entityManagerFactory;

    @Override
    public void init() throws ServletException {
        entityManagerFactory = Persistence.createEntityManagerFactory("libreriaPU");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().println("El servlet está funcionando.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // REGISTRO PÚBLICO HABILITADO - Cualquiera puede registrarse
        // (Comenta estas líneas si quieres bloquear el registro público)
        /*
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            response.sendRedirect("pages/login.jsp?error=registro_bloqueado");
            return;
        }
        
        String rolUsuario = (String) session.getAttribute("rol");
        if (!"Admin".equals(rolUsuario)) {
            response.sendRedirect("pages/login.jsp?error=no_autorizado");
            return;
        }
        */

        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            // Obtener parámetros del formulario
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String emailUsuario = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");
            String contrasena = request.getParameter("contrasena");
            String nit = request.getParameter("nit");
            
            // Para registro público, todos son clientes por defecto
            RolUsuario rol = RolUsuario.Cliente;
            
            // Validación básica de campos obligatorios
            if (nombre == null || nombre.trim().isEmpty() ||
                apellido == null || apellido.trim().isEmpty() ||
                emailUsuario == null || emailUsuario.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty() ||
                nit == null || nit.trim().isEmpty()) {
                response.sendRedirect("pages/register.jsp?error=campos_incompletos");
                return;
            }

            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            // Verificar si el correo ya está registrado
            Long count = (Long) entityManager.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.emailUsuario = :email")
                    .setParameter("email", emailUsuario)
                    .getSingleResult();
            
            if (count > 0) {
                response.sendRedirect("pages/register.jsp?error=ya_existe");
                return;
            }

            // Crear nuevo usuario
            Usuario usuario = new Usuario();
            
            
            usuario.setNombreUsuario(nombre);                
            usuario.setApellidoUsuario(apellido);             
            usuario.setEmailUsuario(emailUsuario);          
            usuario.setTelefonoUsuario(telefono);            
            usuario.setDireccionUsuario(direccion);          
            usuario.setContrasena(contrasena);        
            usuario.setNit(nit);                    
            usuario.setRol(rol);                      
            usuario.setFechaRegistro(Timestamp.valueOf(LocalDateTime.now()));

            // Persistir usuario
            entityManager.persist(usuario);
            transaction.commit();

            // Redirigir con éxito
            response.sendRedirect("pages/login.jsp?register=exitoso");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR EN SERVLET: " + e.getMessage()); 
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            response.sendRedirect("pages/register.jsp?error=servidor");
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public void destroy() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}