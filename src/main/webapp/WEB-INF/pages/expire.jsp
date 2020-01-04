<%--
  Created by IntelliJ IDEA.
  User: lb
  Date: 2020/1/2
  Time: 10:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>过期文件</title>
</head>
<body>
    <a href="/toIndex">去公司页</a><br>
    文件夹区<br>
    <c:forEach items="${folders}" var="folder">
        ${folder.name}<br>
    </c:forEach><br>
    文件区<br>
    <c:forEach items="${fils}" var="fil">
        ${fil.name}<br>
    </c:forEach><br>
</body>
</html>
