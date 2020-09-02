<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- Standing Book -->
    <title>新增单</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/materialReceiptList.js"></script>
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
<div id="approveInfo">
    <input name="id" type="hidden">
    <input name="serialNumber" type="hidden">
    <input name="receiptName" type="hidden">
</div>
<input id="UserID" type="hidden" value="${userInfoDto.id}">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 40px;">
                    <div style="float:left; margin-top: 5px;">
                        编号：<input id="runningNum" name="runningNum" class="textbox" style="margin-right: 5px;"/>
                        名称：<input id="receiptName" name="receiptName" class="textbox" style="margin-right: 5px;"/>
                        来源方式:<input id="sourceType" name="sourceType" class="easyui-combobox"
                                    data-options="url:'ram/enum_combo_by_source_type', method:'GET', editable:true"
                                    style="margin-right: 5px; width: 80px;"/>
                        状态：<input id="receiptStatus" name="receiptStatus" class="easyui-combobox"
                                  style="margin-right: 5px; width: 120px;"/>
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
            <button class="btn btn-primary" onclick="location='addReceipt'"
                    style="margin-bottom: 5px; margin-left: 5px; margin-right: 5px; margin-top: 5px;">
                <i class="fa fa-plus" style="margin-right: 5px;"></i>创建资产新增单
            </button>
        </div>
    </div>
</div>
</body>
</html>