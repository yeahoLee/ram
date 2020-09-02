<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>盘点单编辑</title>
    <jsp:include page="../../../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/myAssetInventoryView.js"></script>
    <script type="text/javascript" src="resources/js/flowable/approveCommon.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommonIcon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }

        .background_color {
            background-color: #F8F9F9;
            line-height: 25px;
            text-align: center;
        }

        .td {
            line-height: 25px;
            text-align: center;
        }
    </style>
</head>

<script type="text/javascript">
    $(function () {
        $("#processDefinitionKey").val(ASSETS_INVENTORY_RESULT);
    })
</script>

<body>
<input id="UserID" value="${userInfoDto.id }" type="hidden"/>
<input id="ChsName" value="${userInfoDto.chsName }" type="hidden"/>
<input id="DeptID" value="${managerDeptId}" type="hidden"/>
<input id="AssetInventoryId" value="${assetInventoryDto.id }" type="hidden"/>
<input id="MyAssetInventoryId" value="${myAssetInventoryDto.id }" type="hidden"/>

<jsp:include page="../../common/approveHead.jsp"></jsp:include>

<div id="baseInfo" style="margin-top: 70px;">
    <span class="span-title">基础信息</span>
    <div>
        <table class="table table-bordered" style="margin-bottom: 0px;">
            <tr>
                <td class="background_color" style="width: 10%;">盘点任务编号</td>
                <td class="td" style="width: 15%;">${assetInventoryDto.inventoryRunningNum }</td>
                <td class="background_color" style="width: 10%;">盘点任务名称</td>
                <td class="td" style="width: 25%;" colspan="3">${assetInventoryDto.inventoryName }</td>
                <td class="background_color" style="width: 10%;">发起日期</td>
                <td class="td" style="width: 25%;">${assetInventoryDto.lanuchDateStr }</td>
            </tr>
            <tr style="height: 80px;">
                <td class="background_color">盘点任务说明</td>
                <td colspan="7">${assetInventoryDto.reason }</td>
            </tr>
            <tr>
                <td class="background_color">盘点单编号</td>
                <td class="td">${myAssetInventoryDto.myAssetInventoryCode }</td>
                <td class="background_color">盘点资产数量</td>
                <td class="td">
                    <fmt:formatNumber type="number" value="	${myAssetInventoryDto.quantity }" maxFractionDigits="0"/>
                </td>
                <td class="background_color" style="width: 10%;">资产管理员</td>
                <td class="td">${myAssetInventoryDto.managerStr }</td>
                <td class="background_color">盘点单状态</td>
                <td class="td">${myAssetInventoryDto.myinventoryStatus }</td>
            </tr>
            <tr>
                <td class="background_color">盘亏</td>
                <td class="td">
                    <fmt:formatNumber type="number" value="	${myAssetInventoryDto.inventoryLoss}"
                                      maxFractionDigits="0"/>
                </td>
                <td class="background_color">盘盈</td>
                <td class="td">
                    <fmt:formatNumber type="number" value="${myAssetInventoryDto.inventoryProfit }"
                                      maxFractionDigits="0"/>
                </td>
                <td class="background_color"></td>
                <td class="td"></td>
                <td class="background_color"></td>
                <td class="td"></td>
            </tr>
            <tr>
                <td class="background_color">盘点结果报告</br>盘亏盘盈原因：</td>
                <td class="td" colspan="7">${myAssetInventoryDto.reason }</td>
            </tr>
            <tr>
                <td class="background_color">处置意见：</td>
                <td class="td" colspan="7">${myAssetInventoryDto.disposalAdvice }</td>
            </tr>
        </table>
    </div>

    <div id="assetList" style="margin-top: 5px;">
        <span class="span-title">盘点清单</span>
        <hr class="hr-css"/>
        <div class="container-fluid">
            <div style="margin-top: 5px;">
                <table id="assetInventoryDataGrid"></table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../../common/approveFoot.jsp"></jsp:include>

</body>
</html>