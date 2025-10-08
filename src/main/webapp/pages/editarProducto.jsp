<%-- 
    Document   : editarProducto
    Created on : 7/10/2025, 20:04:31
    Author     : Kevin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Formulario de Producto</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
        
        <style>
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
        
        <c:set var="titulo" value="${producto.idProducto > 0 ? 'Editar Producto' : 'Agregar Nuevo Producto'}"/>
        <c:set var="accion" value="${producto.idProducto > 0 ? 'actualizar' : 'agregar'}"/>

        <div class="titulo-bg">
            <div class="container d-flex justify-content-between align-items-center">
                   <h2 class="my-3 text-white" style="font-family: var(--font-georgia);">${titulo}</h2>
                 <a href="ProductoServlet?accion=listar" class="btn btn-sm btn-outline-light" style="font-family: var(--font-georgia); border-radius: 8px;">Cancelar / Volver a Listado</a>
            </div>
        </div>

        <div class="container my-5">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card shadow-lg border-0">
                        <div class="card-body p-5">
                            
                            <form action="ProductoServlet?accion=${accion}" method="POST">
                                
                                <c:if test="${producto.idProducto > 0}">
                                    <div class="mb-3">
                                        <label for="idProducto" class="form-label texto-principal">ID de Producto</label>
                                        <input type="text" class="form-control form-control-custom" id="idProducto" 
                                               name="idProducto" value="${producto.idProducto}" readonly>
                                    </div>
                                </c:if>

                                <div class="mb-3">
                                    <label for="nombreProducto" class="form-label texto-principal">Nombre del Producto</label>
                                    <input type="text" class="form-control form-control-custom" id="nombreProducto" 
                                           name="nombreProducto" value="${producto.nombreProducto}" required maxlength="255">
                                </div>

                                <div class="mb-3">
                                    <label for="descripcionProducto" class="form-label texto-principal">Descripción</label>
                                    <textarea class="form-control form-control-custom" id="descripcionProducto" 
                                              name="descripcionProducto" rows="3" required>${producto.descripcionProducto}</textarea>
                                </div>

                                <div class="mb-3">
                                    <label for="precio" class="form-label texto-principal">Precio</label>
                                    <input type="number" class="form-control form-control-custom" id="precio" 
                                           name="precio" value="${producto.precio}" required min="0.01" step="0.01">
                                </div>

                                <div class="mb-3">
                                    <label for="stock" class="form-label texto-principal">Stock</label>
                                    <input type="number" class="form-control form-control-custom" id="stock" 
                                           name="stock" value="${producto.stock}" required min="0" step="1">
                                </div>

                                <div class="mb-3">
                                    <label for="categoria" class="form-label texto-principal">Categoría</label>
                                    <input type="text" class="form-control form-control-custom" id="categoria" 
                                           name="categoria" value="${producto.categoria}" required maxlength="255">
                                </div>

                                <div class="mb-4">
                                    <label for="marca" class="form-label texto-principal">Marca</label>
                                    <input type="text" class="form-control form-control-custom" id="marca" 
                                           name="marca" value="${producto.marca}" required maxlength="255">
                                </div>

                                <div class="d-flex justify-content-end">
                                    <button type="submit" class="btn btn-custom-primary">
                                        <c:choose><c:when test="${producto.idProducto > 0}">Actualizar Producto</c:when><c:otherwise>Guardar Producto</c:otherwise></c:choose>
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
</html>v
