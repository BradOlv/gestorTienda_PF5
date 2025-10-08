<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Administración de Ventas</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        
        <style>
            /* --- Colores y Fuentes --- */
            :root {
                --color-principal: rgb(48, 87, 118); /* Azul/Gris oscuro */
                --color-secundario: rgb(255, 170, 102); /* Naranja claro para inputs */
                --color-fondo-claro: #f4f4f4;
                --color-fondo-alterno: #e3f2f9;
                --font-georgia: "Georgia", serif;
            }

            /* --- Estilos Generales y Títulos (fondoTitulo) --- */
            .titulo-bg {
                background-color: var(--color-principal);
                color: white;
                padding: 1rem 0;
            }
            .texto-principal { /* (estiloTexto) */
                color: var(--color-principal);
                font-family: var(--font-georgia);
            }
            
            /* --- Botón Principal (estiloBoton) --- */
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
            
            /* --- Estilos de la Tabla (table-view) --- */
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
            .table-custom tbody tr:nth-child(even) { /* Filas pares */
                background-color: var(--color-fondo-claro);
            }
            .table-custom tbody tr:nth-child(odd) { /* Filas impares */
                background-color: var(--color-fondo-alterno);
            }
            .table-custom tbody tr:hover { /* Simulación de selección */
                background-color: rgba(255, 170, 102, 0.5) !important;
                cursor: pointer;
            }

            /* Estilos para los botones de acción específicos */
            .btn-accion-edit { background-color: #ffc107; color: #333; border-color: #ffc107; font-weight: 600; }
            .btn-accion-edit:hover { background-color: #e0a800; color: #333; }
            .btn-accion-delete { background-color: #dc3545; color: white; border-color: #dc3545; font-weight: 600; }
            .btn-accion-delete:hover { background-color: #c82333; color: white; }
            
            /* Estilos de estado de venta */
            .estado-pendiente { background-color: #ffc107; color: #333; font-weight: 600; padding: 4px 8px; border-radius: 4px; display: inline-block; }
            .estado-completada { background-color: #28a745; color: white; font-weight: 600; padding: 4px 8px; border-radius: 4px; display: inline-block; }
            .estado-cancelada { background-color: #dc3545; color: white; font-weight: 600; padding: 4px 8px; border-radius: 4px; display: inline-block; }
        </style>
    </head>
    
    <body>
        <div class="titulo-bg">
            <div class="container d-flex justify-content-between align-items-center">
                <h2 class="my-3 text-white" style="font-family: var(--font-georgia);">Administración de Ventas</h2>
                <%-- Volver al Menú Admin --%>
                <a href="pages/admin-dashboard.jsp" class="btn btn-sm btn-outline-light" style="font-family: var(--font-georgia); border-radius: 8px;">Volver al Panel</a>
            </div>
        </div>

        <div class="container my-5">
            <div class="row mb-4">
                <div class="col-md-12">
                    <%-- Enlace para ir al formulario de agregar venta (formNuevo) --%>
                    <a href="${pageContext.request.contextPath}/VentaServlet?accion=formNuevo" class="btn btn-custom-primary">
                        + Registrar Nueva Venta
                    </a>
                </div>
            </div>

            <%-- TABLA DE VENTAS --%>
            <div class="table-responsive">
                <table class="table table-custom align-middle">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Cliente</th>
                            <th>Vendedor</th>
                            <th>Fecha Venta</th>
                            <th>Total</th>
                            <th>Estado</th>
                            <th class="text-center">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%-- Itera sobre la lista de Ventas enviada desde ServletVentas --%>
                        <c:forEach var="venta" items="${listaVentas}">
                            <tr>
                                <td class="texto-principal">${venta.idVenta}</td>
                                <td class="texto-principal">${venta.cliente.nombreCliente} ${venta.cliente.apellidoCliente}</td>
                                <td class="texto-principal">${venta.usuario.nombreUsuario}</td>
                                
                                <%-- Formato de Fecha usando JSTL fmt --%>
                                <td class="texto-principal">
                                    <fmt:parseDate value="${venta.fechaVenta}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" type="both"/>
                                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm" />
                                </td>
                                
                                <%-- Formato de Moneda --%>
                                <td class="texto-principal">
                                    <fmt:formatNumber value="${venta.totalVenta}" type="currency" currencySymbol="Q" maxFractionDigits="2"/>
                                </td>
                                
                                <td>
                                    <%-- Determinar la clase de estilo según el estado --%>
                                    <c:choose>
                                        <c:when test="${venta.estadoVenta eq 'Completada'}">
                                            <span class="estado-completada">${venta.estadoVenta}</span>
                                        </c:when>
                                        <c:when test="${venta.estadoVenta eq 'Cancelada'}">
                                            <span class="estado-cancelada">${venta.estadoVenta}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="estado-pendiente">${venta.estadoVenta}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                
                                <td class="text-center">
                                    <%-- Botón Editar --%>
                                    <a href="${pageContext.request.contextPath}/VentaServlet?accion=editar&id=${venta.idVenta}" 
                                       class="btn btn-sm btn-accion-edit">
                                        Editar
                                    </a>
                                    <%-- Botón Eliminar con confirmación --%>
                                    <a href="${pageContext.request.contextPath}/VentaServlet?accion=eliminar&id=${venta.idVenta}" 
                                       onclick="return confirm('¿Está seguro de eliminar la Venta ID ${venta.idVenta}?')" 
                                       class="btn btn-sm btn-accion-delete">
                                        Eliminar
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <%-- Mensaje si no hay ventas --%>
            <c:if test="${empty listaVentas}">
                <div class="alert alert-info mt-3" role="alert">
                    No hay ventas registradas en la base de datos.
                </div>
            </c:if>

        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    </body>
</html>