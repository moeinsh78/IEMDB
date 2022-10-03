<%--
  Created by IntelliJ IDEA.
  User: atieharmin
  Date: 3/12/2022 AD
  Time: 19:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <style>
        h1 {
            color: rgb(207, 3, 3);
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<h1>
    Error:
</h1>
<br>
<h3>
    <%= request.getAttribute("errorText") %>
</h3>
</body>
</html>
