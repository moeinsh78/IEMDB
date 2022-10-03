<%--
  Created by IntelliJ IDEA.
  User: atieharmin
  Date: 3/12/2022 AD
  Time: 19:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="IEMDBClasses.IEMDBSystem" %>
<%@ page import="IEMDBClasses.Movie" %>
<%@ page import="java.util.*" %>
<%@ page import="IEMDBClasses.User" %>
<%@ page import="Errors.MovieNotFoundError" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>WatchList</title>
    <style>
        li {
            padding: 5px
        }
        table{
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<%
    User logged_in_user = IEMDBSystem.getInstance().getLoggedInUser();
%>
<p id="email">email: <%= logged_in_user.getEmail() %></p>
<ul>
    <li id="name">name: <%= logged_in_user.getName() %></li>
    <li id="nickname">nickname: @<%= logged_in_user.getNickname() %></li>
</ul>
<h2>Watch List</h2>
<table>
    <tr>
        <th>Movie</th>
        <th>releaseDate</th>
        <th>director</th>
        <th>genres</th>
        <th>imdb Rate</th>
        <th>rating</th>
        <th>duration</th>
        <th>Links</th>
        <th></th>
    </tr>
    <%
        HashMap<Integer, Movie> watchlist = new HashMap<>();
        try {
            watchlist = IEMDBSystem.getInstance().getWatchList(logged_in_user.getEmail());
        } catch (Exception ignored) {}
        for (HashMap.Entry<Integer, Movie> movie : watchlist.entrySet()) {
    %>
    <tr>
        <td><%= movie.getValue().getName() %></td>
        <td><%= movie.getValue().getReleaseDate() %></td>
        <td><%= movie.getValue().getDirector() %></td>
        <td><%= IEMDBSystem.getInstance().convertListOfStringsToString(movie.getValue().getGenres()) %></td>
        <td><%= String.valueOf(movie.getValue().getImdbRate()) %></td>
        <%
            String rating;
            try { rating = String.valueOf(movie.getValue().getRatingsAverage()); }
            catch (ArithmeticException e) { rating = "null"; }
        %>
        <td><%= rating%></td>
        <td><%= String.valueOf(movie.getValue().getDuration()) %></td>
        <td><a href="<%= "/movies/" + movie.getValue().getId()%>">Link</a></td>
        <td>
            <form action="/watchlist" method="POST" >
                <input type="hidden" name="movie_id" value="<%= String.valueOf(movie.getValue().getId()) %>">
                <input type="hidden" name="user_id" value="<%= logged_in_user.getEmail() %>">
                <button type="submit">Remove</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
</table>
<h2>Recommendation List</h2>

<table>
    <tr>
        <th>Movie</th>
        <th>imdb Rate</th>
        <th>Link</th>
    </tr>
    <%
        List<Movie> recom_list = IEMDBSystem.getInstance().getRecomMovies();
        for(Movie movie : recom_list){
    %>
    <tr>
        <th><%=movie.getName()%></th>
        <th><%=movie.getImdbRate()%></th>
        <td><a href="<%= "/movies/" + movie.getId()%>">Link</a></td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>