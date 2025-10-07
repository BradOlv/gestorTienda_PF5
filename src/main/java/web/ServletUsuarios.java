package web;

import dao.UsuariosDAO;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Usuario;

/**
 *
 * @author Bradley Oliva
 */
@WebServlet(name = "UsuarioServlet", urlPatterns = {"/UsuarioServlet"})
public class ServletUsuarios extends HttpServlet {
    
    private final UsuariosDAO usuarioDao = new UsuariosDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; 
        }
        
        switch (accion) {
            case "agregar":
                agregarUsuario(request, response);
                break;
            case "editar":
                formularioEditar(request, response);
                break;
            case "actualizar":
                actualizarUsuario(request, response);
                break;
            case "eliminar":
                eliminarUsuario(request, response);
                break;
            case "listar":
                listarUsuarios(request, response);
                break;
            default:
                listarUsuarios(request, response); 
        }
    }

    // --- Métodos de CRUD ---
    
    protected void agregarUsuario(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        String nombre = solicitud.getParameter("nombreUsuario");
        String apellido = solicitud.getParameter("apellidoUsuario");
        String email = solicitud.getParameter("emailUsuario");
        String telefono = solicitud.getParameter("telefonoUsuario");
        String direccion = solicitud.getParameter("direccionUsuario");
        String contrasena = solicitud.getParameter("contrasena"); // ¡OJO! En un proyecto real, la contraseña debe ser HASHED.
        
        Usuario nuevoUsuario = new Usuario(nombre, apellido, email, telefono, direccion, contrasena);
        usuarioDao.saveUsuario(nuevoUsuario);
        
        respuesta.sendRedirect("UsuarioServlet?accion=listar");
    }
    
    protected void formularioEditar(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idEditar = Integer.parseInt(solicitud.getParameter("id"));
        Usuario usuario = usuarioDao.getUsuarioById(idEditar);
        
        solicitud.setAttribute("usuario", usuario);
        solicitud.getRequestDispatcher("/pages/editarUsuario.jsp").forward(solicitud, respuesta);
    }
    
    protected void actualizarUsuario(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idActualizar = Integer.parseInt(solicitud.getParameter("idUsuario"));
        String nombre = solicitud.getParameter("nombreUsuario");
        String apellido = solicitud.getParameter("apellidoUsuario");
        String email = solicitud.getParameter("emailUsuario");
        String telefono = solicitud.getParameter("telefonoUsuario");
        String direccion = solicitud.getParameter("direccionUsuario");
        String contrasena = solicitud.getParameter("contrasena"); 

        Usuario usuario = usuarioDao.getUsuarioById(idActualizar);
        
        if (usuario != null) {
            usuario.setNombreUsuario(nombre);
            usuario.setApellidoUsuario(apellido);
            usuario.setEmailUsuario(email);
            usuario.setTelefonoUsuario(telefono);
            usuario.setDireccionUsuario(direccion);
            if (contrasena != null && !contrasena.isEmpty()) {
                usuario.setContrasena(contrasena);
            }
            
            usuarioDao.updateUsuario(usuario);
        }
        
        respuesta.sendRedirect("UsuarioServlet?accion=listar");
    }
    
    protected void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idEliminar = Integer.parseInt(request.getParameter("id"));
        usuarioDao.deleteUsuario(idEliminar);
        response.sendRedirect("UsuarioServlet?accion=listar");
    }
    
    protected void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Usuario> listaUsuarios = usuarioDao.getAllUsuarios();
        
        request.setAttribute("listaUsuarios", listaUsuarios);
        
        request.getRequestDispatcher("/pages/administracionUsuarios.jsp").forward(request, response);
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Servlet para gestionar usuarios";
    }
}