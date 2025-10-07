package web;

import dao.ProductosDAO;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Producto;

/**
 *
 * @author Kevin Velasquez
 */
@WebServlet(name = "ProductoServlet", urlPatterns = {"/ProductoServlet"})
public class ServletProductos extends HttpServlet {
    
    private final ProductosDAO productoDao = new ProductosDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar"; 
        }
        
        switch (accion) {
            case "agregar":
                agregarProducto(request, response);
                break;
            case "editar":
                formularioEditar(request, response);
                break;
            case "actualizar":
                actualizarProducto(request, response);
                break;
            case "eliminar":
                eliminarProducto(request, response);
                break;
            case "listar":
                listarProductos(request, response);
                break;
            default:
                listarProductos(request, response); 
        }
    }

    // --- Métodos de CRUD ---
    
    protected void agregarProducto(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        String nombre = solicitud.getParameter("nombreProducto");
        String descripcion = solicitud.getParameter("descripcionProducto");
        double precio = Double.parseDouble(solicitud.getParameter("precio"));
        int stock = Integer.parseInt(solicitud.getParameter("stock"));
        String categoria = solicitud.getParameter("categoria");
        String marca = solicitud.getParameter("marca");
        
        Producto nuevoProducto = new Producto(nombre, descripcion, precio, stock, categoria, marca, Timestamp.from(Instant.now()));
        productoDao.saveProducto(nuevoProducto);
        
        respuesta.sendRedirect("ProductoServlet?accion=listar");
    }
    
    protected void formularioEditar(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idEditar = Integer.parseInt(solicitud.getParameter("id"));
        Producto producto = productoDao.getProductoById(idEditar);
        
        solicitud.setAttribute("producto", producto);
        solicitud.getRequestDispatcher("/pages/editarProducto.jsp").forward(solicitud, respuesta);
    }
    
    protected void actualizarProducto(HttpServletRequest solicitud, HttpServletResponse respuesta) throws ServletException, IOException {
        int idActualizar = Integer.parseInt(solicitud.getParameter("idProducto"));
        String nombre = solicitud.getParameter("nombreProducto");
        String descripcion = solicitud.getParameter("descripcionProducto");
        double precio = Double.parseDouble(solicitud.getParameter("precio"));
        int stock = Integer.parseInt(solicitud.getParameter("stock"));
        String categoria = solicitud.getParameter("categoria");
        String marca = solicitud.getParameter("marca");

        Producto producto = productoDao.getProductoById(idActualizar);
        
        if (producto != null) {
            producto.setNombreProducto(nombre);
            producto.setDescripcionProducto(descripcion);
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setCategoria(categoria);
            producto.setMarca(marca);
            
            productoDao.updateProducto(producto);
        }
        
        respuesta.sendRedirect("ProductoServlet?accion=listar");
    }
    
    protected void eliminarProducto(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idEliminar = Integer.parseInt(request.getParameter("id"));
        productoDao.deleteProducto(idEliminar);
        response.sendRedirect("ProductoServlet?accion=listar");
    }
    
    protected void listarProductos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Producto> listaProductos = productoDao.getAllProductos();
        
        request.setAttribute("listaProductos", listaProductos);
        
        request.getRequestDispatcher("/pages/administracionProductos.jsp").forward(request, response);
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
        return "Servlet para gestionar productos";
    }
}