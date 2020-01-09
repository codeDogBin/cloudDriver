<%--
  Created by IntelliJ IDEA.
  User: lb
  Date: 2020/1/2
  Time: 14:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
<title>用户绑定公司</title>

    <script type="text/javascript" src="js/jquery.js"></script>
    <script>
        var user_id = -1;
        //选择用户调用方法
        function x() {
            user_id = $("#UserBox input[name=user_id]:checked").val();
            shuaXin(user_id);
        }
        //刷新用户的公司
        function shuaXin(user_id){
            $.ajax({
                type: "post",
                url:"findUserCompanyAJAX.do",
                success:function (result) {
                    var div = $("#userCompany");
                    if(result[0].length == 0 ){
                        var $span = $("<span>");
                        div.html("当前客户没有绑定公司");
                        div.style = "color:red";
                        return;
                    }
                    for (var i = 0; i < result.length ; i++) {
                        // i==0?div=$(".userCompany"):div=$(".company")
                        if(i===0){
                            div.html("");
                        }
                        for (var j = 0; j < result[i].length ; j++) {
                            var temp = result[i][j];
                            var input = $('<input>');
                            $(input).attr('type','checkbox');
                            $(input).attr('name','companies_id');
                            $(input).attr('value',temp.id);
                            if(i===0){
                                $(input).attr('checked',true);
                            }
                            div.append(input);
                            div.append(temp.name);
                            div.append($('<br>'));
                        }
                    }
                },
                data:{"user_id":user_id},
            });
        }
        $(function () {
                //刷新当前页面的按钮


            //下拉框的点击事件
            // $("#user_id").on("change",function shu() {
            //     user_id = $("#user_id").val();//获取id
            //     if(user_id == -1 ){
            //         return;
            //     }8
            //     shuaXin(user_id);
            // });

            //查询公司的点击事件
             $("#selectCompany").on("click",function () {
                 // user_id = $("#user_id").val();
                 user_id = $("#UserBox input[name=user_id]:checked").val();
                 if(user_id == undefined) {
                     alert("没有勾选用户");
                     return;
                 }
                 var name = $("#company_name").val();
                 $.ajax({
                     type:"post",
                     url:"findCompanyByNameAJAX.do",
                     success:function (result) {
                         var div = $("#company");
                         div.html("");
                         for (var i = 0; i < result.length ; i++) {
                             var temp = result[i];
                             var input = $('<input>');
                             $(input).attr('type','checkbox');
                             $(input).attr('name','companies_id');
                             $(input).attr('value',temp.id);
                             div.append(input);
                             div.append(temp.name);
                             div.append($('<br>'))
                         }
                     },
                     data:{"user_id":user_id,"name":name},
                     traditional:true
                 });
             });

            // 绑定的点击事件
            $("#bind").on("click",function () {
                // user_id = $("#user_id").val();
                user_id = $("#UserBox input[name=user_id]:checked").val();
                if(user_id == undefined) {
                    alert("没有勾选用户");
                    return;
                }
                var companies = $("#company input[name='companies_id']:checked(:checked)");
                if(companies.length==0){
                    alert("没有勾选公司，请先查询公司，然后勾选需要绑定的公司");
                    return;
                }
                var companies_id = [];
                $.each(companies,function(index,ele){
                   var num = parseInt($(ele).val());
                    companies_id.push(num);
                });
                $.ajax({
                    type:"post",
                    url:"bindUserCompanyAJAX.do",
                    success:function (result) {
                        alert(result);
                        shuaXin(user_id);
                    },
                    data:{"user_id":user_id,"companies_id":companies_id},
                    // contentType: 'application/json',
                    traditional: true
                });
            });
            //解绑的点击事件
            $("#unbind").on("click",function () {
                // user_id = $("#user_id").val();
                user_id = $("#UserBox input[name=user_id]:checked").val();
                if(user_id == undefined) {
                    alert("没有勾选用户");
                    return;
                }
                var companies = $("#userCompany input[name='companies_id']:not(:checked)");
               if(companies.length == 0 ){
                   alert("您尚未选择，请需要解绑的公司前的钩去掉");
                   return;
               }
                var companies_id = [];
                $.each(companies,function(index,ele){
                    var num = parseInt($(ele).val());
                    companies_id.push(num);
                });
                $.ajax({
                    type:"post",
                    url:"unbindUserCompanyAJAX.do",
                    success:function (result) {
                        alert(result);
                        shuaXin(user_id);
                    },
                    data:{"user_id":user_id,"companies_id":companies_id},
                    // contentType: 'application/json',
                    traditional: true
                });
            });
        });
    </script>
</head>
<body>
<%--信息提示--%>
<span style="color: red">${msg}</span>
<%--去主页的按钮--%>
<a href="/index.do">去主页</a><br>
<%--选择绑定的用户--%>
<%--绑定的用户：<select id="user_id" name="user_id">--%>
    <%--<option value="-1">请选择</option>--%>
    <%--<c:forEach items="${users}" var="user">--%>
        <%--<option  value="${user.id}" onchange="shu()">${user.name}</option>--%>
    <%--</c:forEach>--%>
    <%--</select><br>--%>
    查询用户<input type="text" id="userName" name="" >
    <input type="button" onclick="selectUser()" value="查询"><br>
    用户列表<div id="UserBox"></div>
<br>
<%--已经绑定的公司--%>
    <div>已绑定公司<div id="userCompany"></div></div>
    <input type="button" id="unbind" value="解除绑定"><br>
<%--未绑定的公司--%>
    <div>
    输入公司名称<br>
    <input type="text"  id="company_name" ><br>
    <input type="button" id="selectCompany" value="查询">
    </div>
    <div >未绑定公司<div id="company"></div></div>
    <input type="button" id="bind" value="绑定">
</body>
<script>
    function selectUser() {
        var userName = $("#userName").val();
        var UserBox = $("#UserBox");
        $.ajax({
            type:"post",
            url:"findUserByNameAJAX.do",
            data:{"userName":userName},
            success:function (result) {
                UserBox.html("");
                for (var i = 0; i < result.length ; i++) {
                    var $input = $("<input>");
                    $input.attr("type","radio");
                    $input.attr("name","user_id");
                    $input.attr("value",result[i].id)
                    $input.attr("onclick","x()")
                    UserBox.append($input);
                    UserBox.append(result[i].name);
                    UserBox.append($("<br>"));
                }
            }
        })
    }

</script>
</html>
