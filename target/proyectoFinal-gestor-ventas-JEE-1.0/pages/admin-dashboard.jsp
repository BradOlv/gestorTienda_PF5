<%-- 
    Document    : admin-dashboard
    Created on : 7/10/2025, 21:14:13
    Author      : Kevin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.RolUsuario"%>
<%
    // Verificar que el usuario esté logueado y sea administrador
    if (session.getAttribute("idUsuario") == null) {
        // Asumiendo que admin-login.jsp está en la misma carpeta (/pages/)
        response.sendRedirect("admin-login.jsp?error=acceso_denegado");
        return;
    }

    String rolUsuario = (String) session.getAttribute("rol");
    if (!"Admin".equals(rolUsuario)) {
        // Redirigir al inicio o a una página de error si no es Admin
        response.sendRedirect("../menuPrincipal.jsp?error=no_autorizado");
        return;
    }

    String nombreAdmin = (String) session.getAttribute("nombre");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Panel de Administración</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&family=Open+Sans:wght@400;600&family=Georgia&display=swap" rel="stylesheet">
        
        <style>
            :root {
                /* Colores de tu tema administrativo */
                --color-principal: rgb(48, 87, 118);      /* Azul */
                --color-secundario: rgb(255, 170, 102);   /* Naranja suave */
                --color-secundario-foco: rgb(255, 140, 70); /* Naranja fuerte */
                --font-georgia: "Georgia", serif;
            }

            body {
                font-family: 'Open Sans', sans-serif;
                background-color: #f8f9fa; /* Fondo gris claro */
            }

            /* 1. Barra de Navegación */
            .navbar {
                background-color: var(--color-principal) !important; /* Color principal (Azul) */
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                font-family: var(--font-georgia);
            }
            .navbar-brand {
                font-weight: 700;
                color: white !important;
            }
            .navbar-brand img {
                width: 35px; /* Icono más pequeño */
                height: 35px;
            }
            
            /* 4. Estilo de Botones de Mantenimiento */
            .btn-maintenance-access {
                background-color: var(--color-principal);
                color: white;
                border: none;
                transition: all 0.3s;
                font-weight: 600;
                padding: 15px 10px;
                border-radius: 8px;
                line-height: 1.2;
                font-family: 'Montserrat', sans-serif;
                text-align: center;
            }
            .btn-maintenance-access i {
                font-size: 1.5rem;
                display: block;
                margin-bottom: 5px;
            }
            .btn-maintenance-access:hover {
                background-color: rgb(30, 70, 95);
                transform: translateY(-2px);
                color: white;
            }

            .btn-primary-admin {
                background-color: var(--color-principal);
            }
            .btn-primary-admin:hover {
                background-color: rgb(30, 70, 95);
            }

            .btn-secondary-admin {
                background-color: #6c757d; /* Gris para acciones menos destacadas */
            }
            .btn-secondary-admin:hover {
                background-color: #5a6268;
            }

            .btn-venta-foco {
                background-color: var(--color-secundario-foco) !important;
                color: white !important;
            }
            .btn-venta-foco:hover {
                background-color: rgb(200, 100, 50) !important;
            }
        </style>
    </head>
    <body class="bg-light">
        <nav class="navbar navbar-expand-lg">
            <div class="container-fluid">
                <a class="navbar-brand" href="../index.jsp">
                    <span class="ms-2">PANEL DE ADMINISTRACIÓN</span>
                </a>    
                <div class="d-flex align-items-center text-white">
                    <span class="me-3 d-none d-sm-inline">Bienvenido, <%= nombreAdmin%></span>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline-light">
                        <i class="bi bi-box-arrow-right"></i> Cerrar Sesión
                    </a>
                </div>
            </div>
        </nav>
        
        <div class="container-fluid mt-4">
            <div class="row">
                <div class="col-12">
                    <h3 class="mb-4" style="font-family: var(--font-georgia); color: #333;">Bienvenido, <%= nombreAdmin%></h3>

                    <div class="card mb-4">
                        <div class="card-header bg-white">
                            <h5 class="mb-0 text-dark"><i class="bi bi-gear"></i> **MANTENIMIENTO DE ENTIDADES**</h5>
                        </div>
                        <div class="card-body">
                            
                            <div class="row row-cols-2 row-cols-md-3 row-cols-lg-5 g-4 justify-content-center">
                                
                                <div class="col">
                                    <a href="${pageContext.request.contextPath}/UsuarioServlet?accion=listar" class="btn btn-maintenance-access btn-primary-admin w-100">
                                        <i class="bi bi-people"></i>
                                        Usuarios
                                    </a>
                                </div>
                                
                                <div class="col">
                                    <a href="${pageContext.request.contextPath}/ProductoServlet?accion=listar" class="btn btn-maintenance-access btn-primary-admin w-100">
                                    <i class="bi bi-box"></i>
                                        Productos
                                    </a>
                                </div>
                                
                                <div class="col">
                                    <a href="${pageContext.request.contextPath}/ClienteServlet?accion=listar" class="btn btn-maintenance-access btn-primary-admin w-100">
                                        <i class="bi bi-person-badge"></i>
                                        Clientes
                                    </a>
                                </div>

                                <div class="col">
                                    <a href="${pageContext.request.contextPath}/VentaServlet?accion=listar" class="btn btn-maintenance-access btn-venta-foco w-100">
                                        <i class="bi bi-tags"></i>
                                        Ventas
                                    </a>
                                </div>

                                <div class="col">
                                    <a href="${pageContext.request.contextPath}/DetalleVentaServlet?accion=listar" class="btn btn-maintenance-access btn-secondary-admin w-100">
                                        <i class="bi bi-receipt"></i>
                                        Detalles Venta
                                    </a>
                                </div>
                                
                                </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>