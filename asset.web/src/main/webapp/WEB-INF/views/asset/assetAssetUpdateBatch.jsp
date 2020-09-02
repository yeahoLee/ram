<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>固定资产批改</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetAssetUpdateBatch.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommonIcon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }
    </style>
</head>
<body>
<%--<input id="assetIdList" value="${assetStanBookSubmit.assetIdList }" type="hidden"/>--%>
<input id="backToLastPageUrl" value="${assetStanBookSubmit.backToLastPageUrl }" type="hidden"/>
<input id="materialCodeSubmit" value="${assetStanBookSubmit.materialCodeSubmit }" type="hidden">
<input id="assetCodeSubmit" value="${assetStanBookSubmit.assetCodeSubmit }" type="hidden">
<input id="assetChsNameSubmit" value="${assetStanBookSubmit.assetChsNameSubmit }" type="hidden">
<input id="assetStatusSubmit" value="${assetStanBookSubmit.assetStatusSubmit }" type="hidden">
<input id="assetTypeSubmit" value="${assetStanBookSubmit.assetTypeSubmit }" type="hidden">
<input id="manageDeptIdSubmit" value="${assetStanBookSubmit.manageDeptIdSubmit }" type="hidden">
<input id="managerIdSubmit" value="${assetStanBookSubmit.managerIdSubmit }" type="hidden">
<input id="useDeptIdSubmit" value="${assetStanBookSubmit.useDeptIdSubmit }" type="hidden">
<input id="userIdSubmit" value="${assetStanBookSubmit.userIdSubmit }" type="hidden">
<input id="savePlaceIdSubmit" value="${assetStanBookSubmit.savePlaceIdSubmit }" type="hidden">
<input id="codeAndNameSubmit" value="${assetStanBookSubmit.codeAndNameSubmit }" type="hidden">
<div style="height:50px;width:100%;padding-top:0px;padding-left:10px;text-align:left;position:fixed;top:0px;left:0px;background:white;z-index:9999">
    <button id="saveBtn" class="btn btn-primary" style="margin-right: 5px;" onclick="updateAssetBatchSubmit();">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <%-- <button class="btn btn-primary" onclick="javascript:window.location.href = '${backToLastPageUrl}';" style="margin-left: 5px;">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button> --%>
    <button class="btn btn-primary" onclick="backToLastPage();" style="margin-left: 5px;">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button>
</div>
<div id="baseInfo" style="margin-top: 45px;">
    <span class="span-title">基础信息</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <!-- 			<div class="col-lg-4 col-md-6 col-sm-6"> -->
            <!-- 	            <div class="gray"><i class="fa fa-random" style="margin-right: 5px;"></i>规格型号：</div> -->
            <!-- 	            <input name="specAndModels" class="textbox"/> -->
            <!-- 	        </div> -->
            <div class="col-lg-4 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-list-ol" style="margin-right: 5px;"></i>序列号：</div>
                <input name="seriesNum" class="textbox"/>
            </div>
            <!-- 	        <div class="col-lg-4 col-md-6 col-sm-6"> -->
            <!-- 	            <div class="gray"><i class="fa fa-tint" style="margin-right: 5px;"></i>计量单位：</div> -->
            <%-- 	            <input name="unitOfMeasId" class="easyui-combobox" data-options="url:'base/common_combo?commonCodeType=UNIT_OF_MEAS',method:'POST'" value="${assetDto.unitOfMeasId }"/> --%>
            <!-- 	        </div> -->
            <div class="col-lg-4 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-envira"></i>资产品牌：</div>
                <input name="assetBrand" class="textbox"/>
            </div>
            <div class="col-lg-4 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-cny"></i>采购价：</div>
                <input name="purcPrice" class="textbox"/>
            </div>
        </div>
        <div class="row" style="height:65px;"> <!-- 根据内容调整css高度 以保证不出现右侧滚动条 -->
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray" style="height:60px;"><i class="fa fa-map-signs"></i>技术参数：</div>
                <textarea id="techPara" class="textbox" style="width:80%;height:60px;resize: none;"></textarea>
            </div>
        </div>
        <div class="row" style="height:65px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray" style="height:60px;"><i class="fa fa-flag" style="margin-right: 5px;"></i>备注：</div>
                <textarea id="remark" class="textbox" style="width:80%;height:60px;resize: none;"></textarea>
            </div>
        </div>
    </div>
</div>
<div id="extendInfo" style="margin-top: 5px;">
    <span class="span-title">延伸信息</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-4 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-table" style="margin-right: 5px;"></i>购置日期：</div>
                <input id="buyDate" class="easyui-datebox"/>
            </div>
            <div class="col-lg-4 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-th-list" style="margin-right: 5px;"></i>合同编号：</div>
                <input name="contractNum" class="textbox"/>
            </div>
            <div class="col-lg-4 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-address-card" style="margin-right: 5px;"></i>联系人：</div>
                <input name="sourceUser" class="textbox"/>
            </div>
            <div class="col-lg-4 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-table" style="margin-right: 5px;"></i>维保期：</div>
                <input id="mainPeriod" class="easyui-datebox"/>
            </div>
            <div class="col-lg-4 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-table" style="margin-right: 5px;"></i>出厂日期：</div>
                <input id="prodTime" class="easyui-datebox"/>
            </div>
            <div class="col-lg-4 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-volume-control-phone"></i>联系方式：</div>
                <input name="sourceContactInfo" class="textbox"/>
            </div>
        </div>
    </div>
</div>
<div id="assetList" style="margin-top: 5px;">
    <input id="assetIdList" name="assetIdList" type="hidden" value="${assetStanBookSubmit.assetIdList}"/>
    <span class="span-title">资产清单</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div>
            <button id="saveBtn" class="btn btn-primary" style="margin-right: 5px;"
                    onclick="$('#assetStanBookDialog').dialog('center').dialog('open');">
                <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>添加资产
            </button>
            <button id="closeBtn" class="btn btn-primary" onclick="removeAssetToList();">
                <i class="fa fa-minus align-top bigger-125" style="margin-right: 5px;"></i>移除资产
            </button>
        </div>
        <div style="margin-top: 5px;">
            <table id="batchUpdateDataGridTable"></table>
        </div>
    </div>
</div>
<!-- 添加资产 -->
<div id="assetStanBookDialog" class="easyui-dialog" title="添加资产"
     data-options="closed:true, width:980,shadow:false, buttons:'#assetStanBookDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 35px;">
                <!-- 物资编码：<input name="materialCode" class="textbox" style="margin-right: 5px;"/> -->
                资产编码：<input name="assetCode" class="textbox" style="margin-right: 5px;"/>
                资产名称：<input name="assetChsName" class="textbox" style="margin-right: 5px;"/>
                <!-- 				资产状态：<input id="assetStatus" name="assetStatus" class="easyui-combobox" data-options="url:'ram/enum_combo_by_asset_status', method:'GET', editable:false"/> -->
                <button class="btn btn-primary" onClick="searchByQuerys();" style="margin-left:5px; margin-right: 5px;">
                    <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>搜索
                </button>
                <button class="btn btn-primary" onclick="openAdvaSearchDialog();" style="margin-right: 5px;">
                    <i class="fa fa-search-plus align-top bigger-125" style="margin-right: 5px;"></i>高级查询
                </button>
            </div>
            <div id="advaSearch" class="col-lg-12 col-md-12 col-sm-12" title="高级查询" style="height: 60px; display:none;">
                <hr class="hr-css"/>
                <div class="row">
                    <!-- <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="gray">物资编码：</div>
                        <input name="materialCode" class="textbox"/>
                    </div> -->
                    <!-- <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="gray">资产名称：</div>
                        <input name="assetChsName" class="textbox"/>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="gray">资产状态：</div>
                        <input name="assetStatus" class="textbox"/>
                    </div> -->
                    <!-- <div class="col-lg-4 col-md-4 col-sm-4">
                        <div class="gray">资产分类：</div>
                        <input name="searchText" class="textbox"/>
                    </div> -->
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <div class="gray">管理部门：</div>
                        <input id="manageDeptId" class="easyui-combobox"
                               data-options="url:'base/dept_combo_by_querys?showType=0', method:'POST'"/>
                    </div>
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <div class="gray">管理员：</div>
                        <input id="managerId" class="easyui-combobox"
                               data-options="url:'base/perm_user_datagrid_code?code=zcgly', mode: 'remote', method:'POST'"/>
                    </div>
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <div class="gray">使用部门：</div>
                        <input id="useDeptId" class="easyui-combobox"
                               data-options="url:'base/dept_combo_by_querys?showType=0', method:'POST'"/>
                    </div>
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <div class="gray">使用人：</div>
                        <input id="userId" class="easyui-combobox"/>
                        <%--data-options="url:'base/user_combo', mode: 'remote', method:'POST'"/>--%>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <table id="dataGridTable"></table>
</div>
<div id="assetStanBookDialogButtons">
    <!-- <a href="javascript:void(0);" id="uploadSaveBtn" class="easyui-linkbutton" onclick="addAssetToList();">添加</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="$('#assetStanBookDialog').dialog('close');">关闭</a> -->
    <button id="uploadSaveBtn" class="btn btn-primary" onclick="addAssetToList();" style="margin-right:5px;">
        <i class="fas fa-plus" style="margin-right: 5px;"></i>添加
    </button>
    <button class="btn btn-primary" onclick="$('#assetStanBookDialog').dialog('close');" style="margin-right:5px;">
        <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
    </button>
</div>
</body>
</html>