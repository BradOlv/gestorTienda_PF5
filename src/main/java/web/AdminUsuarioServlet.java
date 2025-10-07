
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import model.Usuario;
import model.RolUsuario;
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
import java.util.List;
/**
 *
 * @author Kevin
 */
@WebServlet("/admin/usuarios")
public class AdminUsuarioServlet extends HttpServlet {

    private EntityManagerFactory entityManagerFactory;

    @Override
    public void init() throws ServletException {
        entityManagerFactory = Persistence.createEntityManagerFactory("libreriaPU");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verificar que sea administrador
        if (!esAdministrador(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listarUsuarios(request, response);
                break;
            case "create":
                mostrarFormularioCrear(request, response);
                break;
            case "edit":
                mostrarFormularioEditar(request, response);
                break;
            case "delete":
                eliminarUsuario(request, response);
                break;
            default:
                listarUsuarios(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verificar que sea administrador
        if (!esAdministrador(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "create";

        switch (action) {
            case "create":
                crearUsuario(request, response);
                break;
            case "update":
                actualizarUsuario(request, response);
                break;
            default:
                response.sendRedirect("usuarios.jsp");
                break;
        }
    }

    private boolean esAdministrador(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUsuario") == null) {
            response.sendRedirect("../pages/login.jsp?error=acceso_denegado");
            return false;
        }

        String rol = (String) session.getAttribute("rol");
        if (!"Admin".equals(rol)) {
            response.sendRedirect("../menuPrincipal.jsp?error=no_autorizado");
            return false;
        }
        return true;
    }

    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = null;
        try {
            em = entityManagerFactory.createEntityManager();
            List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u ORDER BY u.idUsuario DESC", Usuario.class)
                    .getResultList();
            
            request.setAttribute("usuarios", usuarios);
            request.getRequestDispatcher("usuarios.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("dashboard.jsp?error=error_servidor");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private void mostrarFormularioCrear(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("action", "create");
        request.getRequestDispatcher("form-usuario.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect("usuarios.jsp?error=id_requerido");
            return;
        }

        EntityManager em = null;
        try {
            int id = Integer.parseInt(idStr);
            em = entityManagerFactory.createEntityManager();
            Usuario usuario = em.find(Usuario.class, id);
            
            if (usuario == null) {
                response.sendRedirect("usuarios.jsp?error=usuario_no_encontrado");
                return;
            }

            request.setAttribute("usuario", usuario);
            request.setAttribute("action", "edit");
            request.getRequestDispatcher("form-usuario.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("usuarios.jsp?error=id_invalido");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("usuarios.jsp?error=error_servidor");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private void crearUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            // Obtener parámetros del formulario
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");
            String contrasena = request.getParameter("contrasena");
            String nit = request.getParameter("nit");
            String rolString = request.getParameter("rol");

            // Validaciones
            if (nombre == null || nombre.trim().isEmpty() ||
                apellido == null || apellido.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty() ||
                nit == null || nit.trim().isEmpty()) {
                
                response.sendRedirect("usuarios?action=create&error=campos_incompletos");
                return;
            }

            // Convertir rol
            RolUsuario rol = RolUsuario.Cliente;
            if (rolString != null && !rolString.isEmpty()) {
                try {
                    rol = RolUsuario.valueOf(rolString);
                } catch (IllegalArgumentException e) {
                    rol = RolUsuario.Cliente;
                }
            }

            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            // Verificar si el email ya existe
            Long count = (Long) em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.emailUsuario = :email")
                    .setParameter("email", email)
                    .getSingleResult();
            
            if (count > 0) {
                response.sendRedirect("usuarios?action=create&error=email_existe");
                return;
            }

            // Crear usuario
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombre.trim());
            usuario.setApellidoUsuario(apellido.trim());
            usuario.setEmailUsuario(email.trim());
            usuario.setTelefonoUsuario(telefono != null ? telefono.trim() : "");
            usuario.setDireccionUsuario(direccion != null ? direccion.trim() : "");
            usuario.setContrasena(contrasena); // En producción: hashear
            usuario.setNit(nit.trim());
            usuario.setRol(rol);
            usuario.setFechaRegistro(new Date());
            em.persist(usuario);
            transaction.commit();

            response.sendRedirect("usuarios?success=usuario_creado");

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            response.sendRedirect("usuarios?action=create&error=error_servidor");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            String idStr = request.getParameter("id");
            if (idStr == null) {
                response.sendRedirect("usuarios?error=id_requerido");
                return;
            }

            int id = Integer.parseInt(idStr);
            
            // Obtener parámetros
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("direccion");
            String nit = request.getParameter("nit");
            String rolString = request.getParameter("rol");

            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            Usuario usuario = em.find(Usuario.class, id);
            if (usuario == null) {
                response.sendRedirect("usuarios?error=usuario_no_encontrado");
                return;
            }

            // Actualizar datos
            usuario.setNombreUsuario(nombre.trim());
            usuario.setApellidoUsuario(apellido.trim());
            usuario.setEmailUsuario(email.trim());
            usuario.setTelefonoUsuario(telefono != null ? telefono.trim() : "");
            usuario.setDireccionUsuario(direccion != null ? direccion.trim() : "");
            usuario.setNit(nit.trim());
            
            if (rolString != null && !rolString.isEmpty()) {
                try {
                    usuario.setRol(RolUsuario.valueOf(rolString));
                } catch (IllegalArgumentException e) {
                    // Mantener el rol actual si es inválido
                }
            }

            em.merge(usuario);
            transaction.commit();

            response.sendRedirect("usuarios?success=usuario_actualizado");

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            response.sendRedirect("usuarios?error=error_servidor");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            String idStr = request.getParameter("id");
            if (idStr == null) {
                response.sendRedirect("usuarios?error=id_requerido");
                return;
            }

            int id = Integer.parseInt(idStr);
            
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            Usuario usuario = em.find(Usuario.class, id);
            if (usuario == null) {
                response.sendRedirect("usuarios?error=usuario_no_encontrado");
                return;
            }

            // No permitir eliminar el último administrador
            if (RolUsuario.Admin.equals(usuario.getRol())) {
                Long adminCount = (Long) em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.rol = :rol")
                        .setParameter("rol", RolUsuario.Admin)
                        .getSingleResult();
                
                if (adminCount <= 1) {
                    response.sendRedirect("usuarios?error=ultimo_admin");
                    return;
                }
            }

            em.remove(usuario);
            transaction.commit();

            response.sendRedirect("usuarios?success=usuario_eliminado");

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            response.sendRedirect("usuarios?error=error_servidor");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
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
