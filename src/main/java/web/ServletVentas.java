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
    private final ProductosDAO productoDAO = new ProductosDAO(); 

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
    
    // ***************************************************************
    // --- LÓGICA DE COMPRA Y FACTURACIÓN ---
    // ***************************************************************

    protected void mostrarFormularioVenta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Carga la lista de clientes y productos
        List<Cliente> clientes = clienteDAO.getAllClientes();
        List<Producto> productos = productoDAO.getAllProductos();
        
        // --- LÍNEAS DE DEPURACIÓN ---
        System.out.println("DEBUG: Clientes cargados: " + clientes.size()); 
        System.out.println("DEBUG: Productos cargados: " + productos.size()); 
        // --- FIN DEPURACIÓN ---

        request.setAttribute("listaClientes", clientes);
        request.setAttribute("listaProductos", productos);
        
        if (clientes.isEmpty() || productos.isEmpty()) {
            // Manejo de error si no hay datos
        }
        
        // RUTA CORRECTA: /pages/administracionCompra.jsp
        request.getRequestDispatcher("/pages/administracionCompra.jsp").forward(request, response);
    } 
    
    protected void crearVentaCompleta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        Usuario usuarioLoggeado = (Usuario) session.getAttribute("usuarioLoggeado"); 
        
        if (usuarioLoggeado == null) {
            response.sendRedirect("pages/login.jsp?error=SesionExpirada");
            return;
        }
        
        String idClienteStr = request.getParameter("idCliente");
        
        if (idClienteStr == null || idClienteStr.isEmpty()) {
            request.setAttribute("mensajeError", "Debe seleccionar un cliente.");
            mostrarFormularioVenta(request, response);
            return;
        }
        int idCliente = Integer.parseInt(idClienteStr);
        int idUsuario = usuarioLoggeado.getIdUsuario(); 

        String[] idsProducto = request.getParameterValues("idProducto");
        String[] cantidades = request.getParameterValues("cantidad");
        String[] precios = request.getParameterValues("precioUnitario");

        if (idsProducto == null || idsProducto.length == 0) {
            request.setAttribute("mensajeError", "Debe agregar productos a la compra.");
            mostrarFormularioVenta(request, response); 
            return;
        }

        Cliente cliente = ventaDAO.getClienteReference(idCliente);
        Usuario usuario = ventaDAO.getUsuarioReference(idUsuario);
        
        BigDecimal totalVenta = BigDecimal.ZERO;
        List<DetalleVenta> detalles = new ArrayList<>();
        
        try {
            // Construir los objetos DetalleVenta
            for (int i = 0; i < idsProducto.length; i++) {
                int idProd = Integer.parseInt(idsProducto[i]);
                int cant = Integer.parseInt(cantidades[i]);
                BigDecimal precioUnitario = new BigDecimal(precios[i]);
                
                Producto productoRef = productoDAO.getProductoById(idProd);
                if (productoRef == null) continue;

                // Cálculo del subtotal y acumulación del totalVenta (asumiendo método getSubtotal en DetalleVenta)
                DetalleVenta detalle = new DetalleVenta(null, productoRef, cant, precioUnitario);
                detalles.add(detalle);
                // Nota: Asumiendo que DetalleVenta tiene un método getSubtotal
                totalVenta = totalVenta.add(detalle.getSubtotal());
            }

            Venta nuevaVenta = new Venta(cliente, usuario, totalVenta, "Completada");
            
            // Guardar Venta y Detalles (Lógica Transaccional en DAO)
            Venta ventaGuardada = ventaDAO.guardarVentaCompleta(nuevaVenta, detalles);
            
            // --- INICIO DE LA CORRECCIÓN DE RUTA ---
            request.setAttribute("idVenta", ventaGuardada.getIdVenta());
            
            // *** CORRECCIÓN: Se añade '/pages/' ***
            request.getRequestDispatcher("/pages/redirectFactura.jsp").forward(request, response);
            // --- FIN DE LA CORRECCIÓN DE RUTA ---

        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al procesar la compra: " + e.getMessage());
            e.printStackTrace();
            mostrarFormularioVenta(request, response);
        }
    }
    
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
        
        // *** CORRECCIÓN: Se añade '/pages/' ***
        request.getRequestDispatcher("/pages/facturaEmergente.jsp").forward(request, response);
    }
    
    // ***************************************************************
    // --- MÉTODO CRUD (Mínimo necesario para que compile) ---
    // ***************************************************************
    
    protected void listarVentas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirige al formulario de venta por defecto si no tienes una página de listado
        mostrarFormularioVenta(request, response); 
    }

    // ***************************************************************
    // --- MÉTODOS DOGET/DOPOST ---
    // ***************************************************************

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