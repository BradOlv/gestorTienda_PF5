<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Relojería Bradley - Menú Principal</title>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <style>
        /* INICIO DE ESTILOS CSS INTEGRADOS */
        body {
            background: #f8f9fa; 
            color: #343a40;
            font-family: 'Montserrat', Arial, sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        
        /* --- HEADER --- */
        .header-relojeria {
            background: #343a40; 
            color: #ffffff;
            padding: 18px 60px;
            display: flex;
            justify-content: center;
            align-items: center;
            border-bottom: 2px solid #007bff; 
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .header-relojeria h1 {
            margin: 0;
            font-size: 2.5em;
            font-weight: 800;
            letter-spacing: 2px;
            color: #ffffff;
            text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
        }

        /* --- BANNER BULOVA --- */
        .banner-principal {
            position: relative;
            width: 100%;
            height: 480px;
            overflow: hidden;
            background: #0d1b2a;
            /* SEPARACIÓN: Mantenemos el margen de 70px */
            margin-top: 70px;
            margin-bottom: 40px; 
        }
        .img-banner {
            width: 100%;
            height: 100%;
            object-fit: cover;
            filter: brightness(0.85);
        }
        .banner-texto {
            position: absolute;
            top: 50%;
            left: 10%;
            transform: translateY(-50%);
            color: #fff;
            text-shadow: 0 2px 8px #0008;
        }
        .banner-texto h1 {
            font-size: 3.2em;
            margin: 0 0 10px 0;
            letter-spacing: 2px;
            font-weight: 800;
        }
        .banner-texto p {
            font-size: 1.3em;
            margin-bottom: 22px;
            font-weight: 400;
        }
        .btn-banner {
            background: #222;
            color: #fff;
            padding: 12px 28px;
            border-radius: 4px;
            text-decoration: none;
            font-weight: bold;
            font-size: 1.05em;
            letter-spacing: 1px;
            border: none;
            transition: background 0.2s;
        }
        .btn-banner:hover {
            background: #1976d2;
        }


        /* --- CONTENIDO PRINCIPAL (TARJETAS) --- */
        .main-content {
            flex-grow: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px 20px;
        }

        .action-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr); 
            gap: 30px; 
            max-width: 1200px;
            width: 100%;
            margin: 0 auto;
        }

        .action-card {
            /* ESTILOS DE FONDO DE IMAGEN */

            background-color: rgba(0, 0, 0, 0.5); /* Color oscuro base para el overlay */
            background-blend-mode: overlay; /* Mezcla el color oscuro con la imagen */
            background-size: cover;
            background-position: center center;
            background-repeat: no-repeat;
            
            color: #ffffff; /* Texto de la tarjeta blanco */
            
            border-radius: 12px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
            padding: 40px;
            text-align: center;
            border: 1.5px solid #e0e0e0;
            transition: box-shadow 0.3s, border 0.3s, transform 0.3s;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            min-height: 250px;
        }
        .action-card:hover {
            border: 1.5px solid #007bff;
            box-shadow: 0 12px 36px rgba(0, 123, 255, 0.2);
            transform: translateY(-8px) scale(1.02);
            background-color: rgba(0, 0, 0, 0.3);
        }
        
        .action-card h3 {
            color: #ffffff; /* Título blanco */
            font-size: 1.8em;
            margin-bottom: 25px;
            font-weight: 700;
            letter-spacing: 1px;
            text-shadow: 1px 1px 3px #000;
        }
        .action-card .btn-action {
            display: inline-block;
            background: #007bff;
            color: #fff;
            padding: 15px 30px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            font-size: 1.1em;
            border: none;
            transition: background 0.2s, transform 0.2s;
            box-shadow: 0 4px 12px rgba(0, 123, 255, 0.3);
        }
        .action-card .btn-action:hover {
            background: #0056b3;
            transform: translateY(-2px);
        }
        
        .action-card .btn-admin {
            background: #ffc107; 
            box-shadow: 0 4px 12px rgba(255, 193, 7, 0.3);
        }
        .action-card .btn-admin:hover {
            background: #e0a800;
        }

        /* FONDOS INDIVIDUALES PARA CADA TARJETA */
        /* Context Root: /proyectofinal_PFS */
        .card-login {
            background-image: url('/proyectofinal_PFS/image/login.jpg'); 
        }

        .card-register {
            background-image: url('/proyectofinal_PFS/image/register.jpg');
        }

        .card-admin {
            background-image: url('/proyectofinal_PFS/image/admin.jpg'); 
        }


        /* --- FOOTER --- */
        .footer-relojeria {
            background: #343a40;
            color: #ffffff;
            text-align: center;
            padding: 20px 0;
            font-size: 1em;
            border-top: 2px solid #007bff;
            box-shadow: 0 -4px 8px rgba(0, 0, 0, 0.1);
            margin-top: auto;
        }

        /* --- RESPONSIVE --- */
        @media (max-width: 992px) {
            .action-grid {
                grid-template-columns: repeat(2, 1fr); 
                gap: 25px;
            }
        }

        @media (max-width: 576px) {
            .header-relojeria h1 {
                font-size: 1.8em;
            }
            .banner-principal {
                height: 250px;
            }
            .banner-texto {
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
                text-align: center;
                width: 90%;
            }
            .banner-texto h1 {
                font-size: 2em;
            }
            .banner-texto p {
                font-size: 1em;
            }
            .action-grid {
                grid-template-columns: 1fr;
                gap: 20px;
                padding: 0 15px;
            }
        }
        /* FIN DE ESTILOS CSS INTEGRADOS */
    </style>
</head>
<body>
    
    <header class="header-relojeria">
        <h1>Relojería Bradley</h1>
    </header>

    <section class="banner-principal">
        <img src="${pageContext.request.contextPath}/image/bulova-relojes-coleccion-Wilton-elegancia-atemporal-funcion-avanzada-lujo-precision-GMT-7.jpeg" alt="Banner Reloj Bulova" class="img-banner">

        <div class="banner-texto">
            <h1>BULOVA</h1>
            <p>from Professional to Classic</p>
            <a href="${pageContext.request.contextPath}/pages/comprar.html" class="btn-banner">Inicia sesión para más</a>
        </div>
    </section>
    
    <div class="main-content">
        <div class="action-grid">
            
            <div class="action-card card-login">
                <h3>Iniciar Sesión</h3>
                <a href="${pageContext.request.contextPath}/pages/login.jsp" class="btn-action">
                    Acceder
                </a>
            </div>
            
            <div class="action-card card-register">
                <h3>Registrarse</h3>
                <a href="${pageContext.request.contextPath}/pages/registro.jsp" class="btn-action">
                    Crear Cuenta
                </a>
            </div>
            
            <div class="action-card card-admin">
                <h3>Administración</h3>
                <a href="${pageContext.request.contextPath}/UsuarioServlet?accion=listar" class="btn-action btn-admin">
                    Gestionar Usuarios
                </a>
            </div>
            
        </div>
    </div>

    <footer class="footer-relojeria">
        © 2025 Relojería Bradley. Todos los derechos reservados.
    </footer>

</body>
</html>