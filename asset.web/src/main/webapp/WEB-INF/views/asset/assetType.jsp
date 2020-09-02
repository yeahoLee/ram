<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetType.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommonIcon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }
    </style>
</head>
<body>
<input id="assetTypeCode" type="hidden"/>
<input id="deviceCodeLevel1" type="hidden"/>
<input id="deviceCodeLevel2" type="hidden"/>
<input id="deviceCodeLevel3" type="hidden"/>
<input id="deviceCodeLevel4" type="hidden"/>
<div id="instSiteCodeDialog" class="easyui-layout" data-options="fit:true">
    <div style="height: 45px;" data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="padding-top: 5px;">
                <div class="container-fluid">
                    <div class="row" style="padding-top: 5px;">
                        <div class="col-lg-12 col-md-12 col-sm-12">
                            选择位置：<input id="deviceIdLevel1" class="easyui-combobox"/>
                            &nbsp;&nbsp;<input id="deviceIdLevel2" class="easyui-combobox"/>
                            &nbsp;&nbsp;<input id="deviceIdLevel3" class="easyui-combobox"/>
                            &nbsp;&nbsp;<input id="deviceIdLevel4" class="easyui-combobox"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div data-options="region:'center', border:false">
        <table id="dataGridTable"></table>
    </div>
    <div style="height: 43px; padding-top: 5px;" data-options="region:'south', border:false" align="right">
        <button id="saveBtn" class="btn btn-primary" onclick="sendCodeAndName();" style="margin-right: 5px;">
            <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>添加
        </button>
        <button id="initBtn" class="btn btn-primary" onclick="initlevel1();" style="margin-right: 5px;">
            <i class="fa fa-refresh align-top bigger-125" style="margin-right: 5px;"></i>重置
        </button>
        <button id="closeBtn" class="btn btn-primary" onclick="window.parent.closeAssetTypeDialog();"
                style="margin-right: 5px;">
            <i class="fa fa-times align-top bigger-125" style="margin-right: 5px;"></i>关闭
        </button>
    </div>
</div>
</body>
</html>