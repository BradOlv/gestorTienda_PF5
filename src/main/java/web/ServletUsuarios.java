package web;

import dao.UsuariosDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Usuario;
import model.RolUsuario;

/**
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
            default:
                listarUsuarios(request, response);
        }
    }



    // --- Métodos de CRUD CORREGIDOS ---

    protected void agregarUsuario(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        String nombre = solicitud.getParameter("nombreUsuario");
        String apellido = solicitud.getParameter("apellidoUsuario");
        String email = solicitud.getParameter("emailUsuario");
        String telefono = solicitud.getParameter("telefonoUsuario");
        String direccion = solicitud.getParameter("direccionUsuario");
        String contrasena = solicitud.getParameter("contrasena");
        String nit = solicitud.getParameter("nit");
        String rolStr = solicitud.getParameter("rol");

        // 🛑 CORRECCIÓN CLAVE: VALIDACIÓN para evitar el error '...cannot be null' en la BD
        if (nombre == null || nombre.trim().isEmpty() || 
            apellido == null || apellido.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            contrasena == null || contrasena.trim().isEmpty() ||
            nit == null || nit.trim().isEmpty() ||
            rolStr == null || rolStr.trim().isEmpty()) 
        {
            // Manejar el error: El usuario no rellenó todos los campos obligatorios
            solicitud.setAttribute("error", "Todos los campos obligatorios (Nombre, Apellido, Email, Contraseña, NIT, Rol) deben ser llenados.");
            solicitud.setAttribute("usuario", new Usuario()); // Pasa un usuario vacío
            solicitud.getRequestDispatcher("/pages/editarUsuario.jsp").forward(solicitud, respuesta);
            return; // Detener la ejecución
        }

        RolUsuario rol;
        try {
             // Convertir String a Enumeración (RolUsuario)
             rol = RolUsuario.valueOf(rolStr);
        } catch (IllegalArgumentException e) {
             solicitud.setAttribute("error", "El rol seleccionado no es válido.");
             solicitud.setAttribute("usuario", new Usuario());
             solicitud.getRequestDispatcher("/pages/editarUsuario.jsp").forward(solicitud, respuesta);
             return;
        }

        // Crear el nuevo objeto Usuario con el constructor de 8 argumentos
        Usuario nuevoUsuario = new Usuario(nombre, apellido, email, telefono, direccion, contrasena, nit, rol);

        try {
             usuarioDao.saveUsuario(nuevoUsuario);
             // Redirigir al listado si todo fue exitoso
             respuesta.sendRedirect("UsuarioServlet?accion=listar");
        } catch (RuntimeException e) {
             // Manejar errores específicos de la BD (como email duplicado si lo tienes)
             solicitud.setAttribute("error", "Error al guardar el usuario: " + e.getMessage());
             solicitud.setAttribute("usuario", nuevoUsuario);
             solicitud.getRequestDispatcher("/pages/editarUsuario.jsp").forward(solicitud, respuesta);
        }
    }


    protected void formularioEditar(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idEditar = 0;
        try {
            idEditar = Integer.parseInt(solicitud.getParameter("id"));
        } catch (NumberFormatException e) {
            // El id=0 es para la acción de "Agregar"
        }
        
        Usuario usuario;
        if (idEditar > 0) {
            usuario = usuarioDao.getUsuarioById(idEditar);
        } else {
            // Inicializar un objeto vacío para el formulario "Agregar"
            usuario = new Usuario(); 
            // Aseguramos que tenga un Rol por defecto para que el JSP no falle
            usuario.setRol(RolUsuario.Cliente);
        }

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
        
        // CAPTURAR NIT Y ROL
        String nit = solicitud.getParameter("nit");
        String rolStr = solicitud.getParameter("rol");

        Usuario usuario = usuarioDao.getUsuarioById(idActualizar);

        if (usuario != null) {
            usuario.setNombreUsuario(nombre);
            usuario.setApellidoUsuario(apellido);
            usuario.setEmailUsuario(email);
            usuario.setTelefonoUsuario(telefono);
            usuario.setDireccionUsuario(direccion);
            
            // Actualizar campos nuevos
            usuario.setNit(nit);
            
            // Convertir y setear el Rol
            try {
                usuario.setRol(RolUsuario.valueOf(rolStr)); 
            } catch (IllegalArgumentException e) {
                // Manejar error de rol inválido, si es necesario.
                System.err.println("Rol inválido en la actualización: " + rolStr);
            }

            // Solo actualizar contraseña si se proporciona un valor
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