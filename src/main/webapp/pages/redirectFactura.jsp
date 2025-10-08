<%-- 
    Document   : redirectFactura
    Created on : 7/10/2025, 22:46:14
    Author     : Bradley Oliva
--%>

<%-- /pages/redirectFactura.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Recupera el ID de Venta que fue pasado como atributo desde el Servlet
    Integer idVenta = (Integer) request.getAttribute("idVenta");
    
    // Si no hay ID de Venta (error), redirige al formulario
    if (idVenta == null) {
        response.sendRedirect("VentaServlet?accion=formVenta");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Procesando Venta...</title>
</head>
<body>
    <script type="text/javascript">
        // 1. Abre la factura en una nueva pestaña
        window.open('VentaServlet?accion=mostrarFactura&idVenta=<%= idVenta %>', '_blank');
        
        // 2. CORRECCIÓN: Redirige la ventana principal a la lista de ventas.
        // Esto evita que la página se quede en el formulario de compra vacío.
        window.location.href='VentaServlet?accion=listar'; 
    </script>
    <p>La factura se está abriendo en una nueva ventana. Redirigiendo...</p>
</body>
</html>