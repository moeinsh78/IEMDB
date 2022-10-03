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

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Movies</title>
    <style>
        li, td, th {
            padding: 5px;
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<p id="email">email: <%= IEMDBSystem.getInstance().getLoggedInUser().getEmail() %></p>
<br><br>
<form action="movies" method="POST">
    <label>Search:</label>
    <input type="text" name="search" value="">
    <input type="hidden" name="hidden_sort_by_date" value="<%= (String) request.getAttribute("sort_by_date") %>" >
    <button type="submit" name="action" value="search">Search</button>
    <button type="submit" name="action" value="clear">Clear Search</button>
</form>
<br><br>
<form action="movies" method="POST">
    <label>Sort By:</label>
    <input type="hidden" name="hidden_search" value="<%= (String) request.getAttribute("searched_movie") %>" >
    <button type="submit" name="action" value="sort_by_imdb">imdb Rate</button>
    <button type="submit" name="action" value="sort_by_date">releaseDate</button>
</form>
<br>
<table>
    <tr>
        <th>name</th>
        <th>summary</th>
        <th>releaseDate</th>
        <th>director</th>
        <th>writers</th>
        <th>genres</th>
        <th>cast</th>
        <th>imdb Rate</th>
        <th>rating</th>
        <th>duration</th>
        <th>ageLimit</th>
        <th>Links</th>
    </tr>
    <%
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        HashMap<Integer, Movie> movies;

        if(!request.getAttribute("searched_movie").equals("false") && request.getAttribute("clear").equals("false")){
            movies = iemdbSystem.getMoviesByName(request.getAttribute("searched_movie").toString());
        }
        else{
            movies = iemdbSystem.getMoviesList();
        }

        List<Movie> movies_list = movies.values().stream().toList();

        if(request.getAttribute("sort_by_imdb").equals("true") || request.getAttribute("sort_by_date").equals("false")) {
            movies_list = movies_list.stream().sorted(Comparator.comparing(Movie::getImdbRate).reversed()).toList();
        }
        if(request.getAttribute("sort_by_date").equals("true")){
            movies_list = movies_list.stream().sorted(Comparator.comparing(Movie::getReleaseDate).reversed()).toList();
        }

        for (Movie movie_rec : movies_list) {
    %>
    <tr>
        <td><%= movie_rec.getName()%></td>
        <td><%= movie_rec.getSummary()%></td>
        <td><%= movie_rec.getReleaseDate()%></td>
        <td><%= movie_rec.getDirector()%></td>
        <td><%= IEMDBSystem.getInstance().convertListOfStringsToString(movie_rec.getWriters())%></td>
        <td><%= IEMDBSystem.getInstance().convertListOfStringsToString(movie_rec.getGenres())%></td>
        <td><%= IEMDBSystem.getInstance().convertListOfStringsToString(movie_rec.getCastNames())%></td>
        <td><%= String.valueOf(movie_rec.getImdbRate())%></td>
        <%
            String rating;
            try { rating = String.valueOf(movie_rec.getRatingsAverage()); }
            catch (ArithmeticException e) { rating = "null"; }
        %>
        <td><%= rating%></td>
        <td><%= String.valueOf(movie_rec.getDuration())%></td>
        <td><%= String.valueOf(movie_rec.getAgeLimit())%></td>
        <td><a href="<%= "/movies/" + movie_rec.getId()%>">Link</a></td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
