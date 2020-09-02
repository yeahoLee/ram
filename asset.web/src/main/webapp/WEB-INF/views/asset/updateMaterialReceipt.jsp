<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/updateMaterialReceipt.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommonIcon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 160px;
        }

        .gray {
            width: 150px;
        }
    </style>
    <title>编辑资产新增单</title>
</head>
<body>
<div style="width:100%; padding-top:0px; padding-left:10px; z-index:1000; text-align:left; position:fixed; top:0px; left:0px; background:white;">
    <button id="sBtn" class="btn btn-primary" onclick="saveReceipt();" style="margin-right: 5px;">
        <i class="fa fa-save" style="margin-right: 5px;"></i>保存
    </button>
    <button id="cBtn" class="btn btn-primary" onclick="location='./receiptList'">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button>
</div>
<div align="center" style="font-weight:blod;font-size:20px;padding:5px;margin-top:50px;">资产新增申请单</div>
<div id="mateReceInfo" style="margin-top: 40px;">
    <input id="recId" type="hidden" value="${receiptDto.id }">
    <input id="type" value="${receiptDto.produceTypeStr }" type="hidden"/>
    <span class="span-title">基本信息</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-4 col-md-5 col-sm-6">
                <div class="gray" style="width: 170px;"><span class="span-red-start">*</span><i
                        class="fa fa-list-alt"></i>资产新增单名称：
                </div>
                <input name="receiptName" class="textbox" value="${receiptDto.receiptName }"/>
            </div>
            <div class="col-lg-4 col-md-5 col-sm-6">
                <div class="gray" style="width: 170px;"><span class="span-red-start">*</span><i
                        class="fa fa-location-arrow"></i>来源方式：
                </div>
                <input id="sourceType" name="sourceType" class="easyui-combobox"
                       data-options="url:'ram/enum_combo_by_source_type', method:'GET', editable:false, disabled:true"
                       value="${receiptDto.sourceType }"/>
            </div>
        </div>
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-4 col-md-5 col-sm-4">
                <div class="gray" style="width:180px;"><span class="span-red-start">*</span><i class="fa fa-list-ol"
                                                                                               style="margin-right: 5px;"></i>物资的类型：
                </div>
                <input id="produceType" class="easyui-combobox"/>
            </div>
        </div>
        <div class="row" style="height:65px;"> <!-- 根据内容调整css高度 以保证不出现右侧滚动条 -->
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray" style="height:60px; width: 170px;"><span class="span-red-start">*</span><i
                        class="fa fa-line-chart"></i>新增原因：
                </div>
                <textarea id="reason" name="reason" class="textbox"
                          style="width:80%;height:60px;resize: none;">${receiptDto.reason }</textarea>
            </div>
        </div>
        <div class="row" style="height:65px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray" style="height:60px; width: 170px;"><i class="fa fa-flag"></i>备注：</div>
                <textarea id="remark" name="remark" class="textbox"
                          style="width:80%;height:60px;resize: none;">${receiptDto.remark }</textarea>
            </div>
        </div>
    </div>
    <span class="span-title">物资信息</span>
    <hr class="hr-css"/>
    <table id="assetDataGridTable"></table>
    <div id="assetDataGridTableButtons">
        <button id="addBtn" class="btn btn-primary" onclick="addAsset();"
                style="margin-bottom:5px; margin-left:5px; margin-right: 5px; margin-top: 5px;">
            <i class="fa fa-plus" style="margin-right: 5px;"></i>添加
        </button>
        <!-- <button class="btn btn-primary" onclick="exportExcel();" style="margin-right:5px;">
              <i class="fa fa-download" style="margin-right: 5px;"></i>下载模板
        </button>
        <button class="btn btn-primary" onclick="excelDialogOpen();" style="margin-right:5px;">
              <i class="fa fa-download" style="margin-right: 5px;"></i>批量导入
        </button> -->
        <!-- <div class="btn-group">
            <button class="btn btn-primary" o>
                  <i class="fa fa-download" style="margin-right: 5px;"></i>批量导入<span class="caret" style="margin-left: 5px;"></span>
            </button>
              <ul class="dropdown-menu">
                <li><a href="javascript:void(0);" onclick="excelDialogOpen()">导入</a></li>
                <li><a href="javascript:void(0);" onclick="exportExcel()">下载模板</a></li>
              </ul>
        </div> -->
        <button id="delBtn" class="btn btn-primary" onclick="deleteAsset()">
            <i class="fa fa-trash-alt" style="margin-right: 5px;"></i>删除
        </button>
        <button class="btn btn-primary" onClick="$('#importAssetDialog').dialog('center').dialog('open')"
                style="margin-right:5px;">
            <i class="" style="margin-right: 5px;"></i>批量导入
        </button>
        <a href="resources/downLoadFile/资产导入模板.xls" class="btn btn-primary">下载模板</a>
    </div>
</div>
<!-- 上传物资信息 -->
<!-- <div id="excelDialog" class="easyui-dialog" title="上传物资信息" data-options="width:400,height:110, closed: true, buttons:'#excelDialogButtons'">
	<div style="margin: 3px;">
	<form enctype="multipart/form-data" id="batchUpload"  action="receipt/uploadAssetExcel" method="post" class="form-horizontal">	 
		<button class="btn btn-success btn-xs" id="uploadEventBtn" style="height:26px;float: left"  type="button" >选择文件</button>
		<input type="file" name="file"  style="width:0px;height:0px;float: left" id="uploadEventFile">
	    <input id="uploadEventPath"  disabled="disabled"  type="text" style="width: 250px;">
	</form>
	<button style="height:26px;float: right;margin-top: -25px" type="button" class="btn btn-success btn-sm"  onclick="asset.uploadBtn()" >上传</button>
	</div>
</div>
<div id="excelDialogButtons" style="text-align:right;">
	<a class="easyui-linkbutton" style="padding-left: 10px;padding-right: 10px;" onClick="$('#excelDialog').dialog('close');">关闭</a>
</div> -->

<!--上传物资信息Dialog  -->
<div id="importAssetDialog" class="easyui-dialog" title="导入文件"
     data-options="closed:true,width:400,buttons:'#importAssetDialogButtons'">
    <form id="importForm" method="POST" enctype="multipart/form-data">
        <input type="file" name="uploadFileData"/>
    </form>
</div>
<div id="importAssetDialogButtons">
    <a id="importAssetConfirmBtn" href="javascript:void(0);" class="easyui-linkbutton"
       onclick="importAssetXls();">确定</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="$('#importAssetDialog').dialog('close')">取消</a>
</div>
<!-- 资产信息 -->
<div id="assetDialog" class="easyui-dialog" title="资产信息" style="width: 1100px; height: 450px;"
     data-options="closed: true, buttons:'#assetDialogButtons'">
    <from id="asset_add">
        <input id="assetId" type="hidden"/>
        <div id="baseInfo" style="margin-top: 5px;">
            <span class="span-title">基础信息</span>
            <hr class="hr-css"/>
            <div class="container-fluid">
                <div class="row" style="margin-top: 5px;">
                    <div class="col-lg-4 col-md-4 col-sm-6">
                        <div class="gray"><span class="span-red-start">*</span><i class="fa fa-barcode"></i>物资编号：</div>
                        <input name="materialCode" id="materialCode" class="textbox"  onclick="reloadAsset();"/>
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
                        <!-- 			            <input id="assetType" name="assetType" class="easyui-combobox" data-options="editable:false"/> -->
                        <span id="assetType"></span>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6">
                        <div class="gray"><i class="fa fa-random"></i>规格型号：</div>
                        <!-- 			            <input name="specAndModels" class="textbox"/> -->
                        <span id="specAndModels"></span>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6">
                        <div class="gray"><i class="fa fa-list-ol"></i>序列号：</div>
                        <input name="seriesNum" class="textbox"/>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6">
                        <div class="gray"><span class="span-red-start">*</span><i class="fa fa-tint"></i>计量单位：</div>
                        <%--<input id="unitOfMeasId" name="unitOfMeasId" class="easyui-combobox"
                               data-options="url:'base/common_combo?commonCodeType=UNIT_OF_MEAS&showCode=true',method:'POST'"/>--%>
                        <span id="unitOfMeasId"></span>
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
                        <div class="gray"><i class="fa fa-cny"></i>资产原值：</div>
                        <input name="equiOrigValue" class="textbox"/>
                    </div>
                </div>
                <div class="row" style="height:65px;">
                    <div class="col-lg-12 col-md-12 col-sm-12">
                        <div class="gray" style="height:60px;"><i class="fa fa-sliders"></i>技术参数：</div>
                        <textarea id="techPara" name="techPara" class="textbox"
                                  style="width:84%;height:60px;resize: none;"></textarea>
                    </div>
                </div>
                <div class="row" style="height:65px;">
                    <div class="col-lg-12 col-md-12 col-sm-12">
                        <div class="gray" style="height:60px;"><i class="fa fa-flag"></i>备注：</div>
                        <textarea id="remark" name="remark" class="textbox"
                                  style="width:84%;height:60px;resize: none;"></textarea>
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
                        <div class="gray"><span class="span-red-start">*</span><i class="fa fa-building"></i>所属公司：</div>
                        <input id="companyId" name="companyId" class="easyui-combobox"
                               data-options="url:'base/dept_combo_by_querys?showType=1',
							   				 method:'POST',
							   				 onChange:function(newValue,oldValue){
												if(newValue!=oldValue){
													$('#manageDeptId').combobox('setValue','');
													$('#managerId').combobox('setValue','');
												}
							   				 }"/>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6">
                        <div class="gray"><span class="span-red-start">*</span><i class="fa fa-map"></i>所属线路/建筑：</div>
                        <input id="belongLine" name="belongLine" class="easyui-combobox"
                               data-options="url:'base/common_combo?commonCodeType=BELONG_LINE',method:'POST'"/>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6">
                        <div class="gray"><span class="span-red-start">*</span><i class="fa fa-table"></i>购置日期：</div>
                        <input id="buyDate" name="buyDate" class="easyui-datebox"/>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6">
                        <div class="gray"><span class="span-red-start">*</span><i class="fa fa-sitemap"></i>主管部门：</div>
                        <input id="manageDeptId" name="manageDeptId" class="easyui-combobox"
                               data-options="mode:'remote'"/>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6">
                        <div class="gray"><span class="span-red-start">*</span><i class="fa fa-user"></i>资产管理员：</div>
                        <input id="managerId" name="managerId" class="easyui-combobox" data-options="mode:'remote'"/>
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
                        <div class="gray"
                             style="width:500px; text-align: left; padding-left: 15px; background-color: white;">
                            <span id="showCodeAndData" style="position:relative;left:-15px"></span>&nbsp;&nbsp;&nbsp;&nbsp;
                            <a onclick="openDialig();" style="padding-right: 15px;">选择位置</a>
                            <input id="sp" name="savePlaceIdHidden" type="hidden">
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6">
                        <div class="gray"><i class="fa fa-indent"></i>标段编号：</div>
                        <input name="tendersNum" class="textbox"/>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-6">
                        <div class="gray"><i class="fa fa-wrench"></i>维保期：</div>
                        <input id="mainPeriod" name="mainPeriod" class="easyui-datebox"/>
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
                        <input id="prodTime" name="prodTime" class="easyui-datebox"/>
                    </div>
                </div>
            </div>
        </div>
    </from>
</div>
<div id="assetDialogButtons" style="text-align:right;">
    <button id="saveBtn" class="btn  btn-primary" onclick="saveAsset();" style="margin-right: 5px;">
        <i class="fa fa-save" style="margin-right: 5px;"></i>保存
    </button>
    <button id="closeBtn" class="btn btn-primary" onclick="$('#assetDialog').dialog('close');">
        <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 安装位置 -->
<div id="instSiteCodeDialog" class="easyui-dialog" title="添加安装位置" data-options="width:880, height:435, closed: false">
    <iframe id="instSiteCodeDialogFrame" class="embed-responsive-item" src="asset_save_place"
            style="width: 100%;height: 100%;" frameborder="no" border="0" marginwidth="0" marginheight="0"
            allowtransparency="yes"></iframe>
</div>

<div id="assetListDialog" class="easyui-dialog" title="添加资产"
     data-options="closed:true, width:980">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 35px;">
                资产编码：<input id="assetCodeQuery" class="textbox" style="margin-right: 5px;"/>
                资产名称：<input id="assetNameQuery" class="textbox" style="margin-right: 5px;"/>
                <button class="btn btn-primary" onClick="searchByQuerys();" style="margin-left:5px; margin-right: 5px;">
                    <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>搜索
                </button>
            </div>
            <div id="advaSearch" class="col-lg-12 col-md-12 col-sm-12" title="高级查询" style="height: 60px; display:none;">
                <hr class="hr-css"/>
                <div class="row">
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <div class="gray">使用部门：</div><!-- 待改 -->
                        <input id="useDeptId" class="easyui-combotree"/>
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
</body>
</html>