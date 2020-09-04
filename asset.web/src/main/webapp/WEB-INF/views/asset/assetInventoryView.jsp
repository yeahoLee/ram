<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>盘点单编辑</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetInventoryView.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommonIcon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }

        .background_color {
            background-color: rgba(240, 244, 249, 1);
            line-height: 25px;
            text-align: center;
            width: 10%;
        }

        .td {
            line-height: 25px;
            text-align: center;
            border: 1px solid rgba(121, 121, 121, 1)
        }
    </style>
</head>
<body>
<input id="UserID" value="${userInfoDto.id }" type="hidden"/>
<input id="ChsName" value="${userInfoDto.chsName }" type="hidden"/>
<input id="AssetInventoryId" value="${assetInventoryDto.id }" type="hidden"/>
<div style="height: 50px; width: 100%; padding-top: 0px; padding-left: 10px; text-align: left; position: fixed; top: 0px; left: 0px; background: white; z-index: 9999">
    <button class="btn btn-primary"
            onclick="location='./assetInventory_query'">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button>
</div>
<div align="center" style="font-weight:blod;font-size:20px;padding:5px;margin-top:50px;">盘点任务申请单</div>
<div id="baseInfo" style="margin-top: 70px;">
    <span class="span-title">基础信息</span>
    <div>
        <table class="table table-bordered" style="border:1px solid gray;width:100%;">
            <tr>
                <td class="background_color">盘点任务编号</td>
                <td class="td" style="width: 10%;">${assetInventoryDto.inventoryRunningNum }</td>
                <td class="background_color">发起日期</td>
                <td class="td" style="width: 10%;">${assetInventoryDto.lanuchDateStr }</td>
                <td class="background_color">发起人</td>
                <td class="td" style="width: 10%;">${assetInventoryDto.createrName }</td>
                <td class="background_color">盘点状态</td>
                <td class="td" style="width: 10%;">${assetInventoryDto.inventoryStatus }</td>
            </tr>
            <tr style="height: 80px;">
                <td class="background_color">盘点任务名称</td>
                <td class="td" style="text-align:left" colspan="3">${assetInventoryDto.inventoryName }</td>
                <td class="background_color">盘点资产数量</td>
                <td class="td" style="width: 10%;">
                    <fmt:formatNumber type="number" value="${assetInventoryDto.quantity }" maxFractionDigits="0"/>
                </td>
                <td class="background_color">物资的类型</td>
                <td>${assetInventoryDto.produceStr }</td>
            </tr>
            <tr style="height: 80px;">
                <td class="background_color">盘点任务说明</td>
                <td colspan="7">${assetInventoryDto.reason }</td>
            </tr>
        </table>
    </div>
    <c:if test="${assetInventoryDto.inventoryStatusStr eq 3}">
    <div id="assetList" style="margin-top: 5px;">
        <span class="span-title">盘点单</span>
        <hr class="hr-css"/>
        <div class="container-fluid">
            <div style="margin-top: 5px;">
                <table id="myAssetInventoryDataGrid"></table>
            </div>
        </div>
    </div>
    </c:if>
    <br/>
    <br/>
    <div id="assetScopeList" style="">
        <span class="span-title">添加盘点范围</span> （若未指定盘点范围同时未附加盘点资产，系统默认盘点当前物资类型下的全部资产,并将其显示在附加资产清单中）
        <hr class="hr-css"/>
        <div class="container-fluid">
            <div style="margin-top:5px;height:auto;">
                <table id="assetInventoryScope"></table>
            </div>
        </div>
    </div>
    <br/>
    <br/>
    <div id="assetLists" style="">
        <span class="span-title">附加盘点资产清单</span>
        <hr class="hr-css"/>
        <div class="container-fluid">
            <div style="margin-top: 5px;">
                <table id="assetInventoryDataGrid"></table>
            </div>
        </div>
    </div>
</div>
</body>
</html>