<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>查看资产管理员变更申请单</title>
    <jsp:include page="../../../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetManagerChangeShow.js"></script>
    <script type="text/javascript" src="resources/js/flowable/approveCommon.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommonIcon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }

        .baseInfo td {
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: grey;
            /* background-color: #ffffff; */
            width: 12.5%;
            text-align: center;
            font-family: '宋体 常规', '宋体';
            font-weight: 400;
            font-style: normal;
            font-size: 12px;
        }

        .tdColor {
            background-color: #E6E6FA;
        }
    </style>
</head>
<script type="text/javascript">
    $(function () {
        $("#processDefinitionKey").val(ASSETS_USER_LOCATION_CHANGE);
    })
</script>
<body>
<input id="AssetManagerChangeId" value="${AssetManagerChangeDto.id}" type="hidden">
<div align="center" style="font-weight:blod;font-size:20px;padding:5px;margin-top:50px;">资产管理员变更申请单</div>

<jsp:include page="../../common/approveHead.jsp"></jsp:include>

<div id="baseInfo" style="margin-top: 70px;">
    <hr class="hr-css"/>
    <div class="container-fluid">
        <table class="baseInfo" style="border:1px solid gray;width:100%;">
            <tr>
                <td class="tdColor">变更单号</td>
                <td>${AssetManagerChangeDto.changeNum}</td>
                <td class="tdColor">发起日期</td>
                <td>${AssetManagerChangeDto.createTimestamp}</td>
                <td class="tdColor">发起人</td>
                <td>${AssetManagerChangeDto.createUserIdStr}</td>
                <td class="tdColor">审核状态</td>
                <td>${AssetManagerChangeDto.receiptStatusStr}</td>
            </tr>
            <tr>
                <td class="tdColor">原资产管理员</td>
                <td>${AssetManagerChangeDto.oldAssetManagerStr}</td>
                <td class="tdColor">新资产管理员</td>
                <td>${AssetManagerChangeDto.assetManagerStr}</td>
                <td class="tdColor"></td>
                <td></td>
                <td class="tdColor"></td>
                <td></td>
            </tr>
            <tr>
                <td class="tdColor">变更原因</td>
                <td colspan="7" style="text-align:left;">${AssetManagerChangeDto.reason}</td>
            </tr>
        </table>
    </div>
</div>
<div id="assetList" style="margin-top: 5px;">
    <input id="assetIdList" name="assetIdList" type="hidden" value="${AssetManagerChangeDto.assetIdListStr }"/>
    <span class="span-title">变更资产清单</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div style="margin-top: 5px;">
            <table id="batchUpdateDataGridTable"></table>
        </div>
    </div>
</div>
<jsp:include page="../../common/approveFoot.jsp"></jsp:include>
</body>
</html>