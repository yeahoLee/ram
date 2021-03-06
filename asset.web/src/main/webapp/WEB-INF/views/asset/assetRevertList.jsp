<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>资产归还管理</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetRevertList.js"></script>
    <script type="text/javascript" src="resources/js/flowable/approveCommon.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 120px;
        }
    </style>
</head>
<body>
<input id="createUserID" type="hidden" value="${userInfoDto.id}">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 40px;">
                    <div style="float:left; margin-top: 5px;">
                        资产编码：<input name="assetCode" class="textbox"/>
                        资产名称：<input name="assetChsName" class="textbox"/>
                        归还人： <input id="assetrevertUserID" class="easyui-combobox"
                                    data-options="url:'base/user_combo', mode: 'remote', method:'POST'"/>
                        归还日期： <input id="realrevertTimeStart" class="easyui-datebox"/> &nbsp;&nbsp;~&nbsp;&nbsp;
                        <input id="realrevertTimeEnd" class="easyui-datebox"/>
                    </div>
                    <div style="float:right;">
                        <button class="btn btn-primary" onClick="searchByQuerys();"
                                style="margin-left:5px; margin-right: 5px;">
                            <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>搜索
                        </button>
                        <button class="btn btn-primary" onClick="searchByDefaultQuerys();" style="margin-right: 5px;">
                            <i class="fa fa-refresh" style="margin-right: 5px;"></i>重置搜索
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div data-options="region:'center', border:false">
        <table id="dataGridTable"></table>
    </div>
</div>
<!--将按钮放入datagrid中  -->
<div id="btdiv">
    <!-- 	<button class="btn btn-primary" onclick="goToAssetBorrowPage();" style="margin-top: 4px; margin-left: 5px; margin-right: 5px; margin-bottom:5px;"> -->
    <!-- 		<i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>资产借用 -->
    <!-- 	</button> -->
    <button class="btn btn-primary" onclick="goToAssetRevertPage();"
            style="margin-top: 4px; margin-left: 5px; margin-right: 5px; margin-bottom:5px;">
        <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>资产归还
    </button>
</div>
</body>
</html>