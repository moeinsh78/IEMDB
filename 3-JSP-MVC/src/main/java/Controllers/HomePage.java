package Controllers;

import IEMDBClasses.IEMDBSystem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "HomePage", value = "")
public class HomePage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        if(iemdbSystem.isLogin()){
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
        else {
            response.sendRedirect("/login");
        }
    }
}
