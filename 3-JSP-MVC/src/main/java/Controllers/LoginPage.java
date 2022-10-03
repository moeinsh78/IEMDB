package Controllers;

import Errors.UserNotFoundError;
import IEMDBClasses.IEMDBSystem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoginPage", value = "/login")
public class LoginPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        if(!iemdbSystem.isLogin()){
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        else {
            response.sendRedirect("/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_email = request.getParameter("email");
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        try {
            iemdbSystem.login(user_email);
            request.setAttribute("userEmail", user_email);
            response.sendRedirect("/");

        } catch (UserNotFoundError e) {
            HttpSession session = request.getSession(false);
            session.setAttribute("errorText", e.getMessage());
            response.sendRedirect("/error");
        }
    }
}
