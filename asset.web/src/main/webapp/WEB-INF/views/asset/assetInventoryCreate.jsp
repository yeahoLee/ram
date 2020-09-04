<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>资产盘点申请单</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetInventoryCreate.js"></script>
    <script type="text/javascript" src="resources/js/flowable/approveCommon.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommonIcon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox, .easyui-combotree {
            width: 180px;
        }
    </style>
</head>
<body>
<input id="loginUserID" value="${userInfoDto.id }" type="hidden"/>
<input id="ChsName" value="${userInfoDto.chsName }" type="hidden"/>
<input id="type" value="${produceTypeStr}" type="hidden"/>
<div style="height:50px;width:100%;padding-top:0px;padding-left:10px;text-align:left;position:fixed;top:0px;left:0px;background:white;z-index:9999">
    <button id="saveInventorySubmit" class="btn btn-primary" style="margin-right: 5px;"
            onclick="saveInventorySubmit(1);">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button id="saveAndCheckInventory" class="btn btn-primary" style="margin-right: 5px;"
            onclick="saveInventorySubmit(2);">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存并发起审批
    </button>
    <button class="btn btn-primary" onclick="location='./assetInventory_query'">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button>
</div>
<div align="center" style="font-weight:blod;font-size:20px;padding:5px;margin-top:50px;">盘点任务申请单</div>
<div id="baseInfo" style="margin-top: 45px;">
    <span class="span-title">基础信息</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-4 col-md-5 col-sm-6">
                <div class="gray"><span class="span-red-start">*</span><i class="fa fa-list-alt"></i>盘点任务名称：</div>
                <input id="inventoryName" name="inventoryName" type="text" class="textbox">
            </div>
            <div class="col-lg-4 col-md-5 col-sm-4">
                <div class="gray"><span class="span-red-start">*</span><i class="fa fa-list-ol"
                                                                          style="margin-right: 5px;"></i>物资的类型：
                </div>
                <input id="produceType" class="easyui-combobox"/>
            </div>
        </div>
        <div class="row" style="height:65px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray" style="height:60px;"><span class="span-red-start">*</span><i class="fa fa-line-chart"
                                                                                               style="margin-right: 5px;"></i>盘点任务说明：
                </div>
                <textarea id="reason" class="textbox" style="width:84%;height:60px;resize: none;"></textarea>
            </div>
        </div>
    </div>
</div>

<br/>
<br/>

<div id="assetScopeList" style="">
    <span class="span-title">添加盘点范围</span> （若未指定盘点范围同时未附加盘点资产，系统默认盘点当前物资类型下的全部资产）
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div>
            <button id="inventoryScope" class="btn btn-primary" style="margin-right: 5px;"
                    onclick="addInventoryScope();">
                <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>添加盘点资产范围
            </button>
        </div>
        <div style="margin-top:5px;height:auto;">
            <table id="assetInventoryScope"></table>
        </div>
    </div>
</div>

<br/>
<br/>

<div id="assetList" style="">
    <span class="span-title">附加盘点资产清单</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div>
            <button id="saveBtn" class="btn btn-primary" style="margin-right: 5px;" onclick="openSearch();">
                <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>添加资产
            </button>
            <button id="closeBtn" class="btn btn-primary" onclick="removeAssetToList();">
                <i class="fa fa-minus align-top bigger-125" style="margin-right: 5px;"></i>移除资产
            </button>
        </div>
        <div style="margin-top: 5px;">
            <table id="assetInventoryDataGrid"></table>
        </div>
    </div>
</div>
<!-- 添加资产 -->
<div id="assetListDialog" class="easyui-dialog" title="添加资产"
     data-options="closed:true, width:980,shadow:false, buttons:'#assetListDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 35px;">
                资产编码：<input name="assetCode" class="textbox" style="margin-right: 5px;width: 120px;"/>
                资产名称：<input name="assetChsName" class="textbox" style="margin-right: 5px;width: 120px;"/>
                资产状态：<input name="assetAssetStatus" id="assetAssetStatus" class="easyui-combobox"
                            style="margin-right: 5px;width: 120px;"/>
                <button class="btn btn-primary" onClick="searchByQuerys();" style="margin-left:5px; margin-right: 5px;">
                    <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>搜索
                </button>
                <button class="btn btn-primary" onclick="openAdvaSearchDialog();" style="margin-right: 5px;">
                    <i class="fa fa-search-plus align-top bigger-125" style="margin-right: 5px;"></i>高级查询
                </button>
                <button class="btn btn-primary" onClick="defaultSearch();" style="margin-right: 5px;">
                    <i class="fa fa-refresh" style="margin-right: 5px;"></i>重置搜索
                </button>
            </div>
            <div id="advaSearch" class="col-lg-12 col-md-12 col-sm-12" title="高级查询" style="height: 60px; display:none;">
                <hr class="hr-css"/>
                <div class="row">
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <div class="gray">管理部门：</div>
                        <input name="assetManageDeptId" id="assetManageDeptId" class="easyui-combotree"
                               data-options="url:'base/get_dept_tree', method:'POST'"/>
                    </div>
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <div class="gray">使用部门：</div><!-- 待改 -->
                        <input id="useDeptId" class="easyui-combotree"/>
                    </div>
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <div class="gray">管理员：</div>
                        <input name="assetManagerId" id="assetManagerId" class="easyui-combobox"
                               data-options="url:'base/perm_user_datagrid_code?code=zcgly', mode: 'remote', method:'POST'"
                               style="margin-right: 5px;"/>
                    </div>
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <div class="gray">使用人：</div>
                        <input id="userId" class="easyui-combobox"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <table id="dataGridTable"></table>
</div>
<div id="assetListDialogButtons">
    <button id="uploadSaveBtn" class="btn btn-primary" onclick="addAssetToList();" style="margin-right:5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>添加
    </button>
    <button class="btn btn-primary" onclick="closeAssetListDialog()" style="margin-right:5px;">
        <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
    </button>
</div>

<div id="assetScopeDialog" class="easyui-dialog" title="添加资产"
     data-options="closed:true, width:820,height:260, buttons:'#assetScopeDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray">资产状态：</div>
                <input id="assetStatus" name="assetStatus" class="easyui-combobox" style="margin-right: 5px;"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">资产类别：</div>
                <div class="gray" style="width:580px; text-align: left; padding-left: 15px;">
                    <span id="showAssetTypeCodeAndData" style="position:relative;left:-15px"></span>
                    <a id="showAssetTypeCodeAndDataButton" onclick="openDialogForAssetType();"
                       style="padding-right: 15px;">选择资产类别</a>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray">管理部门：</div>
                <input name="manageDeptId" id="manageDeptId" class="easyui-combotree"
                       data-options="url:'base/get_dept_tree', method:'POST'" style="margin-right: 5px;"/>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray">使用部门：</div>
                <input name="useDeptIdScope" id="useDeptIdScope" class="easyui-combotree"
                       data-options="url:'base/get_dept_tree', method:'POST'" style="margin-right: 5px;"/>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray">管理员：</div>
                <input name="managerId" id="managerId" class="easyui-combobox"
                       data-options="url:'base/perm_user_datagrid_code?code=zcgly', method:'POST'"
                       style="margin-right: 5px;"/>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray">使用人：</div>
                <input name="userIdScope" id="userIdScope" class="easyui-combobox" style="margin-right: 5px;"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">安装位置：</div>
                <div class="gray" style="width:580px; text-align: left; padding-left: 15px;">
                    <span id="showCodeAndData"></span>
                    <a onclick="openDialogForSavePlace();" style="padding-right: 15px;">选择安装位置</a>
                    <input id="savePlaceId" name="savePlaceId" type="hidden">
                </div>
            </div>
        </div>
    </div>
</div>
<div id="assetScopeDialogButtons">
    <button id="uploadSaveBtnScope" class="btn btn-primary" onclick="addScopeToDatagrid();" style="margin-right:5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>添加
    </button>
    <button class="btn btn-primary" onclick="closeAssetScopeDialog()" style="margin-right:5px;">
        <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 调入位置 -->
<div id="instSiteCodeDialog" class="easyui-dialog" title="选择调入位置" data-options="width:880, height:435, closed: true">
    <iframe id="instSiteCodeDialogFrame" class="embed-responsive-item" src="asset_save_place"
            style="width: 100%;height: 100%;" frameborder="no" border="0" marginwidth="0" marginheight="0"
            allowtransparency="yes"></iframe>
</div>
<!--资产类别选择-->
<div id="assetTypeDialog" class="easyui-dialog" title="添加资产类别" data-options="width:880, height:435, closed: true">
    <iframe id="initAssetTypeDialogIframe" class="embed-responsive-item" src="asset_type_place"
            style="width: 100%;height: 100%;" frameborder="no" border="0" marginwidth="0" marginheight="0"
            allowtransparency="yes"></iframe>
    <input name="rowIdHidden" type="hidden">
</div>
</body>
</html>