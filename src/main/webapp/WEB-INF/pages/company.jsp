<%--
  Created by IntelliJ IDEA.
  User: lb
  Date: 2020/1/2
  Time: 14:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>用户绑定公司</title>

    <script type="text/javascript" src="js/jquery.js"></script>
    <script>
        var user_id = -1;
        $(function () {
            function shuaxin(user_id){
                $.ajax({
                    type: "post",
                    url:"findUserCompanyAJAX.do",
                    success:function (result) {
                        result
                        for (var i = 0; i < result.length ; i++) {
                            // i==0?div=$(".userCompany"):div=$(".company")
                            if(i===0){
                                var div = $("#userCompany");
                                div.html("");
                            }
                            for (var j = 0; j < result[i].length ; j++) {
                                var temp = result[i][j];
                                console.log(temp);
                                var input = $('<input>');
                                $(input).attr('type','checkbox');
                                $(input).attr('name','companies_id');
                                $(input).attr('value',temp.id);
                                if(i===0){
                                    $(input).attr('checked',true);
                                }
                                div.append(input);
                                div.append(temp.name);
                                div.append($('<br>'))
                            }
                        }
                    },
                    data:{"user_id":user_id},
                });
            }

            //下拉框的点击事件
            $("#user_id").on("change",function shu() {
                user_id = $("#user_id").val();//获取id
                if(user_id == -1 ){
                    return;
                }
                shuaxin(user_id);
            });
            //查询公司的点击事件
             $("#selectCompany").on("click",function () {
                 user_id = $("#user_id").val();
                 var name = $("#company_name").val();
                 $.ajax({
                     type:"post",
                     url:"findCompanyByNameAJAX.do",
                     success:function (result) {
                         var div = $("#company");
                         div.html("");
                         for (var i = 0; i < result.length ; i++) {
                             var temp = result[i];
                             console.log(temp);
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
                user_id = $("#user_id").val();
                var companies = $("#company input[name='companies_id']:checked(:checked)");
                var companies_id = [];
                $.each(companies,function(index,ele){
                   var num = parseInt($(ele).val());
                    companies_id.push(num);
                });
                console.log(companies_id);
                $.ajax({
                    type:"post",
                    url:"bindUserCompanyAJAX.do",
                    success:function (result) {
                        alert(result);
                        shuaxin(user_id);
                    },
                    data:{"user_id":user_id,"companies_id":companies_id},
                    // contentType: 'application/json',
                    traditional: true
                });
            });
            //解绑的点击事件
            $("#unbind").on("click",function () {
                user_id = $("#user_id").val();
                var companies = $("#userCompany input[name='companies_id']:not(:checked)");
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
                        shuaxin(user_id);
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
<span style="color: red">${msg}</span>
<a href="/index.do">去主页</a>
绑定的用户：<select id="user_id" name="user_id">
    <option value="-1">请选择</option>
<c:forEach items="${users}" var="user">
    <option  value="${user.id}" onchange="shu()">${user.name}</option>
</c:forEach>
</select><br>

    <div>已绑定公司<div id="userCompany"></div></div>
    <input type="button" id="unbind" value="解除绑定"><br>

    <div>
    输入公司名称<br>
    <input type="text"  id="company_name" ><br>
    <input type="button" id="selectCompany" value="查询">
    </div>
    <div >未绑定公司<div id="company"></div></div>
    <input type="button" id="bind" value="绑定">


</body>

</html>
