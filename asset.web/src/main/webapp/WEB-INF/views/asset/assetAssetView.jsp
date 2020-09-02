<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>固定资产查看</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetAssetUpdate.js"></script>
    <script type="text/javascript" src="resources/js/asset/assetAssetView.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <style type="text/css">
        .background_color {
            background-color: #F8F9F9;
        }
    </style>
</head>
<body>
<div style="height:50px;width:100%;padding-top:0px;padding-left:10px;text-align:left;position:fixed;z-index:9999;top:0px;left:0px;background:white;">
    <button class="btn btn-primary" onclick="closeThisPage();" style="margin-top:10px;">
        <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
    </button>
    <%-- <button class="btn btn-primary" onclick="javascript:window.location.href = '${backToLastPageUrl}';" style="margin-left: 5px;">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button> --%>
</div>
<input id="assetId" type="hidden" value="${assetDto.id }">
<div style="margin-top: 70px;">
    <table class="table table-bordered" style="margin-bottom: 0px;">
        <tr>
            <th colspan="6">基本信息</th>
        </tr>
        <tr>
            <td class="background_color" style="width:10%;">资产编码：</td>
            <td style="width:23%;">${assetDto.assetCode }</td>
            <td class="background_color" style="width:10%;">资产类别：</td>
            <td style="width:23%;">${assetDto.combinationAssetType }</td>
            <td class="background_color" style="width:10%;">资产名称：</td>
            <td style="width:23%;">${assetDto.combinationAssetName }</td>
        </tr>
        <tr>
            <td class="background_color">资产类型：</td>
            <td>${assetDto.assetTypeStr }</td>
            <td class="background_color">规格型号：</td>
            <td>${assetDto.specAndModels }</td>
            <td class="background_color">序列号：</td>
            <td>${assetDto.seriesNum }</td>
        </tr>
        <tr>
            <td class="background_color">计量单位：</td>
            <td>${assetDto.unitOfMeasStr }</td>
            <td class="background_color">品牌：</td>
            <td>${assetDto.assetBrand }</td>
            <td class="background_color">采购价：</td>
            <td>${assetDto.purcPrice }</td>
        </tr>
        <tr>
            <td class="background_color">资产原值：</td>
            <%-- <td>${assetDto.assetCode }</td> --%>
            <td>${assetDto.equiOrigValue }</td>
            <td class="background_color">月折旧额：</td>
            <td>${assetDto.monthDeprMoney }</td>
            <td class="background_color">残余价值：</td>
            <td>${assetDto.residualValue }</td>
        </tr>
        <tr style="height: 80px;">
            <td class="background_color">技术参数:</td>
            <td colspan="5">${assetDto.techPara }</td>
        </tr>
        <tr style="height: 80px;">
            <td class="background_color">备注:</td>
            <td colspan="5">${assetDto.remark }</td>
        </tr>
        <tr>
            <th colspan="6">延伸信息</th>
        </tr>
        <tr>
            <td class="background_color">所属公司：</td>
            <td>${assetDto.companyStr }</td>
            <td class="background_color">所属线路/建筑：</td>
            <td>${assetDto.belongLineStr }</td>
            <td class="background_color">购置日期：</td>
            <td>${assetDto.buyDate }</td>
        </tr>
        <tr>
            <td class="background_color">主管部门：</td>
            <td>${assetDto.manageDeptStr }</td>
            <td class="background_color">资产管理员：</td>
            <td>${assetDto.managerStr }</td>
            <td class="background_color">位置编码：</td>
            <td>${assetDto.savePlaceCode }</td>
        </tr>
        <tr>
            <td class="background_color">使用部门：</td>
            <td>${assetDto.useDeptStr }</td>
            <td class="background_color">使用人：</td>
            <td>${assetDto.useStr }</td>
            <td class="background_color">位置名称：</td>
            <td>${assetDto.savePlaceName }</td>
        </tr>
        <tr>
            <td class="background_color">资产来源：</td>
            <td>${assetDto.assetSource }</td>
            <td class="background_color">来源方式：</td>
            <td>${assetDto.sourceType }</td>
            <td class="background_color">合同编号：</td>
            <td>${assetDto.contractNum }</td>
        </tr>
        <tr>
            <td class="background_color">联系人：</td>
            <td>${assetDto.sourceUser }</td>
            <td class="background_color">联系方式：</td>
            <td>${assetDto.sourceContactInfo }</td>
            <td class="background_color">标段编号：</td>
            <td>${assetDto.tendersNum }</td>
        </tr>
        <tr>
            <td class="background_color">维保期：</td>
            <td>${assetDto.mainPeriod }</td>
            <td class="background_color">出厂日期：</td>
            <td>${assetDto.prodTime }</td>
            <td class="background_color"></td>
            <td></td>
        </tr>
    </table>
    <div class="easyui-tabs" style="height: 340px;">
        <div title="基础信息变更">
            <table id="dataGridTable0"></table>
        </div>
        <div title="变更记录">
            <table id="dataGridTable1"></table>
        </div>
        <div title="封存/启封记录">
            <table id="dataGridTable2"></table>
        </div>
        <div title="调拨记录">
            <table id="dataGridTable3"></table>
        </div>
        <div title="盘点记录">
            <table id="dataGridTable4"></table>
        </div>
        <div title="新增记录">
            <table id="dataGridTable5"></table>
        </div>
        <div title="减少记录">
            <table id="dataGridTable6"></table>
        </div>
    </div>
</div>
</body>
</html>