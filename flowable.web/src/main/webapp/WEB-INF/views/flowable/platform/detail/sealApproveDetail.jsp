<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>资产封存单</title>
    <jsp:include page="../../../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetSealView.js"></script>
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
        $("#processDefinitionKey").val(FIXED_ASSETS_ARCHIVE_CENTER);
    })
</script>
<body>
<input id="id" value="${assetSeal.id }" type="hidden"/>
<input id="UserID" value="${userInfoDto.id }" type="hidden"/>
<input id="ChsName" value="${userInfoDto.chsName }" type="hidden"/>
<div align="center" style="font-weight:blod;font-size:20px;padding:5px;margin-top:50px;">资产封存申请单</div>

<jsp:include page="../../common/approveHead.jsp"></jsp:include>

<div id="baseInfo" style="margin-top: 70px;">
    <span class="span-title">基础信息</span>
    <div>
        <table class="table table-bordered" style="margin-bottom: 0px;">
            <tr>
                <td class="background_color" style="width:10%;">封存单号</td>
                <td class="td" style="width:15%;">${assetSeal.sequestrateNum }</td>
                <td class="background_color" style="width:10%;">封存日期</td>
                <td class="td" style="width:15%;">${assetSeal.launchDateStr }</td>
                <td class="background_color" style="width:10%;">发起人</td>
                <td class="td" style="width:15%;">${assetSeal.sponsorStr }</td>
                <td class="background_color" style="width:10%;">审核状态</td>
                <td class="td" style="width:15%;">${assetSeal.sealApproveStatus }</td>
            </tr>
            <tr style="height:80px;">
                <td class="background_color">封存事由</td>
                <td colspan="5">${assetSeal.sealReason }</td>
            </tr>
        </table>
    </div>
    <div id="assetList" style="margin-top: 5px;">
        <span class="span-title">封存资产清单</span>
        <hr class="hr-css"/>
        <div class="container-fluid">
            <div style="margin-top: 5px;">
                <table id="assetSealDataGridTable"></table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../common/approveFoot.jsp"></jsp:include>

</body>
</html>