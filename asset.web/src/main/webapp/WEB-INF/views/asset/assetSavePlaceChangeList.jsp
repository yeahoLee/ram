<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- Standing Book -->
    <title>资产安装位置变更清单</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetSavePlaceChangeList.js"></script>
    <script type="text/javascript" src="resources/js/flowable/approveCommon.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommonIcon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 120px;
        }
    </style>
</head>
<body>
<input id="DeptID" value="${loginDeptDto.loginDeptDto.id }" type="hidden"/>
<input id="createUserID" type="hidden" value="${userInfoDto.id}">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 40px;">
                    <div style="float:left; margin-top: 5px;">
                        变更单号：<input id="changeNum" name="changeNum" class="textbox" style="margin-right: 5px;"/>
                        目标位置：
                        <div style="width:500px; text-align: left; padding-left: 15px; background-color: white;display:inline;">
                            <span id="showCodeAndData" style="position:relative;left:-15px"></span>&nbsp;&nbsp;&nbsp;&nbsp;
                            <a onclick="openDialig();" style="padding-right: 15px;">选择位置</a>
                            <input id="assetSavePlaceId" name="asseSavePlaceId" type="hidden">
                        </div>
                        变更日期:<input id="createTimestampStart" name="createTimestampStart" class="easyui-datebox"
                                    style="margin-right: 5px;"/>
                        &nbsp;&nbsp;~&nbsp;&nbsp;<input id="createTimestampEnd" name="createTimestampEnd"
                                                        class="easyui-datebox" style="margin-right: 5px;"/>
                    </div>
                    <div style="float:right;">
                        <button class="btn btn-primary" onClick="searchByQuerys();"
                                style="margin-left:5px; margin-right: 5px;">
                            <i class="fa fa-search" style="margin-right: 5px;"></i>搜索
                        </button>
                        <button class="btn btn-primary" onClick="clearSearch();" style="margin-right: 5px;">
                            <i class="fa fa-refresh" style="margin-right: 5px;"></i>重置搜索
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div data-options="region:'center',border:false">
        <table id="receiptTable"></table>
        <div id="receiptTableButtons">
            <button class="btn btn-primary" onclick="location='asset_savePlace_change_create'"
                    style="margin-bottom: 5px; margin-left: 5px; margin-right: 5px; margin-top: 5px;">
                <i class="fa fa-plus" style="margin-right: 5px;"></i>发起变更
            </button>
        </div>
    </div>
</div>
<!-- 原始安装位置 -->
<div id="instSiteCodeDialog" class="easyui-dialog" title="添加安装位置" data-options="width:880, height:435, closed: false">
    <iframe id="instSiteCodeDialogFrame" class="embed-responsive-item" src="asset_save_place"
            style="width: 100%;height: 100%;" frameborder="no" border="0" marginwidth="0" marginheight="0"
            allowtransparency="yes"></iframe>
</div>
</body>
</html>