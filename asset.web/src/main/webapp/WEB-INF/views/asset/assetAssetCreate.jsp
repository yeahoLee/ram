<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>固定资产增</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetAssetCreate.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommonIcon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }
    </style>
</head>
<body>
<div id="baseInfo">
    <span class="span-title">基础信息</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><span class="span-red-start">*</span><i class="fa fa-barcode"></i>物资编号：</div>
                <input name="materialCode" class="textbox" onBlur="findTypeByMaterialCode()"/>
            </div>
            <div class="col-lg-8 col-md-8 col-sm-6">
                <div class="gray"><i class="fab fa-dropbox"></i>资产类别：</div>
                <span id="assetTypeName"></span>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-list-alt"></i>资产名称：</div>
                <span id="assetName"></span>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><span class="span-red-start">*</span><i class="fa fa-tags"></i>资产类型：</div>
                <input id="assetType" name="assetType" class="easyui-combobox" data-options="editable:false"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-random"></i>规格型号：</div>
                <input name="specAndModels" class="textbox"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-list-ol"></i>序列号：</div>
                <input name="seriesNum" class="textbox"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><span class="span-red-start">*</span><i class="fa fa-tint"></i>计量单位：</div>
                <input name="unitOfMeasId" class="easyui-combobox"
                       data-options="url:'base/common_combo?commonCodeType=UNIT_OF_MEAS',method:'POST'"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-envira"></i>资产品牌：</div>
                <input name="assetBrand" class="textbox"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-cny"></i>采购价：</div>
                <input name="purcPrice" class="textbox"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fas fa-yen-sign"></i>资产原值：</div>
                <input name="equiOrigValue" class="textbox"/>
            </div>
        </div>
        <div class="row" style="height:65px;"> <!-- 根据内容调整css高度 以保证不出现右侧滚动条 -->
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray" style="height:60px;"><i class="fa fa-map-signs"></i>技术参数：</div>
                <textarea id="techPara" class="textbox" style="width:84%;height:60px;resize: none;"></textarea>
            </div>
        </div>
        <div class="row" style="height:65px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray" style="height:60px;"><i class="fa fa-flag"></i>备注：</div>
                <textarea id="remark" class="textbox" style="width:84%;height:60px;resize: none;"></textarea>
            </div>
        </div>
    </div>
</div>
<div id="extendInfo" style="margin-top: 5px;">
    <span class="span-title">延伸信息</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><span class="span-red-start">*</span><i class="far fa-building"></i>所属公司：</div>
                <input id="companyId" name="companyId" class="easyui-combobox"
                       data-options="url:'base/dept_combo_by_querys?showType=1', method:'POST'"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><span class="span-red-start">*</span><i class="far fa-map"></i>所属线路/建筑：</div>
                <input id="belongLine" name="belongLine" class="easyui-combobox"
                       data-options="url:'base/common_combo?commonCodeType=BELONG_LINE',method:'POST'"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><span class="span-red-start">*</span><i class="fa fa-table"></i>购置日期：</div>
                <input id="buyDate" class="easyui-datebox"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><span class="span-red-start">*</span><i class="fa fa-sitemap"></i>主管部门：</div>
                <input id="manageDeptId" name="manageDeptId" class="easyui-combobox"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><span class="span-red-start">*</span><i class="fas fa-user-tie"></i>资产管理员：</div>
                <input name="managerId" class="easyui-combobox"
                       data-options="url:'base/user_combo', mode: 'remote', method:'POST'"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-th-list"></i>合同编号：</div>
                <input name="contractNum" class="textbox"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-location-arrow"></i>资产来源：</div>
                <input name="assetSource" class="textbox"/>
            </div>
            <div class="col-lg-8 col-md-8 col-sm-6">
                <div class="gray"><span class="span-red-start">*</span><i class="fa fa-server"></i>安装位置：</div>
                <div class="gray" style="width:75%; text-align: left; padding-left: 15px; background-color: white;">
                    <span id="showCodeAndData" style="position:relative; left:-15px;"></span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a onclick="$('#instSiteCodeDialog').dialog('center').dialog('open');" style="padding-right: 15px;">选择位置</a>
                </div>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-indent"></i>标段编号：</div>
                <input name="tendersNum" class="textbox"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-wrench"></i>维保期：</div>
                <input id="mainPeriod" class="easyui-datebox"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-address-card"></i>联系人：</div>
                <input name="sourceUser" class="textbox"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-volume-control-phone"></i>联系方式：</div>
                <input name="sourceContactInfo" class="textbox"/>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6">
                <div class="gray"><i class="fa fa-table"></i>出厂日期：</div>
                <input id="prodTime" class="easyui-datebox"/>
            </div>
        </div>
    </div>
</div>
<div style="margin-right: 15px; margin-top: 10px; text-align:right;">
    <button id="saveBtn" class="btn btn-primary" onclick="createAsset();" style="margin-right: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>添加
    </button>
    <button id="closeBtn" class="btn btn-primary" onclick="">
        <i class="fa fa-times align-top bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<div id="instSiteCodeDialog" class="easyui-dialog" title="添加安装位置" data-options="width:880, height:435, closed: true">
    <iframe class="embed-responsive-item" src="asset_save_place" style="width: 100%;height: 100%;" frameborder="no"
            border="0" marginwidth="0" marginheight="0" allowtransparency="yes"></iframe>
    <input name="rowIdHidden" type="hidden">
</div>

<!-- <div id="instSiteCodeDialog" class="easyui-dialog" title="添加安装位置" data-options="width:880, closed: true, buttons:'#instSiteCodeDialogButtons'">
	<input name="assetLineIdHidden" type="hidden">
    <input name="stationIdHidden" type="hidden">
    <input name="buildNumIdHidden" type="hidden">
    <input name="floorNumIdHidden" type="hidden">
    <input name="rowIdHidden" type="hidden">
	<div class="container-fluid">
		<div class="row" style="margin-top: 5px;">
			<div class="col-lg-12 col-md-12 col-sm-12">
				选择位置：
	            <input id="assetLineId" class="easyui-combobox" data-options="url:'base/common_combo?commonCodeType=PLACE_CODE_LV1&showCode=true',method:'POST'"/>
	            <input id="stationId" class="easyui-combobox" />
	            <input id="buildNumId" class="easyui-combobox" />
	            <input id="floorNumId" class="easyui-combobox" />
	        </div>
		</div>
	</div>
	<div>
		<table id="dataGridTable"></table>
	</div>
</div>
<div id="instSiteCodeDialogButtons" style="text-align:right;">
	<a class="easyui-linkbutton" style="padding-left: 10px;padding-right: 10px;" onclick="showCodeAndName()">确定</a>
	<a class="easyui-linkbutton" style="padding-left: 10px;padding-right: 10px;" onClick="$('#instSiteCodeDialog').dialog('close');">关闭</a>
</div> -->
</body>
</html>