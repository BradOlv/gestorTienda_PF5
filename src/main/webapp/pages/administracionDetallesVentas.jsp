<%-- 
    Document   : administracionDetallesVentas
    Created on : 7/10/2025, 17:55:34
    Author     : Kevin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Administración de Detalles de Ventas</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        
        <style>
            :root {
                --color-principal: rgb(48, 87, 118);
                --color-secundario: rgb(255, 170, 102);
                --color-fondo-claro: #f4f4f4;
                --color-fondo-alterno: #e3f2f9;
                --font-georgia: "Georgia", serif;
            }

            .titulo-bg {
                background-color: var(--color-principal);
                color: white;
                padding: 1rem 0;
            }
            .texto-principal {
                color: var(--color-principal);
                font-family: var(--font-georgia);
            }
            
            .btn-custom-primary {
                color: var(--color-principal);
                border-color: var(--color-principal);
                border-radius: 8px;
                background-color: transparent;
                font-weight: bold;
                padding: 5px 15px;
                font-family: var(--font-georgia);
                transition: all 0.2s;
            }
            .btn-custom-primary:hover {
                color: var(--color-principal);
                background-color: rgba(48, 87, 118, 0.1);
                border-color: var(--color-principal);
            }
            
            .table-custom {
                border-radius: 6px;
                overflow: hidden;
            }
            .table-custom thead th {
                background-color: var(--color-principal);
                color: white;
                border-color: transparent;
                font-weight: bold;
                padding: 10px;
                border-top: 2px solid var(--color-principal);
                border-bottom: 2px solid var(--color-principal);
                font-family: var(--font-georgia);
            }
            .table-custom td {
                 border: 1px solid #dee2e6;
            }
            .table-custom tbody tr:nth-child(even) {
                background-color: var(--color-fondo-claro);
            }
            .table-custom tbody tr:nth-child(odd) {
                background-color: var(--color-fondo-alterno);
            }
            .table-custom tbody tr:hover {
                background-color: rgba(255, 170, 102, 0.5) !important;
                cursor: pointer;
            }
        </style>
    </head>
    
     <body>
        <div class="titulo-bg">
            <div class="container d-flex justify-content-between align-items-center">
                <h2 class="my-3 text-white" style="font-family: var(--font-georgia);">Administración de Detalles de Ventas</h2>
                <a href="pages/admin-dashboard.jsp" class="btn btn-sm btn-outline-light" style="font-family: var(--font-georgia); border-radius: 8px;">Volver al Panel</a>
            </div>
        </div>

        <div class="container my-5">
            <div class="row mb-4">
                <div class="col-md-12">
                    <a href="DetalleVentaServlet?accion=formNuevo" class="btn btn-custom-primary">
                        Agregar Nuevo Detalle de Venta
                    </a>
                </div>
            </div>

            <div class="table-responsive">
                <table class="table table-custom align-middle">
                    <thead>
                        <tr>
                            <th>ID Detalle</th>
                            <th>ID Venta</th>
                            <th>Producto</th>
                            <th>Cantidad</th>
                            <th>Precio Unitario</th>
                            <th>Subtotal</th>
                            <th class="text-center">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="detalle" items="${listaDetallesVentas}">
                            <tr>
                                <td class="texto-principal">${detalle.idDetalleVenta}</td>
                                <td class="texto-principal">${detalle.venta.idVenta}</td>
                                <td class="texto-principal">${detalle.producto.nombreProducto}</td>
                                <td class="texto-principal">${detalle.cantidad}</td>
                                <td class="texto-principal">
                                    <fmt:formatNumber value="${detalle.precioUnitario}" type="currency" currencySymbol="Q"/>
                                </td>
                                <td class="texto-principal">
                                    <fmt:formatNumber value="${detalle.subtotal}" type="currency" currencySymbol="Q"/>
                                </td>
                                <td class="text-center">
                                    <a href="DetalleVentaServlet?accion=editar&id=${detalle.idDetalleVenta}" class="btn btn-custom-primary btn-sm">
                                        Editar
                                    </a>
                                    <a href="DetalleVentaServlet?accion=eliminar&id=${detalle.idDetalleVenta}" 
                                       onclick="return confirm('¿Está seguro de eliminar este detalle de venta?')" 
                                       class="btn btn-sm btn-danger">
                                        Eliminar
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <c:if test="${empty listaDetallesVentas}">
                <div class="alert alert-info mt-3" role="alert">
                    No hay detalles de ventas registrados en la base de datos.
                </div>
            </c:if>

        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    </body>
</html>
