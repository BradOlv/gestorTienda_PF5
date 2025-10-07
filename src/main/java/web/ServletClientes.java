package web;

import dao.ClientesDAO;
import model.Cliente;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Bradley Oliva
 */
@WebServlet(name = "ClienteServlet", urlPatterns = {"/ClienteServlet"})
public class ServletClientes extends HttpServlet {
    
    private final ClientesDAO clienteDao = new ClientesDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; 
        }
        
        switch (accion) {
            case "agregar":
                agregarCliente(request, response);
                break;
            case "editar":
                formularioEditar(request, response);
                break;
            case "actualizar":
                actualizarCliente(request, response);
                break;
            case "eliminar":
                eliminarCliente(request, response);
                break;
            case "listar":
                listarClientes(request, response);
                break;
            default:
                listarClientes(request, response); 
        }
    }

    // --- Métodos de CRUD replicados de ServletUsuarios ---
    
    protected void agregarCliente(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        String nombre = solicitud.getParameter("nombreCliente");
        String apellido = solicitud.getParameter("apellidoCliente");
        String email = solicitud.getParameter("emailCliente");
        String telefono = solicitud.getParameter("telefonoCliente");
        String direccion = solicitud.getParameter("direccionCliente");
        String nit = solicitud.getParameter("nitCliente"); 
        
        Cliente nuevoCliente = new Cliente(nombre, apellido, email, telefono, direccion, nit);
        clienteDao.saveCliente(nuevoCliente);
        
        respuesta.sendRedirect("ClienteServlet?accion=listar");
    }
    
    protected void formularioEditar(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idEditar = Integer.parseInt(solicitud.getParameter("id"));
        Cliente cliente = clienteDao.getClienteById(idEditar);
        
        solicitud.setAttribute("cliente", cliente);
        solicitud.getRequestDispatcher("/pages/editarCliente.jsp").forward(solicitud, respuesta);
    }
    
    protected void actualizarCliente(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idActualizar = Integer.parseInt(solicitud.getParameter("idCliente"));
        String nombre = solicitud.getParameter("nombreCliente");
        String apellido = solicitud.getParameter("apellidoCliente");
        String email = solicitud.getParameter("emailCliente");
        String telefono = solicitud.getParameter("telefonoCliente");
        String direccion = solicitud.getParameter("direccionCliente");
        String nit = solicitud.getParameter("nitCliente");

        Cliente cliente = clienteDao.getClienteById(idActualizar);
        
        if (cliente != null) {
            cliente.setNombreCliente(nombre);
            cliente.setApellidoCliente(apellido);
            cliente.setEmailCliente(email);
            cliente.setTelefonoCliente(telefono);
            cliente.setDireccionCliente(direccion);
            cliente.setNitCliente(nit);
            
            clienteDao.updateCliente(cliente);
        }
        
        respuesta.sendRedirect("ClienteServlet?accion=listar");
    }
    
    protected void eliminarCliente(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idEliminar = Integer.parseInt(request.getParameter("id"));
        clienteDao.deleteCliente(idEliminar);
        response.sendRedirect("ClienteServlet?accion=listar");
    }
    
    protected void listarClientes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cliente> listaClientes = clienteDao.getAllClientes();
        
        request.setAttribute("listaClientes", listaClientes);
        
        request.getRequestDispatcher("/pages/administracionClientes.jsp").forward(request, response);
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
        return "Servlet para gestionar clientes";
    }
}