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
<%@ page import="java.util.ArrayList" %>
<%@ page import="IEMDBClasses.Actor" %>
<%@ page import="Errors.ActorNotFoundError" %>
<%@ page import="IEMDBClasses.Comment" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Movie</title>
    <style>
        li, td, th {
            padding: 5px;
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<p id="email">email: <%= IEMDBSystem.getInstance().getLoggedInUser().getEmail() %></p>
<% Movie movie = (Movie) request.getAttribute("movie"); %>
<ul>
    <li id="name">name: <%= movie.getName() %></li>
    <li id="summary">summary: <%= movie.getSummary() %></li>
    <li id="releaseDate">releaseDate: <%= movie.getReleaseDate() %></li>
    <li id="director">director: <%= movie.getDirector() %></li>
    <li id="writers">writers: <%= IEMDBSystem.getInstance().convertListOfStringsToString(movie.getWriters()) %></li>
    <li id="genres">genres: <%= IEMDBSystem.getInstance().convertListOfStringsToString(movie.getGenres()) %></li>
    <li id="imdbRate">imdb Rate: <%= movie.getImdbRate() %></li>
    <%
        String rate =  null;
        try {
            rate = String.valueOf(movie.getRatingsAverage());
        }catch (ArithmeticException ignored) {}
    %>
    <li id="rating">rating: <%= rate %></li>
    <li id="duration">duration: <%= movie.getDuration() %></li>
    <li id="ageLimit">ageLimit: <%= movie.getAgeLimit() %></li>
</ul>
<h3>Cast</h3>
<table>
    <tr>
        <th>name</th>
        <th>age</th>
        <th>page</th>
    </tr>
    <%
        IEMDBSystem iemdbSystem = IEMDBSystem.getInstance();
        ArrayList<Integer> cast_ids = movie.getCast();
        for(Integer cast_id : cast_ids){
            Actor actor = null;
            try {
                actor = iemdbSystem.getActorById(cast_id);
            } catch (ActorNotFoundError ignored) {}
            assert actor != null;%>
    <tr>
        <td><%= actor.getName() %></td>
        <td><%= actor.getAge() %></td>
        <td><a href="<%= "/actors/" + actor.getId()%>">Link</a></td>
    </tr>
    <%
        }
    %>
</table>
<br>
<br><br>
<h3>Rate This Movie!</h3>
<form action="<%= "/movies/" + movie.getId()%>" method="POST">
    <label>Rate(between 1 and 10):</label>
    <input type="number" name="quantity" min="1" max="10">
    <input type="hidden" name="action" value="rate">
    <input type="hidden" name="movie_id" value="<%=movie.getId()%>">
    <button type="submit">rate</button>
</form>
<br>
<h3>Add This Movie To Your WatchList!</h3>
<form action="<%= "/movies/" + movie.getId()%>" method="POST">
    <input type="hidden" name="action" value="add">
    <input type="hidden" name="movie_id" value="<%=movie.getId()%>">
    <button type="submit">Add to WatchList</button>
</form>
<br />
<table>
    <tr>
        <th>nickname</th>
        <th>comment</th>
        <th></th>
        <th></th>
    </tr>
    <%
        ArrayList<Comment> comments = movie.getComments();
        for(Comment comment : comments){
    %>
    <tr>
        <td>@<%=comment.getUserNickname()%></td>
        <td><%=comment.getText()%></td>
        <td>
            <form action="<%= "/movies/" + movie.getId()%>" method="POST">
                <% HashMap<String, Integer> votes = comment.getVotesCount(); %>
                <label><%=votes.get("like")%></label>
                <input
                        type="hidden"
                        name="comment_id"
                        value="<%=comment.getId()%>"
                />
                <input type="hidden" name="action" value="like">
                <input type="hidden" name="movie_id" value="<%=movie.getId()%>">
                <button type="submit">like</button>
            </form>
        </td>
        <td>
            <form action="<%= "/movies/" + movie.getId()%>" method="POST">
                <label><%=votes.get("dislike")%></label>
                <input
                        type="hidden"
                        name="comment_id"
                        value="<%=comment.getId()%>"
                />
                <input type="hidden" name="action" value="dislike">
                <input type="hidden" name="movie_id" value="<%=movie.getId()%>">
                <button type="submit">dislike</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
</table>
<br><br>
<form action="<%= "/movies/" + movie.getId()%>" method="POST">
    <label>Your Comment:</label>
    <input type="text" name="comment" value="">
    <input type="hidden" name="action" value="comment">
    <input type="hidden" name="movie_id" value="<%=movie.getId()%>">
    <button type="submit">Add Comment</button>
</form>
</body>
</html>

