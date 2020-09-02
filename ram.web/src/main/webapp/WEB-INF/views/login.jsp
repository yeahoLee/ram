<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1">
    <title>常州实物资产管理系统</title>
    <link rel="shortcut icon" href="resources/css/login/favicon.ico"
          type="image/x-icon"/>

    <link rel="stylesheet" type="text/css"
          href="resources/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="resources/easyui/themes/icon.css">
    <link rel="stylesheet" type="text/css"
          href="resources/css/login/common.css">

    <script type="text/javascript"
            src="resources/ace/js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript"
            src="resources/easyui/jquery.easyui.min.js"></script>
    <!-- <script type="text/javascript" -->
    <!-- 	src="resources/js/user/login.js"></script> -->
    <script type="text/javascript"
            src="resources/easyui/locale/easyui-lang-zh_CN.js"></script>
    <link href="resources/css/login/login.css" rel="stylesheet"
          type="text/css"/>
</head>
<body id="login">
<form id="loginForm" method="POST" action="login">
    <div class="aspNetHidden"></div>
    <div class="aspNetHidden"></div>
    <div class="container">
        <div class="header">
            <div class="header-item"></div>
        </div>
        <div class="content">
            <table cellpadding="0" cellspacing="0">
                <thead>
                <tr>
                    <th></th>
                    <td style="vertical-align: top; padding-top: 16px;">
                        请输入您的用户名和密码登录<br/><font color="#ff8000" style="font-weight: bold">常州实物资产管理系统</font><br/>
	                        <c:if test="${param.error==true }">
					           <span id="wrong" style="color: red;"> 用户名或密码错误,请重试!</span>
							</c:if>
                        <h2></h2>
                    </td>
                </tr>
                </thead>
                <tr>
                    <th>用户名：</th>
                    <td><input type="text" id="username" name="username" class="easyui-validatebox"
                               data-options="required:true"></td>
                </tr>
                <tr>
                    <th>密&nbsp;&nbsp;&nbsp;&nbsp;码：</th>
                    <td>
                    	<input type="password" id="password" name="password">
                    </td>
                </tr>
                <tr>
                	<th></th>
                    <td>
                    	<span id="checkpwd" style="color: red;"> </span>
                    </td>
                </tr>
                <tfoot>
                <tr>
                    <th></th>
                    <td><input type="submit" name="btnLogin" value="登录"
                               id="btnLogin" class="inputbtn"/></td>
                </tr>
                </tfoot>
            </table>
        </div>
        <div class="footer">
            <p>Copyright 2019 常州市轨道交通发展有限公司 All Rights Reserved</p>
        </div>
        <div id="extraDiv1">
            <span></span>
        </div>
        <div id="extraDiv2" style="width: 0px; left: 490px;">
            <span></span>
        </div>
    </div>
    <script type="text/javascript">
    $('#loginForm').submit(function() { //当提交表单时，会发生 submit 事件。
    	var username = $('#username').val();
    	var password = $('#password').val();
    	if(username==null || username=="" || password==null || password==""){
    		alert("用户名或密码不能为空！");
    		return false;
    	}
    	
    	return true;
    });
    
    //$("#password").keyup(function(event){//.change(function(){
    //	var password = $('#password').val();
    //	reg=/^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,}$/;
    //	if(!reg.test(password)){
   	//	  	$('#checkpwd').html("密码强度弱，请登录成功后修改密码！");
    //	}else{
    //		$('#checkpwd').html("");
    //	}
    //});
    
    </script>
</form>
</body>
</html>
