<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="resources/css/error404.css" rel="stylesheet" type="text/css">
    <title>错误页面</title>
    <!-- <style type="text/css">
        body{
            font-family: "华文细黑";
            background:url("resources/img/blueprint.png") no-repeat;
            background-size: 100%;
        }
    </style> -->
</head>
<body>
<div class="error404">
    <div class="info">
        <h1>404</h1>
        <h2>抱歉，${errorMessage }</h2>
        <a href="javascript:void(0);" class="btn">返回主页</a>
    </div>
</div>
</body>
</html>