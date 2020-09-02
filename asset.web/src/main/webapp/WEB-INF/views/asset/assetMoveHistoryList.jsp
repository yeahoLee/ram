<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- AssetBorrowView -->
    <title>资产移动历史</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetMoveHistoryList.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 120px;
        }
    </style>
</head>
<body>
<input id="createUserID" type="hidden" value="${userInfoDto.id}">
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 40px;">
                    <div style="float:left; margin-top: 5px;">
                        主管部门： <input id="manageDeptId" class="easyui-combotree" style="width: 180px"
                                     data-options="url: 'base/get_dept_tree', method:'POST'"/>
                        资产类别：<input id="assetLeave" class="easyui-combobox"
                                    data-options="url:'assetreport/common_datagrid?commonCodeType=DEVICE_CODE_LV1', method:'POST'"/>
                        移动时间：<input id="createTimestampStart" class="easyui-datebox"/>&nbsp;&nbsp;~&nbsp;&nbsp;<input
                            id="createTimestampEnd" class="easyui-datebox"/>
                    </div>
                    <div style="float:right;">
                        <button class="btn btn-primary" onClick="searchByQuerys();"
                                style="margin-left:5px; margin-right: 5px;">
                            <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>统计
                        </button>
                        <button class="btn btn-primary" onClick="resetQuerys();" style="margin-right: 5px;">
                            <i class="fa fa-refresh align-top bigger-125" style="margin-right: 5px;"></i>重置
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div data-options="region:'center', border:false">
        <table id="dataGridTable"></table>
    </div>
</div>
</body>
</html>