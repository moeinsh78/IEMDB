package Controllers;

import IEMDBClasses.IEMDBSystem;
import IEMDBClasses.Movie;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Objects;

@WebServlet(name = "Movies", value = "/movies")
public class MoviesPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        if(!iemdbSystem.isLogin()){
            response.sendRedirect("/login");
        }
        else {
            request.setAttribute("searched_movie", "false");
            request.setAttribute("clear", "false");
            request.setAttribute("sort_by_imdb", "false");
            request.setAttribute("sort_by_date", "false");
            request.getRequestDispatcher("movies.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        if(!iemdbSystem.isLogin()){
            response.sendRedirect("/login");
        }
        else {
            String searched_name = request.getParameter("search");
            String submit_button = request.getParameter("action");
            String hidden_search = request.getParameter("hidden_search");
            String hidden_sort_by_date = request.getParameter("hidden_sort_by_date");
            request.setAttribute("searched_movie", "false");
            request.setAttribute("clear", "false");
            request.setAttribute("sort_by_imdb", "false");
            request.setAttribute("sort_by_date", "false");
            if(submit_button != null){
                switch (submit_button) {
                    case "search":
                        if (!searched_name.isBlank()) {
                            request.setAttribute("searched_movie", searched_name);
                        }
                        if((!Objects.equals(hidden_sort_by_date, "")) && (!hidden_sort_by_date.equals("false"))) {
                            request.setAttribute("sort_by_date", "true");
                        }
                        else {
                            request.setAttribute("sort_by_imdb", "true");
                        }
                        break;
                    case "clear":
                        if((!Objects.equals(hidden_sort_by_date, "")) && (!hidden_sort_by_date.equals("false"))) {
                            request.setAttribute("sort_by_date", "true");
                        }
                        else {
                            request.setAttribute("sort_by_imdb", "true");
                        }
                        request.setAttribute("clear", "true");
                        break;
                    case "sort_by_imdb":
                        if(!Objects.equals(hidden_search, "")) {
                            request.setAttribute("searched_movie", hidden_search);
                        }
                        request.setAttribute("sort_by_imdb", "true");
                        break;
                    case "sort_by_date":
                        if(!Objects.equals(hidden_search, "")) {
                            request.setAttribute("searched_movie", hidden_search);
                        }
                        request.setAttribute("sort_by_date", "true");
                        break;
                }
            }
            request.getRequestDispatcher("movies.jsp").forward(request, response);
        }
    }
}
