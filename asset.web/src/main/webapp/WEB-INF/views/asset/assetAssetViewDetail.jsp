<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>物资清单查看</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetAssetViewDetail.js"></script>
    <style type="text/css">
        .background_color {
            background-color: #F8F9F9;
        }
    </style>
</head>
<body>
<input id="materialCode" type="hidden" value="${materialCode}">
<input id="assetCode" type="hidden" value="${assetAssetDto.assetCode}">
<input id="assetIdSet" type="hidden" value="${assetAssetDto.floorNumId}">
<input id="assetChsName" type="hidden" value="${assetAssetDto.assetChsName}">
<input id="assetStatus" type="hidden" value="${assetAssetDto.assetStatus}">
<input id="materialSearchCode" type="hidden" value="${assetAssetDto.materialSearchCode}">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center',border:false,title:'物资清单'">
        <table id="assetDetailTable"></table>
    </div>
</div>
<div id="divbt">
    <%--<div class="btn-group" style="margin-right: 5px; margin-top: 5px; margin-left: 5px; margin-bottom:6px;">
        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
                aria-expanded="false">
            <i class="fa fa-bars align-top bigger-125" style="margin-right: 5px;"></i>快速处理<span class="caret"
                                                                                                style="margin-left: 5px;"></span>
        </button>
        <ul class="dropdown-menu">
            <li><a onclick="goToUpdateBatch();">信息批量修改</a></li>
        </ul>
    </div>--%>
    <!-- <button class="btn btn-primary" onclick="javascript:window.location.href = 'asset_stan_book_material';" style="margin-top: 4px; margin-right: 5px;margin-bottom:5px;">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button> -->
    <button class="btn btn-primary" onclick="backToPrePage()"
            style="margin-top: 4px; margin-right: 5px;margin-bottom:5px;">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button>
</div>
<form id="idListForm" action="asset_update_batch" style="display: none" method="post">
    <input id="assetIdList" name="assetIdList"/>
    <input id="backToLastPageUrl" name="backToLastPageUrl" value="asset_stan_book_material"/>
</form>
</body>
</html>