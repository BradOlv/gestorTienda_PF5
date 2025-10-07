<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Formulario de Usuario</title>
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
        <c:set var="titulo" value="${usuario.idUsuario > 0 ? 'Editar Usuario' : 'Agregar Nuevo Usuario'}"/>
        <c:set var="accion" value="${usuario.idUsuario > 0 ? 'actualizar' : 'agregar'}"/>

        <div class="titulo-bg">
            <div class="container d-flex justify-content-between align-items-center">
                   <h2 class="my-3 text-white" style="font-family: var(--font-georgia);">${titulo}</h2>
                 <a href="UsuarioServlet?accion=listar" class="btn btn-sm btn-outline-light" style="font-family: var(--font-georgia); border-radius: 8px;">Cancelar / Volver a Listado</a>
            </div>
        </div>

        <div class="container my-5">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card shadow-lg border-0">
                        <div class="card-body p-5">
                            
                            <form action="UsuarioServlet?accion=${accion}" method="POST">
                                
                                <%-- Campo ID (solo si estamos editando) --%>
                                <c:if test="${usuario.idUsuario > 0}">
                                    <div class="mb-3">
                                        <label for="idUsuario" class="form-label texto-principal">ID de Usuario</label>
                                        <input type="text" class="form-control form-control-custom" id="idUsuario" 
                                               name="idUsuario" value="${usuario.idUsuario}" readonly>
                                    </div>
                                </c:if>

                                <%-- Nombre --%>
                                <div class="mb-3">
                                    <label for="nombreUsuario" class="form-label texto-principal">Nombre</label>
                                    <input type="text" class="form-control form-control-custom" id="nombreUsuario" 
                                           name="nombreUsuario" value="${usuario.nombreUsuario}" required maxlength="50">
                                </div>

                                <%-- Apellido --%>
                                <div class="mb-3">
                                    <label for="apellidoUsuario" class="form-label texto-principal">Apellido</label>
                                    <input type="text" class="form-control form-control-custom" id="apellidoUsuario" 
                                           name="apellidoUsuario" value="${usuario.apellidoUsuario}" required maxlength="50">
                                </div>

                                <%-- Email --%>
                                <div class="mb-3">
                                    <label for="emailUsuario" class="form-label texto-principal">Email</label>
                                    <input type="email" class="form-control form-control-custom" id="emailUsuario" 
                                           name="emailUsuario" value="${usuario.emailUsuario}" required maxlength="100">
                                </div>

                                <%-- Teléfono --%>
                                <div class="mb-3">
                                    <label for="telefonoUsuario" class="form-label texto-principal">Teléfono</label>
                                    <input type="tel" class="form-control form-control-custom" id="telefonoUsuario" 
                                           name="telefonoUsuario" value="${usuario.telefonoUsuario}" maxlength="20">
                                </div>

                                <%-- Dirección --%>
                                <div class="mb-3">
                                    <label for="direccionUsuario" class="form-label texto-principal">Dirección</label>
                                    <input type="text" class="form-control form-control-custom" id="direccionUsuario" 
                                           name="direccionUsuario" value="${usuario.direccionUsuario}" maxlength="255">
                                </div>
                                
                                <%-- NIT (Campo Adicional) --%>
                                <div class="mb-3">
                                    <label for="nit" class="form-label texto-principal">NIT</label>
                                    <input type="text" class="form-control form-control-custom" id="nit" 
                                           name="nit" value="${usuario.nit}" required maxlength="64">
                                </div>

                                <%-- Rol (Campo Adicional - Select) --%>
                                <div class="mb-3">
                                    <label for="rol" class="form-label texto-principal">Rol</label>
                                    <select class="form-select form-control-custom" id="rol" name="rol" required>
                                        <option value="Cliente" ${usuario.rol == 'Cliente' ? 'selected' : ''}>Cliente</option>
                                        <option value="Admin" ${usuario.rol == 'Admin' ? 'selected' : ''}>Admin</option>
                                    </select>
                                </div>

                                <%-- Contraseña (Requerida al agregar, opcional al editar) --%>
                                <div class="mb-4">
                                    <label for="contrasena" class="form-label texto-principal">Contraseña 
                                        <c:if test="${usuario.idUsuario > 0}">(Dejar vacío para no cambiar)</c:if>
                                    </label>
                                    <input type="password" class="form-control form-control-custom" id="contrasena" 
                                           name="contrasena" <c:if test="${usuario.idUsuario == 0}">required</c:if> maxlength="255">
                                </div>

                                <div class="d-flex justify-content-end">
                                    <button type="submit" class="btn btn-custom-primary">
                                        <c:choose><c:when test="${usuario.idUsuario > 0}">Actualizar Usuario</c:when><c:otherwise>Guardar Usuario</c:otherwise></c:choose>
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