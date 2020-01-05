<%--
  Created by IntelliJ IDEA.
  User: lb
  Date: 2019/12/31
  Time: 14:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <meta charset="UTF-8">
    <title>文件柜</title>
    <script type="text/javascript" src="js/jquery.js"></script>
    <script>
        function createFolder(){
            var fway_id= $("#fway_id").val();
            var company_id = $("#company_id").val();
            var name = prompt("请输入文件夹名字:", "新建文件夹");
            if(name === null){
                return;
            }
            var xhr = new XMLHttpRequest();
            xhr.open("post", "/createFolder.do?company_id="+company_id+"&fway_id="+fway_id+"&name="+name);
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                   console.log(xhr.responseText) ;
                   alert(xhr.responseText);
                   window.location.href="/toFolder.do?company_id="+company_id+"&fway_id="+fway_id;
                    }
                }
            xhr.send();
        }
        $(function () {

        })
    </script>
</head>
<body>
${user.name}，欢迎登录
<a href="/toFolder.do?company_id=${company_id}&fway_id=${fway_id}">刷新</a>
<a href="/toIndex">去主(公司)页</a>

 </br>
<%--<input type="file" id="btn_file" name="files" multiple="multiple" >--%>
<%--&lt;%&ndash;<input type="text" id="remark" placeholder="这里输入备注" >&ndash;%&gt;--%>
<%--<button id="upFile" onclick="">确认上传</button>--%>
<%--<button id="noUpFile" onclick="">取消上传</button>--%>
<c:if test="${permission.upload}">

    <button class="levelOne" onclick="createFolder()" >新建文件夹</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

    <form method="post" action="/uploadFile" enctype="multipart/form-data">
            <input type="hidden" id="company_id"name="company_id"  value="${company_id}" >
            <input type="hidden" id="fway_id"name="fway_id" value="${fway_id}"  >
    <c:if test="${fway_id > 0}">
            <input type="file" name="multipartFiles" >
            <input type="submit" value="提交">
    </c:if>
    </form> <br>

</c:if>
<%--<span id="company_id">${f.company_id}</span>--%>
<%--<span id="fway_id">${f.fway_id}</span>--%>
<%--<input id="company_id" value="${f.company_id}" type="hidden">--%>
<%--<input id="fway_id" value="${f.fway_id}" type="hidden">--%>
<div>
    <span style="color: red">${msg}</span><br>
    当前目录：<a href="/toFolder.do?company_id=${company.id}&fway_id=0">${company.name}</a>
    <c:forEach items="${ways}" var="way">
        ><a href="/toFolder.do?company_id=${way.company_id}&fway_id=${way.id}">${way.name}</a>
    </c:forEach>
    <br> 文件夹区<br>
<c:forEach  items="${folders}" var="folder">
    <a href="/toFolder.do?company_id=${folder.company_id}&fway_id=${folder.id}">${folder.name}</a>
    <c:if test="${permission.del}">
        <a href="/expireFolder.do?folder_id=${folder.id}&company_id=${company_id}&fway_id=${fway_id}">删除</a>
    </c:if> <br>
    <br>
</c:forEach>
<c:if test="${fway_id > 0}">
    <br>文件区<br>
<c:forEach  items="${fils}" var="fil">
    <a href="/getFile.do?way=${fil.way}&name=${fil.name}">${fil.name}</a>
    <c:if test="${permission.del}">
        <a href="/expireFile.do?fil_id=${fil.id}&company_id=${company_id}&fway_id=${fway_id}">删除</a>
    </c:if> <br>
</c:forEach>
</c:if>
</div>
</body>
</html>
