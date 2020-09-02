<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/materialReceiptList.js"></script>
    <script type="text/javascript" src="resources/js/asset/materialReceiptVew.js"></script>
    <script type="text/javascript" src="resources/js/flowable/approveCommon.js"></script>
    <script type="text/javascript" src="resources/js/flowable/single/addApprove.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <style type="text/css">
        .background_color {
            background-color: #F8F9F9;
            text-align: right;
        }
    </style>
    <title>新增单查看</title>
</head>
<body>
<div style="height:50px;width:100%;padding-top:0px;padding-left:10px;text-align:left;position:fixed;top:0px;left:0px;background:white;">
    <button class="btn btn-primary" onclick="location='./receiptList'">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button>

    <c:if test="${receiptDto.approveStatus eq 0}">
        <button class="btn btn-primary" onclick="getFirstNode();">
            <i class="fa fa-angle-left" style="margin-right: 5px;"></i>发送
        </button>
    </c:if>
</div>
<div style="margin-top: 70px;">
    <span class="span-title">新增单信息</span>
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

<jsp:include page="../flowable/common/approveHistory.jsp"></jsp:include>

</body>
</html>