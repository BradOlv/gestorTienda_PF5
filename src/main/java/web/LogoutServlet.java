/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Kevin
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener la sesión actual
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Verificar si era administrador
            boolean eraAdmin = "Admin".equals(session.getAttribute("rol"));
            
            // Invalidar la sesión
            session.invalidate();
            
            // Redirigir según el tipo de usuario
            if (eraAdmin) {
                response.sendRedirect("pages/admin-login.jsp?logout=exitoso");
            } else {
                response.sendRedirect("pages/login.jsp?logout=exitoso");
            }
        } else {
            // Si no había sesión, redirigir al inicio
            response.sendRedirect("index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}