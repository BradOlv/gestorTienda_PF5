<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html> 
<html> 
    <head> 
        <meta charset="UTF-8"> 
        <title>Tienda de Guitarras</title> 
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous"> 
        
        <style>
            .card-img-top {
                width: 100%;
                height: 280px;
                object-fit: cover;
                border-radius: 0.5rem 0.5rem 0 0;
            }

            .card-body {
                padding: 2rem 1.5rem;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                height: 100%;
            }

            .card {
                min-height: 500px;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                align-items: center;
                text-align: center;
                box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
                border-radius: 0.5rem;
                transition: all 0.3s ease;
            }

            .card:hover {
                transform: scale(1.02);
            }

            .row {
                margin-bottom: 2.5rem;
            }
        </style>
    </head> 
    
    <body>
        <div class="container mt-5 text-center">
            <h1 class="text-primary">¡Tienda de no se q!</h1>
            <p class="lead">Si ves este mensaje, la conexión entre NetBeans, Maven y GlassFish ha sido exitosa.</p>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    </body>
</html>