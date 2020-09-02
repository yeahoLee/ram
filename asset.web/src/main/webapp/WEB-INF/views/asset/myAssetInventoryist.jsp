<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- AssetBorrowView -->
    <title>我的盘点单</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/myAssetInventoryList.js"></script>
    <script type="text/javascript" src="resources/js/common.js"></script>
    <script type="text/javascript" src="resources/js/flowable/approveCommon.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }
    </style>
</head>
<body>
<input id="DeptID" value="${loginDeptDto.loginDeptDto.id }" type="hidden"/>
<input id="createUserID" type="hidden" value="${userInfoDto.id}">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center', border:false">
        <table id="dataGridTable"></table>
    </div>
</div>
</body>
</html>