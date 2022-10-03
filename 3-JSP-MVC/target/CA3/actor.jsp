<%--
  Created by IntelliJ IDEA.
  User: atieharmin
  Date: 3/12/2022 AD
  Time: 19:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><!DOCTYPE html>
<%@ page import="IEMDBClasses.Actor" %>
<%@ page import="IEMDBClasses.IEMDBSystem" %>
<%@ page import="IEMDBClasses.Movie" %>
<%@ page import="java.util.HashMap" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Actor</title>
    <style>
        li {
            padding: 5px
        }
        table{
            width: 40%;
            text-align: center;
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<p id="email">email: <%= IEMDBSystem.getInstance().getLoggedInUser().getEmail() %></p>
<%
    Actor actor = (Actor) request.getAttribute("actor");
    HashMap<Integer, Movie> movies = IEMDBSystem.getInstance().getActorMovies(actor.getId());
%>

<ul>
    <li id="name">name: <%=actor.getName()%> </li>
    <li id="birthDate">birthDate: <%=actor.getBirthday()%></li>
    <li id="nationality">nationality: <%=actor.getNationality()%></li>
    <li id="tma">Total movies acted in: <%=movies.size()%></li>
</ul>
<table>
    <tr>
        <th>Movie</th>
        <th>imdb Rate</th>
        <th>rating</th>
        <th>page</th>
    </tr>
    <%
        for(HashMap.Entry<Integer, Movie> movie : movies.entrySet()) {
    %>
    <tr>
        <td><%=movie.getValue().getName()%></td>
        <td><%=movie.getValue().getImdbRate()%></td>
        <%
            String rating;
            try { rating = String.valueOf(movie.getValue().getRatingsAverage()); }
            catch (ArithmeticException e) { rating = "null"; }
        %>
        <td><%=rating%></td>
        <td><a href="<%= "/movies/" + movie.getValue().getId()%>">Link</a></td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
