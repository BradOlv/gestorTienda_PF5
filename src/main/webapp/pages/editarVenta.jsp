<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Formulario de Ventas</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        
        <style>
            /* (Estilos omitidos por brevedad, se asume que los estilos CSS son funcionales) */
            :root {
                --color-principal: rgb(48, 87, 118); 
                --color-secundario: rgb(255, 170, 102); 
                --color-secundario-foco: rgb(255, 140, 70); 
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
                color: white; 
                border-color: var(--color-principal);
                background-color: var(--color-principal); 
                border-radius: 8px;
                font-weight: bold;
                padding: 8px 20px;
                font-family: var(--font-georgia);
                transition: all 0.2s;
            }
            .btn-custom-primary:hover {
                color: white;
                background-color: rgb(30, 60, 90);
                border-color: rgb(30, 60, 90);
            }
            
            .form-control-custom {
                border-color: var(--color-secundario) !important;
                border-width: 2px !important;
                border-radius: 6px !important;
                padding: 4px;
                font-family: var(--font-georgia);
            }
            .form-control-custom:focus {
                border-color: var(--color-secundario-foco) !important;
                box-shadow: 0 0 0 0.25rem rgba(255, 140, 70, 0.25) !important;
                background-color: #fffaf5;
            }
            .btn-cancel {
                color: var(--color-principal); 
                border-color: var(--color-principal); 
            }
        </style>
    </head>
    
    <body>
        
        <%-- Define JSP variables for the title and servlet action --%>
        <c:set var="title" value="${venta.idVenta > 0 ? 'Editar Venta' : 'Registrar nueva venta'}"/>
        <c:set var="action" value="${venta.idVenta > 0 ? 'actualizar' : 'agregar'}"/>

        <div class="titulo-bg">
            <div class="container d-flex justify-content-between align-items-center">
                   <h2 class="my-3 text-white" style="font-family: var(--font-georgia);">${title}</h2>
                   <%-- TRADUCCIÓN: Cancel / Back to List -> Cancelar / Volver a la Lista --%>
                   <a href="${pageContext.request.contextPath}/VentaServlet?action=list" class="btn btn-sm btn-outline-light" style="font-family: var(--font-georgia); border-radius: 8px;">Cancelar / Volver a la Lista</a>
            </div>
        </div>

        <div class="container my-5">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card shadow-lg border-0">
                        <div class="card-body p-5">
                            
                            <%-- The action goes to the VentaServlet with the defined action (add or update) --%>
                            <form action="${pageContext.request.contextPath}/VentaServlet?accion=${action}" method="POST">
                                
                                <%-- Sale ID Field (only if editing) --%>
                                <c:if test="${venta.idVenta > 0}">
                                    <div class="mb-3">
                                        <%-- TRADUCCIÓN: Sale ID -> ID de Venta --%>
                                        <label for="idVenta" class="form-label texto-principal">ID de Venta</label>
                                        <input type="text" class="form-control form-control-custom" id="idVenta" 
                                               name="idVenta" value="${venta.idVenta}" readonly>
                                    </div>
                                </c:if>

                                <%-- Client Field (Select) --%>
                                <div class="mb-3">
                                    <%-- TRADUCCIÓN: Client -> Cliente --%>
                                    <label for="idCliente" class="form-label texto-principal">Cliente</label>
                                    <select id="idCliente" name="idCliente" class="form-select form-control-custom" required>
                                        <%-- TRADUCCIÓN: -- Select Client -- -> -- Seleccionar Cliente -- --%>
                                        <option value="">-- Seleccionar Cliente --</option>
                                        <c:forEach var="cliente" items="${listaClientes}">
                                            <option value="${cliente.idCliente}" 
                                                        <c:if test="${venta.cliente.idCliente == cliente.idCliente}">selected</c:if>>
                                                ${cliente.nombreCliente} ${cliente.apellidoCliente} (ID: ${cliente.idCliente})
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <%-- User/Seller Field (Select) --%>
                                <div class="mb-3">
                                    <%-- TRADUCCIÓN: Seller (User) -> Vendedor (Usuario) --%>
                                    <label for="idUsuario" class="form-label texto-principal">Vendedor (Usuario)</label>
                                    <select id="idUsuario" name="idUsuario" class="form-select form-control-custom" required>
                                        <%-- TRADUCCIÓN: -- Select Seller -- -> -- Seleccionar Vendedor -- --%>
                                        <option value="">-- Seleccionar Vendedor --</option>
                                        <c:forEach var="usuario" items="${listaUsuarios}">
                                            <option value="${usuario.idUsuario}" 
                                                        <c:if test="${venta.usuario.idUsuario == usuario.idUsuario}">selected</c:if>>
                                                ${usuario.nombreUsuario} (ID: ${usuario.idUsuario})
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <%-- Sale Total Field --%>
                                <div class="mb-3">
                                    <%-- TRADUCCIÓN: Sale Total -> Total de Venta --%>
                                    <label for="totalVenta" class="form-label texto-principal">Total de Venta</label>
                                    <input type="number" step="0.01" min="0" class="form-control form-control-custom" id="totalVenta" 
                                           name="totalVenta" value="${venta.totalVenta}" required>
                                </div>

                                <%-- Sale Status Field (Select) --%>
                                <div class="mb-4">
                                    <%-- TRADUCCIÓN: Sale Status -> Estado de Venta --%>
                                    <label for="estadoVenta" class="form-label texto-principal">Estado de Venta</label>
                                    <select id="estadoVenta" name="estadoVenta" class="form-select form-control-custom" required>
                                        <%-- TRADUCCIÓN: Pending, Completed, Cancelled -> Pendiente, Completada, Cancelada --%>
                                        <option value="Pendiente" <c:if test="${venta.estadoVenta eq 'Pendiente'}">selected</c:if>>Pendiente</option>
                                        <option value="Completada" <c:if test="${venta.estadoVenta eq 'Completada'}">selected</c:if>>Completada</option>
                                        <option value="Cancelada" <c:if test="${venta.estadoVenta eq 'Cancelada'}">selected</c:if>>Cancelada</option>
                                    </select>
                                </div>

                                <div class="d-flex justify-content-end">
                                    <button type="submit" class="btn btn-custom-primary">
                                        <c:choose>
                                            <c:when test="${venta.idVenta > 0}">Actualizar Venta</c:when>
                                            <c:otherwise>Guardar Venta</c:otherwise>
                                        </c:choose>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    </body>
</html>