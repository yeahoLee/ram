<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="cn">
<head>
    <link rel="stylesheet" href="resources/ace/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="resources/css/localLogin.css"/>
</head>
<body>
<div class="container">
    <form class="form-signin" method="POST" action="login">
        <h2 class="form-signin-heading">常州实物资产管理系统</h2>
        <p>
            <label for="username" class="sr-only">用户名</label> <input
                type="text" id="username" name="username" class="form-control"
                placeholder="Username" required="" autofocus="">
        </p>
        <p>
            <label for="password" class="sr-only">密码</label> <input
                type="password" id="password" name="password" class="form-control"
                placeholder="Password" required="">
        </p>
        <button class="btn btn-lg btn-primary btn-block" type="submit">登 录</button>
    </form>
</div>
</body>
</html>