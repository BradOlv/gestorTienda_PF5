<%-- 
    Document    : admin-login
    Created on : 7/10/2025
    Author     : Kevin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Acceso de Administrador - GuitarKinal</title>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&family=Open+Sans:wght@400;600&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
        
        <style>
            :root {
                /* Colores de tu tema administrativo */
                --color-principal: rgb(48, 87, 118);      /* Azul (para fondos principales/botones) */
                --color-secundario: rgb(255, 170, 102);   /* Naranja suave (para bordes de foco) */
                --color-secundario-foco: rgb(255, 140, 70); /* Naranja fuerte */
                --font-georgia: "Georgia", serif;
            }
            
            body {
                font-family: 'Open Sans', sans-serif;
                /* Fondo suave degradado o sólido que combine con el tema admin */
                background: linear-gradient(135deg, var(--color-principal) 0%, #000000 100%);
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
                margin: 0;
            }

            .back-button {
                position: absolute;
                top: 20px;
                left: 20px;
                background: rgba(255, 255, 255, 0.2);
                border: 2px solid rgba(255, 255, 255, 0.3);
                color: white;
                padding: 10px 20px;
                border-radius: 50px;
                text-decoration: none;
                font-weight: 600;
                transition: all 0.3s ease;
                backdrop-filter: blur(10px);
            }

            .back-button:hover {
                background: rgba(255, 255, 255, 0.3);
                color: white;
                transform: translateY(-2px);
            }

            .admin-container {
                background: rgba(255, 255, 255, 0.98); /* Más opaco */
                backdrop-filter: blur(20px);
                padding: 50px 60px;
                border-radius: 15px; /* Menos redondeado que antes */
                box-shadow: 0px 10px 30px rgba(0, 0, 0, 0.2);
                width: 100%;
                max-width: 450px; /* Más compacto */
                text-align: center;
                border: 1px solid rgba(220, 220, 220, 0.5);
            }

            .admin-icon {
                font-size: 4rem;
                color: var(--color-principal); /* Usar el color principal */
                margin-bottom: 20px;
                /* Animación removida para un look más serio de administración */
            }

            .admin-container h2 {
                color: var(--color-principal); /* Usar el color principal */
                margin-bottom: 5px;
                font-family: var(--font-georgia);
                font-weight: 700;
                font-size: 2.2rem;
            }

            .admin-subtitle {
                color: #666;
                margin-bottom: 30px;
                font-size: 1rem;
            }

            label {
                display: block;
                text-align: left;
                color: #333;
                margin-bottom: 8px;
                font-weight: 600;
                font-size: 0.95em;
                font-family: var(--font-georgia); /* Usar tu fuente principal */
            }

            input[type="email"],
            input[type="password"] {
                width: 100%;
                padding: 12px 15px; /* Más pequeño y limpio */
                margin: 8px 0 20px 0;
                border: 1px solid #ccc;
                border-radius: 8px;
                font-size: 1em;
                color: #333;
                background-color: #ffffff;
                transition: all 0.3s ease;
            }

            input[type="email"]:focus,
            input[type="password"]:focus {
                border-color: var(--color-secundario-foco); /* Foco con tu naranja fuerte */
                box-shadow: 0 0 0 0.25rem rgba(255, 140, 70, 0.3); /* Sombra de foco */
                outline: none;
                background-color: white;
            }

            input[type="submit"] {
                background: var(--color-principal); /* Fondo azul sólido */
                color: white;
                padding: 14px;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                width: 100%;
                font-size: 1.1em;
                font-weight: 700;
                letter-spacing: 0.5px;
                transition: all 0.3s ease;
                margin-top: 10px;
            }

            input[type="submit"]:hover {
                background: rgb(30, 70, 95); /* Tono más oscuro al pasar el mouse */
                transform: translateY(-2px);
                box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
            }

            .warning-text {
                /* Usar el esquema de colores de advertencia de Bootstrap */
                background: rgba(255, 193, 7, 0.1);
                border: 1px solid rgba(255, 193, 7, 0.3);
                color: #856404;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 25px;
                font-size: 0.9rem;
            }
            
            .alert-danger {
                /* Asegurar que la alerta de error siga el estilo de Bootstrap */
                border-radius: 8px;
            }

            .logo-small {
                width: 150px; /* Tamaño reducido para mejor ajuste */
                height: auto;
                margin-bottom: 10px;
                opacity: 0.9;
            }
        </style>
    </head>
    <body>
        <a href="${pageContext.request.contextPath}/menuPrincipal.jsp">
            <i class="bi bi-arrow-left"></i> Regresar al Menú Principal
        </a>

        <div class="admin-container">


            <h2>Iniciar Sesion</h2>
            <p class="admin-subtitle">Acceso restringido para administradores</p>

            <div class="warning-text">
                <i class="bi bi-exclamation-triangle"></i>
                <strong>Área Restringida:</strong> Solo personal autorizado puede acceder a esta sección.
            </div>

            <%
                String error = request.getParameter("error");
                if (error != null) {
                    String mensaje = "";
                    if ("campos_vacios".equals(error)) {
                        mensaje = "Por favor complete todos los campos.";
                    } else if ("no_encontrado".equals(error)) {
                        mensaje = "Usuario no encontrado.";
                    } else if ("credenciales".equals(error)) {
                        mensaje = "Credenciales incorrectas.";
                    } else if ("no_autorizado".equals(error)) {
                        mensaje = "No tiene permisos de administrador.";
                    } else if ("acceso_denegado".equals(error)) {
                        mensaje = "Acceso denegado. Debe iniciar sesión.";
                    } else if ("error_servidor".equals(error)) {
                        mensaje = "Error interno del servidor.";
                    }
            %>
            <div class="alert alert-danger" role="alert">
                <i class="bi bi-exclamation-circle"></i> **<%= mensaje %>**
            </div>
            <%
                }
            %>

            <form method="post" action="${pageContext.request.contextPath}/admin-login">
                <label for="email">Correo de Administrador:</label>
                <input type="email" name="email" id="email" placeholder="admin@gestiontienda.com" required>

                <label for="contrasena">Contraseña:</label>
                <input type="password" name="contrasena" id="contrasena" placeholder="••••••••" required>

                <input type="submit" value="🔐 Acceder al Panel">
            </form>

            <div class="mt-4">
                <small class="text-muted">
                    <i class="bi bi-info-circle"></i>
                    Si no tienes credenciales de administrador, contacta al administrador del sistema.
                </small>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>