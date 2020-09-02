<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetAssetIndex.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <title>固定资产增删改查</title>
</head>
<body>
<table id="dataGridTable"></table>
<div id="dataGridTableButtons">
    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="location='./asset_create'">增加</a>
</div>
</body>
</html>