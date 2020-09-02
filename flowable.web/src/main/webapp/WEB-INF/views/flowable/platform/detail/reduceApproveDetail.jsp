<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>查看资产减损申请单</title>
    <jsp:include page="../../../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetReduceShow.js"></script>
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
        $("#processDefinitionKey").val(ASSETS_IMPAIRMENT);
    })
</script>
<body>
<input id="AssetSavePlaceChangeId" value="${AssetReduceDto.id}" type="hidden">

<jsp:include page="../../common/approveHead.jsp"></jsp:include>

<div id="baseInfo" style="margin-top: 70px;">
    <hr class="hr-css"/>
    <div class="container-fluid">
        <table class="baseInfo" style="border:1px solid gray;width:100%;">
            <tr>
                <td class="tdColor">资产减损单号</td>
                <td>${AssetReduceDto.changeNum}</td>
                <td class="tdColor">发起日期</td>
                <td>${AssetReduceDto.createTimestamp}</td>
                <td class="tdColor">发起人</td>
                <td>${AssetReduceDto.createUserIdStr}</td>
                <td class="tdColor">审核状态</td>
                <td>${AssetReduceDto.receiptStatusStr}</td>
            </tr>
            <tr>
                <td class="tdColor">资产减损单名称</td>
                <td>${AssetReduceDto.orderName}</td>
                <td class="tdColor">减损类型</td>
                <td>${AssetReduceDto.reduceTypeStr}</td>
                <td class="tdColor">残余价值</td>
                <td>${AssetReduceDto.surplusValue}元</td>
                <td class="tdColor">处理费用</td>
                <td>${AssetReduceDto.processingCost}元</td>
            </tr>
            <tr>
                <td class="tdColor">实际损失</td>
                <td>${AssetReduceDto.actualLoss}元</td>
                <td class="tdColor"></td>
                <td></td>
                <td class="tdColor"></td>
                <td></td>
                <td class="tdColor"></td>
                <td></td>
            </tr>
            <tr>
                <td class="tdColor">减损原因</td>
                <td colspan="7" style="text-align:left;">${AssetReduceDto.reason}</td>
            </tr>
            <tr>
                <td class="tdColor">拟处理办法</td>
                <td colspan="7" style="text-align:left;">${AssetReduceDto.proposedDisposal}</td>
            </tr>
        </table>
    </div>
</div>
<div id="assetList" style="margin-top: 5px;">
    <input id="assetIdList" name="assetIdList" type="hidden" value="${AssetReduceDto.assetIdListStr }"/>
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