<%-- 
    Document   : editarDetalleVenta
    Created on : 7/10/2025, 17:57:32
    Author     : Kevin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Editar Detalle de Venta</title>
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
            
            .form-control-custom, .form-select-custom {
                border-color: var(--color-secundario) !important;
                border-width: 2px !important;
                border-radius: 6px !important;
                padding: 4px;
                font-family: var(--font-georgia);
            }
            .form-control-custom:focus, .form-select-custom:focus {
                border-color: var(--color-secundario-foco) !important;
                box-shadow: 0 0 0 0.25rem rgba(255, 140, 70, 0.25) !important;
                background-color: #fffaf5;
            }
        </style>
    </head>
    
    <body>
        <div class="titulo-bg">
            <div class="container d-flex justify-content-between align-items-center">
                <h2 class="my-3 text-white" style="font-family: var(--font-georgia);">Editar Detalle de Venta</h2>
                <a href="DetalleVentaServlet?accion=listar" class="btn btn-sm btn-outline-light" style="font-family: var(--font-georgia); border-radius: 8px;">Cancelar / Volver a Listado</a>
            </div>
        </div>

        <div class="container my-5">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card shadow-lg border-0">
                        <div class="card-body p-5">
                            
                            <form action="DetalleVentaServlet?accion=actualizar" method="POST">
                                
                                <div class="mb-3">
                                    <label for="idDetalleVenta" class="form-label texto-principal">ID de Detalle</label>
                                    <input type="text" class="form-control form-control-custom" id="idDetalleVenta" 
                                           name="idDetalleVenta" value="${detalleVenta.idDetalleVenta}" readonly>
                                </div>

                                <div class="mb-3">
                                    <label for="idVenta" class="form-label texto-principal">Venta</label>
                                    <select class="form-select form-select-custom" id="idVenta" name="idVenta" required>
                                        <c:forEach var="venta" items="${listaVentas}">
                                            <option value="${venta.idVenta}" ${detalleVenta.venta.idVenta == venta.idVenta ? 'selected' : ''}>
                                                Venta #${venta.idVenta} - ${venta.cliente.nombreCliente} ${venta.cliente.apellidoCliente}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label for="idProducto" class="form-label texto-principal">Producto</label>
                                    <select class="form-select form-select-custom" id="idProducto" name="idProducto" required>
                                        <c:forEach var="producto" items="${listaProductos}">
                                            <option value="${producto.idProducto}" ${detalleVenta.producto.idProducto == producto.idProducto ? 'selected' : ''}>
                                                ${producto.nombreProducto} - Q${producto.precio}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label for="cantidad" class="form-label texto-principal">Cantidad</label>
                                    <input type="number" class="form-control form-control-custom" id="cantidad" 
                                           name="cantidad" value="${detalleVenta.cantidad}" required min="1" step="1">
                                </div>

                                <div class="mb-4">
                                    <label for="precioUnitario" class="form-label texto-principal">Precio Unitario</label>
                                    <input type="number" class="form-control form-control-custom" id="precioUnitario" 
                                           name="precioUnitario" value="${detalleVenta.precioUnitario}" required min="0.01" step="0.01">
                                </div>

                                <div class="d-flex justify-content-end">
                                    <button type="submit" class="btn btn-custom-primary">
                                        Actualizar Detalle de Venta
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
