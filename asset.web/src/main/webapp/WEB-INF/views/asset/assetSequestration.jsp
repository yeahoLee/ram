<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- AssetBorrowView -->
    <title>资产封启管理</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetSequestration.js"></script>
    <script type="text/javascript" src="resources/js/flowable/approveCommon.js"></script>
    <script type="text/javascript" src="resources/js/common.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 120px;
        }
    </style>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 35px;">
                    <div style="float:left; margin-top: 5px;">
                        封存/启封单号：<input name="sequestrateNum" class="textbox" style="margin-right: 5px;"/>
                        封存/启封日期： <input id="createTimestampStart" class="easyui-datebox"/>
                        &nbsp;&nbsp;~&nbsp;&nbsp;<input id="createTimestampEnd" class="easyui-datebox"/>
                    </div>
                    <div style="float:right;">
                        <button class="btn btn-primary" onClick="searchByQuerys();"
                                style="margin-left:5px; margin-right: 5px;">
                            <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>搜索
                        </button>
                        <button class="btn btn-primary" onClick="resetQuerys();"
                                style="margin-left:5px; margin-right: 5px;">
                            <i class="fa fa-refresh align-top bigger-125" style="margin-right: 5px;"></i>重置
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
<div id="asdiv">
    <button class="btn btn-primary" onclick="goToAssetSealPage();"
            style="margin-top: 4px; margin-left: 5px; margin-right: 5px; margin-bottom:5px;">
        <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>资产封存
    </button>
    <button class="btn btn-primary" onclick="goToAssetUnsealPage();"
            style="margin-top: 4px; margin-left: 5px; margin-right: 5px; margin-bottom:5px;">
        <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>资产启封
    </button>
    <%--<button class="btn btn-primary" onclick="goToSealViewPage();"--%>
            <%--style="margin-top: 4px; margin-left: 5px; margin-right: 5px; margin-bottom:5px;">--%>
        <%--<i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>封存视图--%>
    <%--</button>--%>
</div>

</body>
</html>