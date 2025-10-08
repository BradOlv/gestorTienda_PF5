<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Administración de Clientes</title>
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
        </style>
    </head>
    
      <body>
        <div class="titulo-bg">
            <div class="container d-flex justify-content-between align-items-center">
                <h2 class="my-3 text-white" style="font-family: var(--font-georgia);">Administración de Clientes</h2>
                <%-- Ajusta la URL de retorno según tu estructura de menú --%>
                <a href="pages/menuPrincipal.jsp" class="btn btn-sm btn-outline-light" style="font-family: var(--font-georgia); border-radius: 8px;">Volver al Menú</a>
            </div>
        </div>

        <div class="container my-5">
            <div class="row mb-4">
                <div class="col-md-12">
                    <%-- Enlace que llama al Servlet con acción 'editar' e id=0 para mostrar el formulario vacío (Agregar) --%>
                    <a href="ClienteServlet?accion=editar&id=0" class="btn btn-custom-primary">
                        Agregar Nuevo Cliente
                    </a>
                </div>
            </div>

            <%-- TABLA DE CLIENTES --%>
            <div class="table-responsive">
                <table class="table table-custom align-middle">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nombre</th>
                            <th>Apellido</th>
                            <th>Email</th>
                            <th>Teléfono</th>
                            <th>Dirección</th>
                            <th>NIT</th>
                            <th class="text-center">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%-- Itera sobre la lista de Clientes enviada desde ServletClientes --%>
                        <c:forEach var="cliente" items="${listaClientes}">
                            <tr>
                                <td class="texto-principal">${cliente.idCliente}</td>
                                <td class="texto-principal">${cliente.nombreCliente}</td>
                                <td class="texto-principal">${cliente.apellidoCliente}</td>
                                <td class="texto-principal">${cliente.emailCliente}</td>
                                <td class="texto-principal">${cliente.telefonoCliente}</td>
                                <td class="texto-principal">${cliente.direccionCliente}</td>
                                <td class="texto-principal">${cliente.nitCliente}</td>
                                <td class="text-center">
                                    <%-- Botón Editar: Llama al formularioEditar del Servlet --%>
                                    <a href="ClienteServlet?accion=editar&id=${cliente.idCliente}" class="btn btn-custom-primary btn-sm">
                                        Editar
                                    </a>
                                    <%-- Botón Eliminar: Llama a eliminarCliente del Servlet --%>
                                    <a href="ClienteServlet?accion=eliminar&id=${cliente.idCliente}" 
                                       onclick="return confirm('¿Está seguro de eliminar a ${cliente.nombreCliente} ${cliente.apellidoCliente}?')" 
                                       class="btn btn-sm btn-danger">
                                        Eliminar
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <%-- Mensaje si no hay clientes --%>
            <c:if test="${empty listaClientes}">
                <div class="alert alert-info mt-3" role="alert">
                    No hay clientes registrados en la base de datos.
                </div>
            </c:if>

        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    </body>
</html>