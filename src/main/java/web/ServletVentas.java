package web;

import dao.VentasDAO;
import model.Venta;
import model.Cliente;
import model.Usuario;
import model.Producto;
import model.DetalleVenta;
import dao.ClientesDAO;
import dao.UsuariosDAO; 
import dao.ProductosDAO;
import model.RolUsuario;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "VentaServlet", urlPatterns = {"/VentaServlet"})
public class ServletVentas extends HttpServlet {

    private final VentasDAO ventaDAO = new VentasDAO();
    private final ClientesDAO clienteDAO = new ClientesDAO();
    private final UsuariosDAO usuarioDAO = new UsuariosDAO(); 
    private final ProductosDAO productoDAO = new ProductosDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLoggeado = (session != null) ? (Usuario) session.getAttribute("usuarioLoggeado") : null;
        
        // 🚀 CORRECCIÓN CLAVE: Proteger el Servlet. Si no hay usuario loggeado, se redirige.
        if (usuarioLoggeado == null) {
            response.sendRedirect(request.getContextPath() + "/pages/admin-login.jsp?error=SesionExpirada");
            return;
        }
        
        request.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "formVenta":
                mostrarFormularioVenta(request, response);
                break;
            case "crearVenta":
                crearVentaCompleta(request, response);
                break;
            case "mostrarFactura":
                mostrarFactura(request, response);
                break;
            case "listar":
                listarVentas(request, response);
                break;
            default:
                listarVentas(request, response);
        }
    }
    
    protected void mostrarFormularioVenta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cliente> clientes = clienteDAO.getAllClientes();
        List<Producto> productos = productoDAO.getAllProductos();
        
        request.setAttribute("listaClientes", clientes);
        request.setAttribute("listaProductos", productos);
        
        // Se usa forward para mantener cualquier atributo de error establecido (como mensajeError o error de URL)
        request.getRequestDispatcher("/pages/administracionCompra.jsp").forward(request, response);
    }
    

    protected void crearVentaCompleta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String contextPath = request.getContextPath(); // Capturamos el contexto
        
        // El usuarioLoggeado ya se validó en processRequest, pero lo recuperamos aquí:
        Usuario usuarioLoggeado = (Usuario) session.getAttribute("usuarioLoggeado");
        
        if (usuarioLoggeado == null) {
              response.sendRedirect(contextPath + "/pages/admin-login.jsp?error=SesionExpirada");
              return;
        }
        
        String idClienteStr = request.getParameter("idCliente");
        
        if (idClienteStr == null || idClienteStr.isEmpty()) {
            request.setAttribute("mensajeError", "Debe seleccionar un cliente.");
            mostrarFormularioVenta(request, response);
            return;
        }
        
        // 1. OBTENEMOS LOS DATOS DEL FORMULARIO POST
        String[] idsProducto = request.getParameterValues("idProducto");
        String[] cantidades = request.getParameterValues("cantidad");
        String[] precios = request.getParameterValues("precioUnitario");

        // === INICIO DE CORRECCIÓN: VALIDACIÓN DE PRODUCTOS VACÍOS ===
        if (idsProducto == null || idsProducto.length == 0) {
            // Usamos sendRedirect para recargar la página y añadir un parámetro de error
            response.sendRedirect(contextPath + "/VentaServlet?accion=formVenta&error=no_productos");
            return;
        }
        // === FIN DE CORRECCIÓN ===

        // 2. Obtener referencias completas desde la DB (SOLO Cliente)
        int idCliente = Integer.parseInt(idClienteStr);
        Cliente cliente = clienteDAO.getClienteById(idCliente);  
        
        if (cliente == null) {
              request.setAttribute("mensajeError", "Cliente no encontrado en la base de datos.");
              mostrarFormularioVenta(request, response);
              return;
        }
        
        BigDecimal totalVenta = BigDecimal.ZERO;
        List<DetalleVenta> detalles = new ArrayList<>();
        
        try {
            for (int i = 0; i < idsProducto.length; i++) {
                int idProd = Integer.parseInt(idsProducto[i]);
                int cant = Integer.parseInt(cantidades[i]);
                BigDecimal precioUnitario = new BigDecimal(precios[i]);
                
                // Usar ProductoDAO para obtener la referencia completa del Producto
                Producto productoRef = productoDAO.getProductoById(idProd);  
                
                if (productoRef == null) {
                    throw new RuntimeException("Producto con ID " + idProd + " no encontrado.");
                }

                DetalleVenta detalle = new DetalleVenta(null, productoRef, cant, precioUnitario);
                detalles.add(detalle);
                
                totalVenta = totalVenta.add(detalle.getSubtotal());
            }

            // 3. Crear Venta con las referencias correctas
            Venta nuevaVenta = new Venta(cliente, usuarioLoggeado, totalVenta, "Completada");
            
            Venta ventaGuardada = ventaDAO.guardarVentaCompleta(nuevaVenta, detalles);
            
            request.setAttribute("idVenta", ventaGuardada.getIdVenta());
            
            request.getRequestDispatcher("/pages/redirectFactura.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al procesar la compra. Revise el stock o datos ingresados: " + e.getMessage());
            e.printStackTrace();
            mostrarFormularioVenta(request, response);
        }
    }
    
    // ... (El resto de métodos: mostrarFactura, listarVentas, doGet, doPost, getServletInfo se mantienen sin cambios) ...
    protected void mostrarFactura(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idVentaStr = request.getParameter("idVenta");
        if (idVentaStr == null || idVentaStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de Venta es requerido.");
            return;
        }
        
        int idVenta = Integer.parseInt(idVentaStr);
        
        Venta venta = ventaDAO.getVentaById(idVenta);
        List<DetalleVenta> detalles = ventaDAO.getDetallesByVentaId(idVenta);  
        
        if (venta == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Venta no encontrada.");
            return;
        }
        
        request.setAttribute("venta", venta);
        request.setAttribute("detalles", detalles);
        
        request.getRequestDispatcher("/pages/facturaEmergente.jsp").forward(request, response);
    }
    
    protected void listarVentas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Venta> listaVentas = ventaDAO.getAllVentas();  
            
            request.setAttribute("listaVentas", listaVentas);
            
            request.getRequestDispatcher("/pages/administracionVentas.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al listar ventas: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin-dashboard.jsp"); 
        }
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