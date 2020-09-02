<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>查看资产使用人变更申请单</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetUserChangeShow.js"></script>
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
<body>
<div style="height:50px;width:100%;padding-top:0px;padding-left:10px;text-align:left;position:fixed;top:0px;left:0px;background:white;z-index:9999">
    <button class="btn btn-primary" style="margin-left: 5px;" onclick="location='./asset_user_change_list'">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button>
</div>
<input id="AssetUserChangeId" value="${AssetUserChangeDto.id}" type="hidden">
<div align="center" style="font-weight:blod;font-size:20px;padding:5px;margin-top:50px;">使用人变更申请单</div>
<div id="baseInfo" style="margin-top: 70px;">
    <hr class="hr-css"/>
    <div class="container-fluid">
        <table class="baseInfo" style="border:1px solid gray;width:100%;">
            <tr>
                <td class="tdColor">变更单号</td>
                <td>${AssetUserChangeDto.changeNum}</td>
                <td class="tdColor">发起日期</td>
                <td>${AssetUserChangeDto.createTimestamp}</td>
                <td class="tdColor">发起人</td>
                <td>${AssetUserChangeDto.createUserIdStr}</td>
                <td class="tdColor">审核状态</td>
                <td>${AssetUserChangeDto.receiptStatusStr}</td>
            </tr>
            <tr>
                <td class="tdColor">新使用人</td>
                <td>${AssetUserChangeDto.assetUserStr}</td>
                <td class="tdColor"></td>
                <td></td>
                <td class="tdColor"></td>
                <td></td>
                <td class="tdColor"></td>
                <td></td>
            </tr>
            <tr>
                <td class="tdColor">变更原因</td>
                <td colspan="7" style="text-align:left;">${AssetUserChangeDto.reason}</td>
            </tr>
        </table>
    </div>
</div>
<div id="assetList" style="margin-top: 5px;">
    <input id="assetIdList" name="assetIdList" type="hidden" value="${AssetUserChangeDto.assetIdListStr }"/>
    <span class="span-title">变更资产清单</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div style="margin-top: 5px;">
            <table id="batchUpdateDataGridTable"></table>
        </div>
    </div>
</div>

<jsp:include page="../flowable/common/approveHistory.jsp"></jsp:include>

</body>
</html>