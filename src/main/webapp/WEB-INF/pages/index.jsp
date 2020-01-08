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
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="css/all.css"/>
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript" src="js/bootstrap-paginator.js"></script>
</head>
<body>
<span style="color: red">${msg}</span><br>
<input type="hidden" id="sumPages" value="${sumPages}">
<span>${user.name}，欢迎登录</br></span>
<c:if test="${permission.readall}">
<a href="/toAllExpire ">过期文件</a>
<a href="/toCompany">分配公司</a>
</c:if>
<a href="/index.do">刷新</a>
<a href="/index.jsp">去登录页</a>
<c:if test="${permission.readall}">
<form action="/createCompany.do" method="post">
<input type="text" name="name">
<input type="submit" value="新建公司">
</form>
</c:if>
<div>
公司列表<br>
    <div>
        <div id="page">
        </div>
    </div>
    <div id="company_list">
<c:forEach  items="${companies}" var="company">
    <a href="toFolder.do?company_id=${company.id}&fway_id=0">${company.name}</a><br>
</c:forEach>
    </div>
</div>
</body>
<script type="text/javascript">
    $(function(){
        var sumPages = $("#sumPages").val();
        var company_list =  $("#company_list");
        var options={
            bootstrapMajorVersion:1,
            currentPage:1,    //当前页数
            numberOfPages:sumPages>5? 5:sumPages,  //显示页数
            totalPages:sumPages,   //显示总数
            onPageClicked:function(e,originalEvent,type,page){

                $.ajax({
                    url:"indexAJAX.do",
                    type:"post",
                    success:function (datas) {
                        $("#company_list").html("")
                        for (var i = 0; i < datas.length ; i++) {
                            var a =$("<a>");
                            a.attr("href","toFolder.do?company_id="+datas[i].id+"&fway_id=0");
                            a.html(datas[i].name);
                            company_list.append(a);
                            company_list.append($("<br>"));
                        }
                    },
                    data:{start:(page-1)*20},
                })
            }
        }
        $("#page").bootstrapPaginator(options);
    })
</script>
</html>
