<%--
  Created by IntelliJ IDEA.
  User: atieharmin
  Date: 3/12/2022 AD
  Time: 19:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="IEMDBClasses.IEMDBSystem" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<body>
<ul>
    <li id="email">email: <%= IEMDBSystem.getInstance().getLoggedInUser().getEmail() %></li>
    <li>
        <a href="/movies">Movies</a>
    </li>
    <li>
        <a href="/watchlist">Watch List</a>
    </li>
    <li>
        <a href="/logout">Log Out</a>
    </li>
</ul>
</body>
</html>
