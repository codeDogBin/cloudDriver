<%--
  Created by IntelliJ IDEA.
  User: lb
  Date: 2019/12/30
  Time: 16:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  isELIgnored="false" pageEncoding="UTF-8" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>登录页</title>
    <script type="text/javascript" src="js/jquery.js"></script>
</head>
<body>
<form action="/login.do" method="post">
登录：<input type="text" name="name" ><br>
密码：<input type="password" name="password"><br>
<input type="submit">${msg}
</form>
</body>
</html>
