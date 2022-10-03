package Controllers;

import IEMDBClasses.IEMDBSystem;
import IEMDBClasses.Movie;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "WatchListPage", value = "/watchlist")
public class WatchListPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        if(iemdbSystem.isLogin()){
            request.getRequestDispatcher("watchlist.jsp").forward(request, response);
        }
        else {
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();

        String user_email = request.getParameter("user_id");
        int movie_id = Integer.parseInt(request.getParameter("movie_id"));
        try {
            iemdbSystem.removeFromWatchList(user_email, movie_id);
            request.getRequestDispatcher("watchlist.jsp").forward(request, response);
        } catch (Exception e) {
            HttpSession session = request.getSession(false);
            session.setAttribute("errorText", e.getMessage());
            response.sendRedirect("/error");
        }
    }
}