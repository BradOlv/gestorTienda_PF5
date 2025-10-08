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
        
        // 🚀 SOLUCIÓN AL ERROR: Declarar redirectUrl fuera del try
        String redirectUrl = null; 

        try {
            // Recuperar los datos del formulario
            String email = request.getParameter("email");
            String contrasena = request.getParameter("contrasena");
            
            // ASIGNACIÓN: Solo asignar el valor aquí
            redirectUrl = request.getParameter("redirect");
            String contextPath = request.getContextPath(); // Para redirecciones absolutas


            // Validación básica de campos
            if (email == null || email.isEmpty() || contrasena == null || contrasena.isEmpty()) {
                String errorRedirect = "pages/admin-login.jsp?error=campos_vacios";
                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    errorRedirect += "&redirect=" + redirectUrl;
                }
                response.sendRedirect(errorRedirect);
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
                String errorRedirect = "pages/admin-login.jsp?error=no_encontrado";
                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    errorRedirect += "&redirect=" + redirectUrl;
                }
                response.sendRedirect(errorRedirect);
                return;
            }

            // Verificar contraseña
            if (usuario != null && usuario.getContrasena().equals(contrasena)) {
                
                // VERIFICAR QUE SEA ADMINISTRADOR
                if (!RolUsuario.Admin.equals(usuario.getRol())) {
                    String errorRedirect = "pages/admin-login.jsp?error=no_autorizado";
                    if (redirectUrl != null && !redirectUrl.isEmpty()) {
                        errorRedirect += "&redirect=" + redirectUrl;
                    }
                    response.sendRedirect(errorRedirect);
                    return;
                }
                
                // Si es admin y las credenciales son correctas
                HttpSession session = request.getSession();
                
                // Configuración de la sesión
                session.setAttribute("usuarioLoggeado", usuario); 
                session.setAttribute("idUsuario", usuario.getIdUsuario());
                session.setAttribute("nombre", usuario.getNombreUsuario());
                session.setAttribute("rol", usuario.getRol().toString());
                session.setAttribute("nit", usuario.getNit());
                session.setAttribute("esAdmin", true); 

                transaction.commit();

                // 🚀 REDIRECCIÓN CLAVE
                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    response.sendRedirect(contextPath + redirectUrl);
                } else {
                    response.sendRedirect("pages/admin-dashboard.jsp");
                }
                
            } else {
                // Credenciales incorrectas - Preservar 'redirect'
                transaction.rollback();
                String errorRedirect = "pages/admin-login.jsp?error=credenciales";
                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    errorRedirect += "&redirect=" + redirectUrl;
                }
                response.sendRedirect(errorRedirect);
            }

        } catch (Exception e) {
            e.printStackTrace(); 
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            // Error de servidor - Usa redirectUrl (ya accesible)
            String errorRedirect = "pages/admin-login.jsp?error=error_servidor";
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                errorRedirect += "&redirect=" + redirectUrl;
            }
            response.sendRedirect(errorRedirect);
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