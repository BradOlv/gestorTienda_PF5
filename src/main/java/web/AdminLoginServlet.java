/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web;

import java.io.IOException;
import model.Usuario;
import model.RolUsuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Kevin
 */
@WebServlet("/admin-login")
public class AdminLoginServlet extends HttpServlet {

    private EntityManagerFactory entityManagerFactory;

    @Override
    public void init() throws ServletException {
        entityManagerFactory = Persistence.createEntityManagerFactory("libreriaPU");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            // Recuperar los datos del formulario
            String email = request.getParameter("email");
            String contrasena = request.getParameter("contrasena");

            // Validación básica de campos
            if (email == null || email.isEmpty() || contrasena == null || contrasena.isEmpty()) {
                response.sendRedirect("pages/admin-login.jsp?error=campos_vacios");
                return;
            }

            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            Usuario usuario = null;
            try {
                // Buscar el usuario por email
                usuario = entityManager.createQuery("SELECT u FROM Usuario u WHERE u.emailUsuario = :email", Usuario.class)
                                     .setParameter("email", email)
                                     .getSingleResult();
            } catch (NoResultException e) {
                // Usuario no encontrado
                response.sendRedirect("pages/admin-login.jsp?error=no_encontrado");
                return;
            }

            // Verificar contraseña
            if (usuario != null && usuario.getContrasena().equals(contrasena)) {
                
                // VERIFICAR QUE SEA ADMINISTRADOR
                if (!RolUsuario.Admin.equals(usuario.getRol())) {
                    response.sendRedirect("pages/admin-login.jsp?error=no_autorizado");
                    return;
                }
                
                // Si es admin y las credenciales son correctas
                HttpSession session = request.getSession();
                session.setAttribute("idUsuario", usuario.getIdUsuario());
                session.setAttribute("nombre", usuario.getNombreUsuario());
                session.setAttribute("rol", usuario.getRol().toString());
                session.setAttribute("nit", usuario.getNit());
                session.setAttribute("esAdmin", true); // Flag adicional para verificaciones

                transaction.commit();

                // Redirigir al dashboard de administración
                response.sendRedirect("pages/menuPrincipal.jsp");
                
            } else {
                // Credenciales incorrectas
                transaction.rollback();
                response.sendRedirect("pages/admin-login.jsp?error=credenciales");
            }

        } catch (Exception e) {
            e.printStackTrace(); 
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            response.sendRedirect("pages/admin-login.jsp?error=error_servidor");
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