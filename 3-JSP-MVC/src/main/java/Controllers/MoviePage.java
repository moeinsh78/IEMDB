package Controllers;

import Errors.MovieNotFoundError;
import IEMDBClasses.Comment;
import IEMDBClasses.IEMDBSystem;
import IEMDBClasses.Movie;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Objects;

@WebServlet(name = "MoviePage", value = "/movies/*")
public class MoviePage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        if(!iemdbSystem.isLogin()){
            response.sendRedirect("/login");
        }
        else {
            String[] split_url = request.getRequestURI().split("/");
            try {
                int movie_id = Integer.parseInt(split_url[2]);
                request.setAttribute("movie",iemdbSystem.findMovieById(movie_id));
                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/movie.jsp");
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
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        if (!iemdbSystem.isLogin()) {
            response.sendRedirect("/login");
        } else {
            String submit_button = request.getParameter("action");
            String score = request.getParameter("quantity");
            int movie_id = Integer.parseInt(request.getParameter("movie_id"));
            String comment_id = request.getParameter("comment_id");
            String comment_txt = request.getParameter("comment");

            try {
                request.setAttribute("movie", iemdbSystem.findMovieById(movie_id));
            } catch (MovieNotFoundError e) {
                HttpSession session = request.getSession(false);
                session.setAttribute("errorText", e.getMessage());
                response.sendRedirect("/error");
            }
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/movie.jsp");

            if (submit_button != null) {
                switch (submit_button) {
                    case "rate":
                        try {
                            iemdbSystem.rateMovie(movie_id, iemdbSystem.getLoggedInUser().getEmail(), Integer.parseInt(score));
                            requestDispatcher.forward(request, response);
                        } catch (Exception e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                        break;
                    case "add":
                        try {
                            iemdbSystem.addToWatchList(iemdbSystem.getLoggedInUser().getEmail(), movie_id);
                        } catch (Exception e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                        response.sendRedirect("/watchlist");
                        break;
                    case "like":
                        try {
                            iemdbSystem.voteComment(iemdbSystem.getLoggedInUser().getEmail(), Integer.valueOf(comment_id),1);
                            requestDispatcher.forward(request, response);
                        } catch (Exception e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                        break;
                    case "dislike":
                        try {
                            iemdbSystem.voteComment(iemdbSystem.getLoggedInUser().getEmail(), Integer.valueOf(comment_id),-1);
                            requestDispatcher.forward(request, response);
                        } catch (Exception e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                        break;
                    case "comment":
                        try {
                            iemdbSystem.addComment(movie_id,comment_txt);
                            requestDispatcher.forward(request, response);
                        } catch (MovieNotFoundError e) {
                            HttpSession session = request.getSession(false);
                            session.setAttribute("errorText", e.getMessage());
                            response.sendRedirect("/error");
                        }
                        break;
                }
            }
        }
    }
}
