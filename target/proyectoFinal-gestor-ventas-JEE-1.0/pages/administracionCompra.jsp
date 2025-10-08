<%-- /pages/administracionCompra.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="model.Cliente" %>
<%@page import="model.Producto" %>
<%@page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Administración de Compras (Ventas)</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    <style>
        /* Variables de Estilo del Formulario Existente */
        :root {
            --color-principal: rgb(48, 87, 118); /* Azul/Gris oscuro */
            --color-secundario: rgb(255, 170, 102); /* Naranja suave */
            --color-secundario-foco: rgb(255, 140, 70); /* Naranja foco */
            --font-georgia: "Georgia", serif; /* Tipografía para formularios */
            --font-montserrat: 'Montserrat', Arial, sans-serif; /* Tipografía para encabezados/general */
        }
        
        body {
            font-family: var(--font-montserrat);
            background: #f8f9fa;
        }

        .titulo-bg {
            background-color: var(--color-principal);
            color: white;
            padding: 1rem 0;
            margin-bottom: 2rem;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .texto-principal { 
            color: var(--color-principal);
            font-family: var(--font-georgia);
        }
        
        /* Botones y Formulario Adaptados */
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
        
        /* Estilos específicos para la interfaz de compra */
        .product-list { max-height: 400px; overflow-y: auto; }
        .product-item { cursor: pointer; transition: background-color 0.2s; }
        .product-item:hover { background-color: #f0f8ff; }
        .card-header-cliente { background-color: var(--color-principal) !important; }
        .card-header-productos { background-color: #007bff !important; } /* Usamos un color Bootstrap para contraste */
        .card-header-carrito { background-color: #28a745 !important; } /* Usamos otro color Bootstrap para contraste */
    </style>
</head>
<body>
    
    <div class="titulo-bg">
        <div class="container">
           <h2 class="my-3 text-white">Registro de Nueva Venta (Compra)</h2>
        </div>
    </div>

    <div class="container">
        
        <c:if test="${not empty mensajeError}">
            <div class="alert alert-danger" role="alert">${mensajeError}</div>
        </c:if>
        
        <form id="formVenta" action="VentaServlet" method="POST">
            <input type="hidden" name="accion" value="crearVenta"/>

            <div class="card mb-4 shadow-sm border-0">
                <div class="card-header card-header-cliente text-white fw-bold">
                    Selección de Cliente
                </div>
                <div class="card-body">
                    <div class="mb-3">
                        <label for="idCliente" class="form-label texto-principal">Cliente:</label>
                        <select id="idCliente" name="idCliente" class="form-select form-select-custom" required>
                            <option value="">-- Seleccione un Cliente --</option>
                            <c:forEach var="cliente" items="${listaClientes}">
                                <option value="${cliente.idCliente}">${cliente.nombreCliente} ${cliente.apellidoCliente} (NIT: ${cliente.nitCliente})</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <div class="card shadow-sm border-0">
                        <div class="card-header card-header-productos text-white fw-bold">Productos Disponibles</div>
                       <div class="card-body product-list">
    <ul class="list-group list-group-flush">
        <c:choose>
            <c:when test="${empty listaProductos}">
                <li class="list-group-item text-center text-danger">⚠️ No se encontraron productos en la base de datos.</li>
            </c:when>
            <c:otherwise>
                <c:set var="productosConStock" value="false"/>
                <c:forEach var="producto" items="${listaProductos}">
                    <c:if test="${producto.stock > 0}">
                        <c:set var="productosConStock" value="true"/>
                        <li class="list-group-item d-flex justify-content-between align-items-center product-item"
                            data-id="${producto.idProducto}"
                            data-nombre="${producto.nombreProducto}"
                            data-precio="${producto.precio}"
                            data-stock="${producto.stock}"
                            onclick="agregarACarrito(this)">
                            <span class="fw-bold">${producto.nombreProducto}</span> - Q${producto.precio}
                            <span class="badge bg-secondary rounded-pill">Stock: ${producto.stock}</span>
                        </li>
                    </c:if>
                </c:forEach>
                <c:if test="${!productosConStock}">
                    <li class="list-group-item text-center text-warning">❌ Todos los productos están sin stock.</li>
                </c:if>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="card shadow-sm border-0">
                        <div class="card-header card-header-carrito text-white fw-bold">Carrito de Compra</div>
                        <div class="card-body">
                            <table class="table table-sm">
                                <thead>
                                    <tr>
                                        <th>Producto</th>
                                        <th>Cant.</th>
                                        <th>Precio</th>
                                        <th>Subtotal</th>
                                        <th>Acción</th>
                                    </tr>
                                </thead>
                                <tbody id="cuerpoCarrito">
                                    <tr><td colspan="5" class="text-center text-muted">Aún no hay productos.</td></tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="3" class="text-end fw-bold texto-principal">Total:</td>
                                        <td id="totalCompra" colspan="2" class="fw-bold text-end">Q0.00</td>
                                    </tr>
                                </tfoot>
                            </table>
                            <button type="submit" id="btnFinalizar" class="btn btn-custom-primary w-100 mt-3" disabled>Finalizar Compra</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <script>
        let carrito = {}; // {id: {nombre, precio, cantidad, stock}}
        
        function actualizarTotal() {
            let total = 0;
            for (let id in carrito) {
                total += carrito[id].cantidad * carrito[id].precio;
            }
            $('#totalCompra').text('Q' + total.toFixed(2));
            
            const hayProductos = Object.keys(carrito).length > 0;
            const hayCliente = $('#idCliente').val() !== "";
            
            $('#btnFinalizar').prop('disabled', !(hayProductos && hayCliente));
        }

        function agregarACarrito(element) {
            const id = $(element).data('id');
            const nombre = $(element).data('nombre');
            const precio = parseFloat($(element).data('precio'));
            const stock = parseInt($(element).data('stock'));

            if (carrito[id]) {
                if (carrito[id].cantidad < stock) {
                    carrito[id].cantidad++;
                } else {
                    alert('Stock máximo (' + stock + ') alcanzado para ' + nombre);
                }
            } else {
                carrito[id] = { nombre: nombre, precio: precio, cantidad: 1, stock: stock };
            }
            dibujarCarrito();
        }

        function dibujarCarrito() {
            const $cuerpo = $('#cuerpoCarrito');
            $cuerpo.empty();

            if (Object.keys(carrito).length === 0) {
                $cuerpo.append('<tr><td colspan="5" class="text-center text-muted">Aún no hay productos.</td></tr>');
            }
            
            for (let id in carrito) {
                const item = carrito[id];
                const subtotal = item.cantidad * item.precio;
                
                const hiddenInputs = `
                    <input type="hidden" name="idProducto" value="${id}">
                    <input type="hidden" name="cantidad" value="${item.cantidad}" class="input-cantidad-${id}">
                    <input type="hidden" name="precioUnitario" value="${item.precio}">
                `;

                $cuerpo.append(`
                    <tr>
                        <td>${item.nombre} ${hiddenInputs}</td>
                        <td class="text-center">
                            <input type="number" min="1" max="${item.stock}" value="${item.cantidad}" 
                                   data-id="${id}" class="form-control form-control-sm text-center form-control-custom"
                                   style="width: 70px;" onchange="actualizarCantidad(this)">
                        </td>
                        <td>Q${item.precio.toFixed(2)}</td>
                        <td class="text-end">Q<span class="subtotal-item">${subtotal.toFixed(2)}</span></td>
                        <td><button type="button" class="btn btn-sm btn-danger" onclick="quitarDeCarrito(${id})">X</button></td>
                    </tr>
                `);
            }
            actualizarTotal();
        }
        
        function actualizarCantidad(input) {
            const id = $(input).data('id');
            let nuevaCantidad = parseInt($(input).val());
            const stock = carrito[id].stock;

            if (isNaN(nuevaCantidad) || nuevaCantidad < 1) {
                nuevaCantidad = 1;
            } else if (nuevaCantidad > stock) {
                 nuevaCantidad = stock;
                 alert('Stock máximo (' + stock + ') alcanzado para este producto.');
            } 
            
            $(input).val(nuevaCantidad);
            carrito[id].cantidad = nuevaCantidad;
            
            $(`.input-cantidad-${id}`).val(carrito[id].cantidad);
            dibujarCarrito(); 
        }
        
        function quitarDeCarrito(id) {
            delete carrito[id];
            dibujarCarrito();
        }
        
        $(document).ready(function() {
            $('#idCliente').on('change', function() {
                actualizarTotal();
            });
            dibujarCarrito();
        });

    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>