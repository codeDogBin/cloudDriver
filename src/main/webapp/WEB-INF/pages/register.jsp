<%--
  Created by IntelliJ IDEA.
  User: lb
  Date: 2019/12/30
  Time: 16:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>注册页</title>

</head>
<body>
<form action="/register.do" method="post">
    登录：<input type="text" name="name" ><br>
    密码：<input type="password" name="password"><br>
    级别：<select name="permission_id">
            <option value="1">超管</option>
            <option value="2">主管</option>
            <option value="3">管理</option>
            <option value="4" selected="selected">客户</option>
        </select><br>
    <input type="submit">${msg}
</form>
</body>
</html>
