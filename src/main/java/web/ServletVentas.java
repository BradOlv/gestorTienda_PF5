package web;

import dao.VentasDAO;
import model.Venta;
import model.Cliente;
import model.Usuario; 
import dao.ClientesDAO; 
import dao.UsuariosDAO; 

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@WebServlet(name = "VentaServlet", urlPatterns = {"/VentaServlet"})
public class ServletVentas extends HttpServlet {

    private final VentasDAO ventaDAO = new VentasDAO();
    private final ClientesDAO clienteDAO = new ClientesDAO();
    private final UsuariosDAO usuarioDAO = new UsuariosDAO(); 

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; 
        }
        
        switch (accion) {
            case "agregar":
                agregarVenta(request, response);
                break;
            case "editar":
                formularioEditar(request, response);
                break;
            case "actualizar":
                actualizarVenta(request, response);
                break;
            case "eliminar":
                eliminarVenta(request, response);
                break;
            case "listar":
                listarVentas(request, response);
                break;
            case "formNuevo": // Para mostrar el formulario de agregar/crear
                mostrarFormularioNuevo(request, response);
                break;
            default:
                listarVentas(request, response); 
        }
    }

    // --- Métodos de CRUD ---
    
    protected void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cliente> clientes = clienteDAO.getAllClientes();
        List<Usuario> usuarios = usuarioDAO.getAllUsuarios(); 

        request.setAttribute("listaClientes", clientes);
        request.setAttribute("listaUsuarios", usuarios);
        
        request.getRequestDispatcher("/pages/editarVenta.jsp").forward(request, response);
    }
    
    protected void agregarVenta(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idCliente = Integer.parseInt(solicitud.getParameter("idCliente"));
        int idUsuario = Integer.parseInt(solicitud.getParameter("idUsuario"));
        BigDecimal totalVenta = new BigDecimal(solicitud.getParameter("totalVenta"));
        String estadoVenta = solicitud.getParameter("estadoVenta");
        
        Cliente cliente = ventaDAO.getClienteReference(idCliente);
        Usuario usuario = ventaDAO.getUsuarioReference(idUsuario);
        
        Venta nuevaVenta = new Venta(cliente, usuario, totalVenta, estadoVenta);
        ventaDAO.saveVenta(nuevaVenta);
        
        respuesta.sendRedirect("VentaServlet?accion=listar");
    }
    
    protected void formularioEditar(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idEditar = Integer.parseInt(solicitud.getParameter("id"));
        Venta venta = ventaDAO.getVentaById(idEditar);
        
        List<Cliente> clientes = clienteDAO.getAllClientes();
        List<Usuario> usuarios = usuarioDAO.getAllUsuarios(); 
        
        solicitud.setAttribute("venta", venta);
        solicitud.setAttribute("listaClientes", clientes);
        solicitud.setAttribute("listaUsuarios", usuarios);
        
        solicitud.getRequestDispatcher("/pages/editarVenta.jsp").forward(solicitud, respuesta);
    }
    
    protected void actualizarVenta(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idActualizar = Integer.parseInt(solicitud.getParameter("idVenta"));
        int idCliente = Integer.parseInt(solicitud.getParameter("idCliente"));
        int idUsuario = Integer.parseInt(solicitud.getParameter("idUsuario"));
        BigDecimal totalVenta = new BigDecimal(solicitud.getParameter("totalVenta"));
        String estadoVenta = solicitud.getParameter("estadoVenta");

        Venta venta = ventaDAO.getVentaById(idActualizar);
        
        if (venta != null) {
            Cliente cliente = ventaDAO.getClienteReference(idCliente);
            Usuario usuario = ventaDAO.getUsuarioReference(idUsuario);
            
            venta.setCliente(cliente);
            venta.setUsuario(usuario);
            venta.setTotalVenta(totalVenta);
            venta.setEstadoVenta(estadoVenta);

            ventaDAO.updateVenta(venta);
        }
        
        respuesta.sendRedirect("VentaServlet?accion=listar");
    }
    
    protected void eliminarVenta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idEliminar = Integer.parseInt(request.getParameter("id"));
        ventaDAO.deleteVenta(idEliminar);
        response.sendRedirect("VentaServlet?accion=listar");
    }
    
    protected void listarVentas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Venta> listaVentas = ventaDAO.getAllVentas();
        
        request.setAttribute("listaVentas", listaVentas);
        
        request.getRequestDispatcher("/pages/administracionVentas.jsp").forward(request, response);
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
        return "Servlet para gestionar ventas";
    }
}