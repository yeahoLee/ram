<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetStanBookSearch.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }
    </style>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 35px;">
                    物资编码：<input name="materialCode" class="textbox" style="margin-right: 5px;"/>
                    <!-- 					资产编码：<input name="assetCode" class="textbox" style="margin-right: 5px;"/> -->
                    资产名称：<input name="assetChsName" class="textbox" style="margin-right: 5px;"/>
                    资产状态：<input id="assetStatus" name="assetStatus" class="easyui-combobox"
                                data-options="url:'ram/enum_combo_by_asset_status', method:'GET', editable:false"
                                style="width: 80px;" value="${dynamicViewDto.assetStatusNum }"/>
                    <button class="btn btn-primary" onClick="searchByQuerys();"
                            style="margin-left:5px; margin-right: 5px;">
                        <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>搜索
                    </button>
                    <button class="btn btn-primary" onclick="openAdvaSearchDialog();" style="margin-right: 5px;">
                        <i class="fa fa-search-plus align-top bigger-125" style="margin-right: 5px;"></i>高级查询
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div data-options="region:'center', border:false">
        <table id="dataGridTable"></table>
    </div>
</div>
<!--高级搜索的dialog  -->
<div id="advaSearch" class="easyui-dialog" title="高级查询"
     data-options="width:820,height:210, closed: true,buttons:'#closeAdvaSearchDialogButton'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 8px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">资产类型：</div>
                <!-- 				<input id="assetType" name="assetType" class="easyui-combobox" editable="false"/> -->
                <div class="gray" style="width:580px; text-align: left; padding-left: 15px;">
                    <span id="showAssetTypeCodeAndData"
                          style="position:relative;left:-15px">${dynamicViewDto.assetType }</span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a id="showAssetTypeCodeAndDataButton"
                       onclick="$('#assetTypeDialog').dialog('center').dialog('open');" style="padding-right: 15px;">选择资产类别</a>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray">管理部门：</div>
                <input id="manageDeptId" class="easyui-combobox"
                       data-options="url:'base/dept_combo_by_querys?showType=1', method:'POST'"/>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray">管理员：</div>
                <input id="managerId" class="easyui-combobox"
                       data-options="url:'base/perm_user_datagrid_code?code=zcgly', mode: 'remote', method:'POST'"/>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray">使用部门：</div>
                <input id="useDeptId" class="easyui-combobox"
                       data-options="url:'base/dept_combo_by_querys?showType=1', method:'POST'"/>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray">使用人：</div>
                <input id="userId" class="easyui-combobox"
                       data-options="url:'base/user_combo', mode:'remote', method:'POST'"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">安装位置：</div>
                <div class="gray" style="width:580px; text-align: left; padding-left: 15px;">
                    <span id="showCodeAndData" style="position:relative;left:-15px"></span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a id="showCodeAndDataButton" onclick="$('#instSiteCodeDialog').dialog('center').dialog('open');"
                       style="padding-right: 15px;">选择位置</a>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="closeAdvaSearchDialogButton" style="text-align: right;">
    <button class="btn btn-primary" onclick="$('#advaSearch').dialog('close')" style="margin-right:5px;">
        <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 安装位置 -->
<div id="instSiteCodeDialog" class="easyui-dialog" title="添加安装位置" data-options="width:880, height:435, closed: true">
    <iframe class="embed-responsive-item" src="asset_save_place" style="width: 100%;height: 100%;" frameborder="no"
            border="0" marginwidth="0" marginheight="0" allowtransparency="yes"></iframe>
</div>
<div id="instSiteCodeDialogButtons" style="text-align:right;">
    <a class="easyui-linkbutton" style="padding-left: 10px;padding-right: 10px;" onclick="showCodeAndName()">确定</a>
    <a class="easyui-linkbutton" style="padding-left: 10px;padding-right: 10px;"
       onClick="$('#instSiteCodeDialog').dialog('close');">关闭</a>
</div>

<!--资产类别选择-->
<div id="assetTypeDialog" class="easyui-dialog" title="添加资产类别" data-options="width:880, height:435, closed: true">
    <iframe class="embed-responsive-item" src="asset_type_place" style="width: 100%;height: 100%;" frameborder="no"
            border="0" marginwidth="0" marginheight="0" allowtransparency="yes"></iframe>
    <input name="rowIdHidden" type="hidden" value="${dynamicViewDto.assetType }">
</div>
<div id="assetTypeDialogButtons" style="text-align:right;">
    <a class="easyui-linkbutton" style="padding-left: 10px;padding-right: 10px;"
       onclick="showAssetTypeCodeAndName()">确定</a>
    <a class="easyui-linkbutton" style="padding-left: 10px;padding-right: 10px;"
       onClick="$('#assetTypeDialog').dialog('close');">关闭</a>
</div>

</body>
</html>