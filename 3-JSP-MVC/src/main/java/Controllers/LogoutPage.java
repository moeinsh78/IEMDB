package Controllers;

import IEMDBClasses.IEMDBSystem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LogoutPage", value = "/logout")
public class LogoutPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        if(iemdbSystem.isLogin()) {
            iemdbSystem.logout();
            response.sendRedirect("/login");
        }
        else {
            HttpSession session = request.getSession(false);
            session.setAttribute("errorText", "No Logged in Users Found!");
            response.sendRedirect("/error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
