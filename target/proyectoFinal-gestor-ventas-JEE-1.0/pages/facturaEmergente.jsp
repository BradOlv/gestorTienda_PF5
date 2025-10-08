<%-- /pages/facturaEmergente.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="model.Venta" %>
<%@page import="model.DetalleVenta" %>
<%@page import="java.util.List" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Factura #${venta.idVenta}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        /* Variables de Estilo del Formulario Existente */
        :root {
            --color-principal: rgb(48, 87, 118); 
            --color-secundario: rgb(255, 170, 102); 
            --font-georgia: "Georgia", serif;
            --font-montserrat: 'Montserrat', Arial, sans-serif;
        }

        @media print {
            .no-print { display: none; }
        }
        
        body { 
            background-color: #f8f9fa;
            font-family: var(--font-montserrat); /* Usamos Montserrat para el cuerpo */
        }
        
        .invoice-box {
            max-width: 800px;
            margin: auto;
            padding: 30px;
            border: 1px solid #eee;
            box-shadow: 0 0 15px rgba(0, 0, 0, .1);
            line-height: 24px;
            color: #555;
            background: #fff;
        }
        
        .header-factura h1 {
            color: var(--color-principal);
            font-family: var(--font-montserrat);
            font-weight: 800;
        }

        .info-label {
            font-family: var(--font-georgia);
            font-weight: bold;
            color: #343a40; /* Gris oscuro para las etiquetas */
        }
        
        /* Estilos de tabla adaptados */
        .table-custom-header {
             background-color: var(--color-principal) !important;
             color: white;
             font-family: var(--font-georgia);
        }
        .table-custom-total {
             background-color: #e9ecef; /* Gris claro */
             font-family: var(--font-georgia);
             color: var(--color-principal);
             border-top: 2px solid var(--color-principal);
        }
    </style>
</head>
<body>

    <div class="invoice-box mt-5">
        <div class="row mb-4">
            <div class="col-6">
                <h1 class="header-factura">Relojería Bradley</h1>
            </div>
            <div class="col-6 text-end">
                <span class="info-label">Factura #:</span> <c:out value="${venta.idVenta}"/><br>
                <span class="info-label">Fecha:</span> <fmt:formatDate value="${venta.fechaVenta}" pattern="yyyy-MM-dd HH:mm"/>
            </div>
        </div>

        <hr>

        <div class="row mb-4">
            <div class="col-6">
                <span class="info-label">Información del Vendedor:</span><br>
                Usuario: <c:out value="${venta.usuario.nombreUsuario}"/> <br>
                ID: <c:out value="${venta.usuario.idUsuario}"/>
            </div>
            <div class="col-6 text-end">
                <span class="info-label">Información del Cliente:</span><br>
                <c:out value="${venta.cliente.nombreCliente}"/> <c:out value="${venta.cliente.apellidoCliente}"/><br>
                NIT: <c:out value="${venta.cliente.nitCliente}"/><br>
                <c:if test="${not empty venta.cliente.direccionCliente}">
                    Dirección: <c:out value="${venta.cliente.direccionCliente}"/>
                </c:if>
            </div>
        </div>
        
        <h4 class="mb-3" style="color: var(--color-principal); font-family: var(--font-georgia);">Detalle de Productos</h4>
        <table class="table table-bordered table-sm">
            <thead>
                <tr class="table-custom-header">
                    <th>Producto</th>
                    <th class="text-center">Cantidad</th>
                    <th class="text-end">Precio Unitario</th>
                    <th class="text-end">Subtotal</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="detalle" items="${detalles}">
                    <tr>
                        <td><c:out value="${detalle.producto.nombreProducto}"/></td>
                        <td class="text-center"><c:out value="${detalle.cantidad}"/></td>
                        <td class="text-end">
                            <fmt:formatNumber value="${detalle.precioUnitario}" type="currency" currencySymbol="Q" minFractionDigits="2"/>
                        </td>
                        <td class="text-end fw-bold">
                            <fmt:formatNumber value="${detalle.subtotal}" type="currency" currencySymbol="Q" minFractionDigits="2"/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot>
                <tr class="table-custom-total fw-bold">
                    <td colspan="3" class="text-end">TOTAL DE VENTA</td>
                    <td class="text-end">
                        <fmt:formatNumber value="${venta.totalVenta}" type="currency" currencySymbol="Q" minFractionDigits="2"/>
                    </td>
                </tr>
            </tfoot>
        </table>

        <div class="text-center mt-5 no-print">
            <button onclick="window.print()" class="btn btn-secondary me-2" style="background-color: var(--color-principal); border-color: var(--color-principal);">
                Imprimir Factura
            </button>
            <button onclick="window.close()" class="btn btn-outline-secondary">Cerrar</button>
        </div>
        
        <div class="text-center text-muted mt-3" style="font-family: var(--font-georgia);">
            Gracias por su compra.
        </div>
    </div>
</body>
</html>