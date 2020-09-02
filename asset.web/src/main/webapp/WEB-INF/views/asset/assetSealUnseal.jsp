<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>资产启封申请单</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetSealRemove.js"></script>
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
<input id="UserID" value="${userInfoDto.id }" type="hidden"/>
<input id="ChsName" value="${userInfoDto.chsName }" type="hidden"/>
<div style="height:50px;width:100%;padding-top:0px;padding-left:10px;text-align:left;position:fixed;top:0px;left:0px;background:white;z-index:9999">
    <button id="saveBtn" class="btn btn-primary" style="margin-right: 5px;" onclick="saveAssetSealSubmit(1);">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button id="saveBtn" class="btn btn-primary" style="margin-right: 5px;" onclick="saveAssetSealSubmit(2);">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存并发起审批
    </button>
    <button class="btn btn-primary" onclick="location='./assetsequestration_query'">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button>
</div>
<div align="center" style="font-weight:blod;font-size:20px;padding:5px;margin-top:50px;">资产启封申请单</div>
<div id="baseInfo" style="margin-top: 45px;">
    <span class="span-title">基础信息</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div class="row" style="height:65px;">
            <div class="col-lg-4 col-md-5 col-sm-4">
                <div class="gray"><span class="span-red-start">*</span><i class="fa fa-list-ol"
                                                                          style="margin-right: 5px;"></i>物资的类型：
                </div>
                <input id="produceType" class="easyui-combobox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray" style="height:60px;"><span class="span-red-start">*</span><i class="fa fa-line-chart"
                                                                                               style="margin-right: 5px;"></i>启封原因：
                </div>
                <textarea id="reason" class="textbox" style="width:80%;height:60px;resize: none;"></textarea>
            </div>
        </div>
    </div>
</div>
<div id="assetList" style="margin-top: 60px;">
    <span class="span-title">启封资产清单</span>
    <hr class="hr-css"/>
    <div class="container-fluid">
        <div>
            <button id="saveBtn" class="btn btn-primary" style="margin-right: 5px;"
                    onclick="openAssetStanBookDialog();">
                <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>添加资产
            </button>
            <button id="closeBtn" class="btn btn-primary" onclick="removeAssetToList();">
                <i class="fa fa-minus align-top bigger-125" style="margin-right: 5px;"></i>移除资产
            </button>
        </div>
        <div style="margin-top: 5px;">
            <table id="assetSequestrationSealDataGrid"></table>
        </div>
    </div>
</div>
<!-- 添加资产 -->
<div id="assetListDialog" class="easyui-dialog" title="添加资产"
     data-options="closed:true, width:980,shadow:false, buttons:'#assetListDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 35px;">
                资产编码：<input name="assetCode" class="textbox" style="margin-right: 5px;"/>
                资产名称：<input name="assetChsName" class="textbox" style="margin-right: 5px;"/>
                <!-- 资产状态：<input id="assetStatus" name="assetStatus" class="easyui-combobox"/> -->
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
<div id="assetListDialogButtons">
    <button id="uploadSaveBtn" class="btn btn-primary" onclick="addAssetToList();" style="margin-right:5px;">
        <i class="fas fa-plus" style="margin-right: 5px;"></i>添加
    </button>
    <button class="btn btn-primary" onclick="$('#assetListDialog').dialog('close');" style="margin-right:5px;">
        <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
    </button>
</div>

</body>
</html>