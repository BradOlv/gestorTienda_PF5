package web;

import dao.DetalleVentaDAO;
import model.DetalleVenta;
import model.Venta;
import model.Producto; 
import dao.VentasDAO; 
import dao.ProductosDAO; 

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Kevin Velasquez
 */
@WebServlet(name = "DetalleVentaServlet", urlPatterns = {"/DetalleVentaServlet"})
public class ServletDetallesVentas extends HttpServlet {

    private final DetalleVentaDAO detalleVentaDAO = new DetalleVentaDAO();
    private final VentasDAO ventaDAO = new VentasDAO();
    private final ProductosDAO productoDAO = new ProductosDAO(); 

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; 
        }
        
        switch (accion) {
            case "agregar":
                agregarDetalleVenta(request, response);
                break;
            case "editar":
                formularioEditar(request, response);
                break;
            case "actualizar":
                actualizarDetalleVenta(request, response);
                break;
            case "eliminar":
                eliminarDetalleVenta(request, response);
                break;
            case "listar":
                listarDetallesVentas(request, response);
                break;
            case "formNuevo":
                mostrarFormularioNuevo(request, response);
                break;
            case "listarPorVenta":
                listarDetallesPorVenta(request, response);
                break;
            default:
                listarDetallesVentas(request, response); 
        }
    }

    protected void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Venta> ventas = ventaDAO.getAllVentas();
        List<Producto> productos = productoDAO.getAllProductos(); 

        request.setAttribute("listaVentas", ventas);
        request.setAttribute("listaProductos", productos);
        
        request.getRequestDispatcher("/pages/agregarDetalleVenta.jsp").forward(request, response);
    }
    
    protected void agregarDetalleVenta(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idVenta = Integer.parseInt(solicitud.getParameter("idVenta"));
        int idProducto = Integer.parseInt(solicitud.getParameter("idProducto"));
        int cantidad = Integer.parseInt(solicitud.getParameter("cantidad"));
        BigDecimal precioUnitario = new BigDecimal(solicitud.getParameter("precioUnitario"));
        
        Venta venta = detalleVentaDAO.getVentaReference(idVenta);
        Producto producto = detalleVentaDAO.getProductoReference(idProducto);
        
        DetalleVenta nuevoDetalleVenta = new DetalleVenta(venta, producto, cantidad, precioUnitario);
        detalleVentaDAO.saveDetalleVenta(nuevoDetalleVenta);
        
        respuesta.sendRedirect("DetalleVentaServlet?accion=listar");
    }
    
    protected void formularioEditar(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idEditar = Integer.parseInt(solicitud.getParameter("id"));
        DetalleVenta detalleVenta = detalleVentaDAO.getDetalleVentaById(idEditar);
        
        List<Venta> ventas = ventaDAO.getAllVentas();
        List<Producto> productos = productoDAO.getAllProductos(); 
        
        solicitud.setAttribute("detalleVenta", detalleVenta);
        solicitud.setAttribute("listaVentas", ventas);
        solicitud.setAttribute("listaProductos", productos);
        
        solicitud.getRequestDispatcher("/pages/editarDetalleVenta.jsp").forward(solicitud, respuesta);
    }
    
    protected void actualizarDetalleVenta(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idActualizar = Integer.parseInt(solicitud.getParameter("idDetalleVenta"));
        int idVenta = Integer.parseInt(solicitud.getParameter("idVenta"));
        int idProducto = Integer.parseInt(solicitud.getParameter("idProducto"));
        int cantidad = Integer.parseInt(solicitud.getParameter("cantidad"));
        BigDecimal precioUnitario = new BigDecimal(solicitud.getParameter("precioUnitario"));

        DetalleVenta detalleVenta = detalleVentaDAO.getDetalleVentaById(idActualizar);
        
        if (detalleVenta != null) {
            Venta venta = detalleVentaDAO.getVentaReference(idVenta);
            Producto producto = detalleVentaDAO.getProductoReference(idProducto);
            
            detalleVenta.setVenta(venta);
            detalleVenta.setProducto(producto);
            detalleVenta.setCantidad(cantidad);
            detalleVenta.setPrecioUnitario(precioUnitario);

            detalleVentaDAO.updateDetalleVenta(detalleVenta);
        }
        
        respuesta.sendRedirect("DetalleVentaServlet?accion=listar");
    }
    
    protected void eliminarDetalleVenta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idEliminar = Integer.parseInt(request.getParameter("id"));
        detalleVentaDAO.deleteDetalleVenta(idEliminar);
        response.sendRedirect("DetalleVentaServlet?accion=listar");
    }
    
    protected void listarDetallesVentas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<DetalleVenta> listaDetallesVentas = detalleVentaDAO.getAllDetalleVentas();
        
        request.setAttribute("listaDetallesVentas", listaDetallesVentas);
        
        request.getRequestDispatcher("/pages/administracionDetallesVentas.jsp").forward(request, response);
    }
    
    protected void listarDetallesPorVenta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idVenta = Integer.parseInt(request.getParameter("idVenta"));
        List<DetalleVenta> listaDetallesVentas = detalleVentaDAO.getDetallesByVenta(idVenta);
        
        Venta venta = ventaDAO.getVentaById(idVenta);
        
        request.setAttribute("listaDetallesVentas", listaDetallesVentas);
        request.setAttribute("venta", venta);
        
        request.getRequestDispatcher("/pages/detallesVentaPorVenta.jsp").forward(request, response);
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
        return "Servlet para gestionar detalles de ventas";
    }
}