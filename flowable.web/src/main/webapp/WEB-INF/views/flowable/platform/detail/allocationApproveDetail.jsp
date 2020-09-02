<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>资产调拨申请单</title>
    <jsp:include page="../../../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetAllocationView.js"></script>
    <script type="text/javascript" src="resources/js/flowable/approveCommon.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommonIcon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }

        .background_color {
            background-color: #F8F9F9;
            line-height: 42px;
            text-align: center;
        }

        .td {
            line-height: 42px;
            text-align: center;
        }
    </style>
</head>

<script type="text/javascript">
    $(function () {
        $("#processDefinitionKey").val(ASSETS_TRANSFER);
    })
</script>

<body>
<input id="AssetAllocationID" value="${AssetAllocationDto.id }" type="hidden"/>
<input id="AssetAllocationCode" value="${AssetAllocationDto.assetAllocationCode }" type="hidden"/>
<input id="CreateUserID" value="${AssetAllocationDto.createUserID }" type="hidden"/>

<jsp:include page="../../common/approveHead.jsp"></jsp:include>

<div id="baseInfo" style="margin-top: 70px;">
    <span class="span-title">基础信息</span>
    <div>
        <table class="table table-bordered" style="margin-bottom: 0px;">
            <tr>
                <td class="background_color">调拨单号</td>
                <td class="td">${AssetAllocationDto.assetAllocationCode }</td>
                <td class="background_color">发起日期</td>
                <td class="td">${AssetAllocationDto.createTimestamp }</td>
                <td class="background_color">发起人</td>
                <td class="td">${AssetAllocationDto.createUserName }</td>
                <td class="background_color">审核状态</td>
                <td class="td">${AssetAllocationDto.receiptStatus }</td>
            </tr>
            <tr>
                <td class="background_color">调出部门</td>
                <td class="td">${AssetAllocationDto.callOutDepartmentName }</td>
                <td class="background_color">调入部门</td>
                <td class="td">${AssetAllocationDto.callInDepartmentName }</td>
                <td class="background_color">调入部门资产管理员</td>
                <td class="td">${AssetAllocationDto.callInAssetManagerName }</td>
                <td class="background_color"></td>
                <td class="td"></td>
            </tr>
            <tr>
                <td class="background_color">调入位置</td>
                <td colspan="7">${AssetAllocationDto.callInSavePlaceStr }</td>
            </tr>
            <tr style="height:80px;">
                <td class="background_color">调拨原因</td>
                <td colspan="7">${AssetAllocationDto.reason }</td>
            </tr>
        </table>
    </div>
    <div id="assetList" style="margin-top: 5px;">
        <span class="span-title">变更资产清单</span>
        <hr class="hr-css"/>
        <div class="container-fluid">
            <div style="margin-top: 5px;">
                <table id="assetAllocationTempDataGridTable"></table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../common/approveFoot.jsp"></jsp:include>
</body>
</html>