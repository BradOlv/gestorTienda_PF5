<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Formulario de Cliente</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        
        <style>
            /* --- Colores y Fuentes --- */
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
            
            /* --- Inputs (estiloTF) --- */
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
        </style>
    </head>
    
    <body>
        
        <%-- Define variables JSP para el título y la acción del servlet --%>
        <c:set var="titulo" value="${cliente.idCliente > 0 ? 'Editar Cliente' : 'Agregar Nuevo Cliente'}"/>
        <c:set var="accion" value="${cliente.idCliente > 0 ? 'actualizar' : 'agregar'}"/>

        <div class="titulo-bg">
            <div class="container d-flex justify-content-between align-items-center">
                   <h2 class="my-3 text-white" style="font-family: var(--font-georgia);">${titulo}</h2>
                 <a href="ClienteServlet?accion=listar" class="btn btn-sm btn-outline-light" style="font-family: var(--font-georgia); border-radius: 8px;">Cancelar / Volver a Listado</a>
            </div>
        </div>

        <div class="container my-5">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card shadow-lg border-0">
                        <div class="card-body p-5">
                            
                            <form action="ClienteServlet?accion=${accion}" method="POST">
                                
                                <%-- Campo ID (solo si estamos editando) --%>
                                <c:if test="${cliente.idCliente > 0}">
                                    <div class="mb-3">
                                        <label for="idCliente" class="form-label texto-principal">ID de Cliente</label>
                                        <input type="text" class="form-control form-control-custom" id="idCliente" 
                                               name="idCliente" value="${cliente.idCliente}" readonly>
                                    </div>
                                </c:if>

                                <%-- Nombre --%>
                                <div class="mb-3">
                                    <label for="nombreCliente" class="form-label texto-principal">Nombre</label>
                                    <input type="text" class="form-control form-control-custom" id="nombreCliente" 
                                           name="nombreCliente" value="${cliente.nombreCliente}" required maxlength="100">
                                </div>

                                <%-- Apellido --%>
                                <div class="mb-3">
                                    <label for="apellidoCliente" class="form-label texto-principal">Apellido</label>
                                    <input type="text" class="form-control form-control-custom" id="apellidoCliente" 
                                           name="apellidoCliente" value="${cliente.apellidoCliente}" required maxlength="100">
                                </div>

                                <%-- Email --%>
                                <div class="mb-3">
                                    <label for="emailCliente" class="form-label texto-principal">Email</label>
                                    <input type="email" class="form-control form-control-custom" id="emailCliente" 
                                           name="emailCliente" value="${cliente.emailCliente}" maxlength="100">
                                </div>

                                <%-- Teléfono --%>
                                <div class="mb-3">
                                    <label for="telefonoCliente" class="form-label texto-principal">Teléfono</label>
                                    <input type="tel" class="form-control form-control-custom" id="telefonoCliente" 
                                           name="telefonoCliente" value="${cliente.telefonoCliente}" maxlength="20">
                                </div>

                                <%-- Dirección --%>
                                <div class="mb-3">
                                    <label for="direccionCliente" class="form-label texto-principal">Dirección</label>
                                    <input type="text" class="form-control form-control-custom" id="direccionCliente" 
                                           name="direccionCliente" value="${cliente.direccionCliente}" maxlength="255">
                                </div>
                                
                                <%-- NIT --%>
                                <div class="mb-4">
                                    <label for="nitCliente" class="form-label texto-principal">NIT</label>
                                    <input type="text" class="form-control form-control-custom" id="nitCliente" 
                                           name="nitCliente" value="${cliente.nitCliente}" maxlength="20">
                                </div>

                                <div class="d-flex justify-content-end">
                                    <button type="submit" class="btn btn-custom-primary">
                                        <c:choose><c:when test="${cliente.idCliente > 0}">Actualizar Cliente</c:when><c:otherwise>Guardar Cliente</c:otherwise></c:choose>
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