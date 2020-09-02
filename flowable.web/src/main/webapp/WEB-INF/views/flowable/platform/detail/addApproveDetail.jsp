<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <jsp:include page="../../../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/materialReceiptList.js"></script>
    <script type="text/javascript" src="resources/js/asset/materialReceiptVew.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <script type="text/javascript" src="resources/js/flowable/approveCommon.js"></script>
<%--    <script type="text/javascript" src="resources/js/flowable/single/addApprove.js"></script>--%>
<%--    <script type="text/javascript" src="resources/js/flowable/platform/detail/approveDetailCommon.js"></script>--%>
    <style type="text/css">
        .background_color {
            background-color: #F8F9F9;
            text-align: right;
        }
    </style>
    <title>新增单查看</title>
</head>

<script type="text/javascript">
    $(function () {
        $("#processDefinitionKey").val(ASSETS_ADD);
    })
</script>

<body>
<jsp:include page="../../common/approveHead.jsp"></jsp:include>

<div style="margin-top: 70px;">
    <span class="span-title">工单信息</span>
    <hr class="hr-css"/>
    <table class="table table-bordered" style="margin-bottom: 0px;width: 99%">
        <tr>
            <td class="background_color" style="width:11%;"><i class="fa fa-th-list" style="margin-right: 5px;"></i>资产新增单编号：
            </td>
            <td style="width:11%;text-align: left;">${receiptDto.runningNum }</td>
            <td class="background_color" style="width:11%;"><i class="fa fa-list-alt" style="margin-right: 5px;"></i>资产新增单名称：
            </td>
            <td style="width:12%;text-align: left;">${receiptDto.receiptName }</td>
            <td class="background_color" style="width:8%;"><i class="fa fa-location-arrow"
                                                              style="margin-right: 5px;"></i>来源方式：
            </td>
            <td style="width:11%;text-align: left;">${receiptDto.sourceTypeStr }</td>
            <td class="background_color" style="width:8%;"><i class="fa fa-recycle" style="margin-right: 5px;"></i>审核状态：
            </td>
            <td style="width:11%;text-align: left;">${receiptDto.receiptStatusStr }</td>
        </tr>
        <tr>
            <td rowspan="1" class="background_color" style="width:10%; height:80px;"><i class="fa fa-line-chart"
                                                                                        style="margin-right: 5px;"></i>新增原因：
            </td>
            <td colspan="7" style="width:11%; text-align: left;">${receiptDto.reason }</td>
        </tr>
        <tr>
            <td rowspan="1" class="background_color" style="width:10%; height:80px;"><i class="fa fa-flag"
                                                                                        style="margin-right: 5px;"></i>备注：
            </td>
            <td colspan="7" style="width:11%; text-align: left;">${receiptDto.remark }</td>
        </tr>
    </table>
</div>
<div style="margin-top: 15px;">
    <span class="span-title">资产清单</span>
    <hr class="hr-css"/>
    <input id="receiptDtoId" type="hidden" value="${receiptDto.id }">
    <table id="dataGridTable"></table>
</div>

<jsp:include page="../../common/approveFoot.jsp"></jsp:include>

</body>
</html>