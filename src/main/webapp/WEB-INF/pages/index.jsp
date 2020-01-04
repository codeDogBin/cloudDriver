<%--
  Created by IntelliJ IDEA.
  User: lb
  Date: 2019/12/30
  Time: 16:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>公司页</title>
</head>
<body>
<span style="color: red">${msg}</span><br>
${user.name}，欢迎登录</br>
<a href="/toAllExpire ">过期文件</a>
<a href="/toCompany">分配公司</a>
<a href="/index.do">刷新</a>

<form action="/createCompany.do" method="post">
<input type="text" name="name">
<input type="submit" value="新建公司">
</form>
<div>
公司列表<br>
<c:forEach  items="${companies}" var="company">
    <a href="toFolder.do?company_id=${company.id}&fway_id=0">${company.name}</a><br>
</c:forEach>
</div>
</body>
</html>
