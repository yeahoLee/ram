<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>资产借用申请单</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetBorrowView.js"></script>
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
<body>
<input id="AssetBorrowID" value="${AssetBorrowDto.id }" type="hidden"/>
<input id="UserID" value="${userInfoDto.id }" type="hidden"/>
<input id="AssetborrowUserID" value="${AssetBorrowDto.assetborrowUserID }" type="hidden"/>
<input id="ChsName" value="${userInfoDto.chsName }" type="hidden"/>
<input id="AssetborrowCode" value="${AssetBorrowDto.assetborrowCode }" type="hidden"/>
<input id="CreateUserID" value="${AssetBorrowDto.createUserID }" type="hidden"/>
<div style="height:50px;width:100%;padding-top:0px;padding-left:10px;text-align:left;position:fixed;top:0px;left:0px;background:white;">
    <button class="btn btn-primary" onclick="location='./assetborrow_query'">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button>
</div>
<div align="center" style="font-weight:blod;font-size:20px;padding:5px;margin-top:50px;">资产借用申请单</div>
<div id="baseInfo" style="margin-top: 70px;">
    <span class="span-title">基础信息</span>
    <div>
        <table class="table table-bordered" style="margin-bottom: 0px;">
            <tr>
                <td class="background_color" style="width:10%;">借用单号</td>
                <td class="td" style="width:23%;">${AssetBorrowDto.assetborrowCode }</td>
                <td class="background_color" style="width:10%;">借用部门</td>
                <td class="td" style="width:23%;">${AssetBorrowDto.assetborrowDepartmentName }</td>
                <td class="background_color" style="width:10%;">借用人</td>
                <td class="td" style="width:23%;">${AssetBorrowDto.assetborrowUserName }</td>
            </tr>
            <tr>
                <td class="background_color">借用日期</td>
                <td class="td">${AssetBorrowDto.createTimestamp }</td>
                <td class="background_color">拟归还日期</td>
                <td class="td">${AssetBorrowDto.revertTimeStr }</td>
                <td class="background_color">审核状态</td>
                <td class="td">${AssetBorrowDto.receiptStatus }</td>
            </tr>
            <tr style="height:80px;">
                <td class="background_color">借用事由</td>
                <td colspan="5">${AssetBorrowDto.reason }</td>
            </tr>
        </table>
    </div>
    <div id="assetList" style="margin-top: 5px;">
        <span class="span-title">借用资产清单</span>
        <hr class="hr-css"/>
        <div class="container-fluid">
            <div style="margin-top: 5px;">
                <table id="assetBorrowTempDataGridTable"></table>
            </div>
        </div>
    </div>

    <jsp:include page="../flowable/common/approveHistory.jsp"></jsp:include>

</div>
</body>
</html>