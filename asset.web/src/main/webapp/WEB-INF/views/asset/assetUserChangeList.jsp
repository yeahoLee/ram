<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- Standing Book -->
    <title>资产使用人变更清单</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetUserChangeList.js"></script>
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
<input id="createUserID" type="hidden" value="${userInfoDto.id}">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 40px;">
                    <div style="float:left; margin-top: 5px;">
                        变更单号：<input id="changeNum" name="changeNum" class="textbox" style="margin-right: 5px;"/>
                        原使用人： <input id="oldAssetUserId" name="oldAssetUserId" class="easyui-combobox"
                                     data-options="url:'base/user_combo', mode: 'remote', method:'POST'"/>
                        新使用人： <input id="assetUserId" name="assetUserId" class="easyui-combobox"
                                     data-options="url:'base/user_combo', mode: 'remote', method:'POST'"/>
                        变更日期：<input id="createTimestampStart" name="createTimestampStart" class="easyui-datebox"
                                    style="margin-right: 5px;"/>
                        &nbsp;&nbsp;~&nbsp;&nbsp;<input id="createTimestampEnd" name="createTimestampEnd"
                                                        class="easyui-datebox" style="margin-right: 5px;"/>
                    </div>
                    <div style="float:right;">
                        <button class="btn btn-primary" onClick="searchByQuerys();"
                                style="margin-left:5px; margin-right: 5px;">
                            <i class="fa fa-search" style="margin-right: 5px;"></i>搜索
                        </button>
                        <button class="btn btn-primary" onClick="searchByDefaultQuerys();" style="margin-right: 5px;">
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
            <button class="btn btn-primary" onclick="location='asset_user_change_create'"
                    style="margin-bottom: 5px; margin-left: 5px; margin-right: 5px; margin-top: 5px;">
                <i class="fa fa-plus" style="margin-right: 5px;"></i>发起变更
            </button>
        </div>
    </div>
</div>
</body>
</html>