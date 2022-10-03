package Controllers;

import Errors.ActorNotFoundError;
import Errors.MovieNotFoundError;
import IEMDBClasses.IEMDBSystem;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ActorPage", value = "/actors/*")
public class ActorPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        if(!iemdbSystem.isLogin()){
            response.sendRedirect("/login");
        }
        else {
            String[] split_url = request.getRequestURI().split("/");
            try {
                int actor_id = Integer.parseInt(split_url[2]);
                request.setAttribute("actor",iemdbSystem.getActorById(actor_id));
                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/actor.jsp");
                requestDispatcher.forward(request, response);
            } catch (Exception e) {
                HttpSession session = request.getSession(false);
                session.setAttribute("errorText", e.getMessage());
                response.sendRedirect("/error");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
