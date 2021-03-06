<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- Standing Book -->
    <title>资产台账（物资模式）</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetStanBookMaterial.js"></script>
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
                <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 40px;">
                    <div style="float:left; margin-top: 5px;">
                        物资编码：<input name="materialCode" class="textbox" style="width:110px; margin-right: 5px;"/>
                        <!-- 						资产编码：<input name="assetCode" class="textbox" style="width:140px; margin-right: 5px;"/> -->
                        物资名称：<input name="assetChsName" class="textbox" style="width:140px; margin-right: 5px;"/>
                        <%--资产状态：<input id="assetStatus" name="assetStatus" class="easyui-combobox" style="width: 80px; margin-right: 5px;"--%>
                                    <%--data-options="url:'ram/enum_combo_by_asset_status', method:'GET', editable:false"/>--%>
                        &nbsp 物资的类型：<input id="produceType" style="width: 120px;" class="easyui-combobox"/>
                    </div>
                    <div style="float:right;">
                        <button class="btn btn-primary" onClick="searchByQuerys();"
                                style="margin-left:5px; margin-right: 5px;">
                            <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>搜索
                        </button>
                        <button class="btn btn-primary" onclick="openAdvaSearchDialog();" style="margin-right: 5px;">
                            <i class="fa fa-search-plus align-top bigger-125" style="margin-right: 5px;"></i>高级查询
                        </button>
                        <button class="btn btn-primary" onClick="searchByDefaultQuerys();">
                            <i class="fa fa-refresh" style="margin-right: 5px;"></i>重置搜索
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div data-options="region:'center',border:false">
        <table id="dataGridTable"></table>
    </div>
</div>
<!--将按钮放入datagrid中  -->
<div id="btdiv" style="width:100%;">
    <div class="btn-group" style="margin-right:5px; margin-left:5px; margin-top:5px; margin-bottom:6px;">
        <c:if test="${zctzwzfast_do}">
        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
                aria-expanded="false">
            <i class="fa fa-bars align-top bigger-125" style="margin-right: 5px;"></i>快速处理<span class="caret"
                                                                                                style="margin-left: 5px;"></span>
        </button>
        <ul class="dropdown-menu">
            <li><a onclick="goToUpdateBatch();">信息批量修改</a></li>
        </ul>
        </c:if>
    </div>
    <!--切换（资产或物资）模式  -->
    <button id="assetModelBtn" class="btn btn-primary" onclick="goToStanBookPage();"
            style="margin-top: 4px;margin-bottom:5px;margin-right:10px;float:right;">
        <i style="margin-right: 5px;"></i>资产模式
    </button>
    <button id="matirialModelBtn" class="btn btn-primary" onclick="goToStanBookMatirialPage();"
            style="margin-top: 4px;margin-bottom:5px;margin-right:10px;float:right;">
        <i style="margin-right: 5px;"></i>物资模式
    </button>
</div>
<!--高级搜索的dialog  -->
<div id="advaSearch" class="easyui-dialog" title="高级查询"
     data-options="width:820,height:210, closed: true,buttons:'#closeAdvaSearchDialogButton'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 8px;">
            <div>
                <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="gray">资产类型：</div>
                    <%--<input id="assetType" name="assetType" class="easyui-combobox" editable="false"/>--%>
                    <div class="gray" style="width:580px; text-align: left; padding-left: 15px;">
                        <span id="showAssetTypeCodeAndData" style="position:relative;left:-15px"></span>&nbsp;&nbsp;&nbsp;&nbsp;
                        <a id="showAssetTypeCodeAndDataButton"
                           onclick="$('#assetTypeDialog').dialog('center').dialog('open');"
                           style="padding-right: 15px;">选择资产类别</a>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6">
                    <div class="gray">管理部门：</div>
                    <select id="manageDeptId" class="easyui-combotree" style="width: 180px;"
                            data-options="url: 'base/get_dept_tree'"></select>
                    <%--<input id="manageDeptId" class="easyui-combobox"--%>
                    <%--data-options="url:'base/dept_combo_by_querys?showType=0', method:'POST'"/>--%>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6">
                    <div class="gray">管理员：</div>
                    <input id="managerId" class="easyui-combobox"
                           data-options="url:'base/perm_user_datagrid_code?code=zcgly', mode: 'remote', method:'POST'"/>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6">
                    <div class="gray">使用部门：</div>
                    <select id="useDeptId" class="easyui-combotree" style="width: 180px;"></select>
                    <%--<input id="useDeptId" class="easyui-combobox" --%>
                    <%--data-options="url:'base/dept_combo_by_querys?showType=0', method:'POST'"/>--%>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6">
                    <div class="gray">使用人：</div>
                    <input id="userId" class="easyui-combobox"/>
                    <!-- 					data-options="url:'base/user_combo', mode: 'remote', method:'POST'"/> -->
                </div>
                <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="gray">安装位置：</div>
                    <div class="gray" style="width:580px; text-align: left; padding-left: 15px;">
                        <span id="showCodeAndData" style="position:relative;left:-15px"></span>&nbsp;&nbsp;&nbsp;&nbsp;
                        <a id="showCodeAndDataButton"
                           onclick="$('#instSiteCodeDialog').dialog('center').dialog('open');"
                           style="padding-right: 15px;">选择位置</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="closeAdvaSearchDialogButton" style="text-align: right;">
    <!-- <a class="easyui-linkbutton" style="padding-left: 10px; padding-right: 10px;" onClick="$('#advaSearch').dialog('close')">关闭</a> -->
    <button class="btn btn-primary" onclick="$('#advaSearch').dialog('close')" style="margin-right:5px;">
        <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<div id="instSiteCodeDialog" class="easyui-dialog" title="添加安装位置" data-options="width:880, height:435, closed: true">
    <iframe class="embed-responsive-item" src="asset_save_place" style="width: 100%;height: 100%;" frameborder="no"
            border="0" marginwidth="0" marginheight="0" allowtransparency="yes"></iframe>
    <input name="rowIdHidden" type="hidden">
</div>

<!--资产类别选择-->
<div id="assetTypeDialog" class="easyui-dialog" title="添加资产类别" data-options="width:880, height:435, closed: true">
    <iframe class="embed-responsive-item" src="asset_type_place" style="width: 100%;height: 100%;" frameborder="no"
            border="0" marginwidth="0" marginheight="0" allowtransparency="yes"></iframe>
    <input name="rowIdHidden" type="hidden" value="${dynamicViewDto.assetType }">
</div>

<!--assetStanBookSubmit.backToLastPageUrl作为是否需要刷新datagrid的标志  -->
<input id="returnFromUpdateBatchFlag" type="hidden" value="${assetStanBookSubmit.backToLastPageUrl }">
<!--点击快速处理中信息批量修改的表单提交  -->
<form id="idListForm" action="asset_update_batch" style="display: none" method="post">
    <input id="assetIdList" name="assetIdList"/>
    <input id="backToLastPageUrl" name="backToLastPageUrl" value="asset_stan_book_material"/>
    <!--普通搜索中提交的参数  -->
    <input id="materialCodeSubmit" name="materialCodeSubmit" value="${assetStanBookSubmit.materialCodeSubmit }">
    <input id="assetCodeSubmit" name="assetCodeSubmit" value="${assetStanBookSubmit.assetCodeSubmit }">
    <input id="assetChsNameSubmit" name="assetChsNameSubmit" value="${assetStanBookSubmit.assetChsNameSubmit }">
    <input id="assetStatusSubmit" name="assetStatusSubmit" value="${assetStanBookSubmit.assetStatusSubmit }">
    <!--高级搜索中提交的参数  -->
    <input id="assetTypeSubmit" name="assetTypeSubmit" value="${assetStanBookSubmit.assetTypeSubmit }">
    <input id="manageDeptIdSubmit" name="manageDeptIdSubmit" value="${assetStanBookSubmit.manageDeptIdSubmit }">
    <input id="managerIdSubmit" name="managerIdSubmit" value="${assetStanBookSubmit.managerIdSubmit }">
    <input id="useDeptIdSubmit" name="useDeptIdSubmit" value="${assetStanBookSubmit.useDeptIdSubmit }">
    <input id="userIdSubmit" name="userIdSubmit" value="${assetStanBookSubmit.userIdSubmit }">
    <input id="savePlaceIdSubmit" name="savePlaceIdSubmit" value="${assetStanBookSubmit.savePlaceIdSubmit }">
    <input id="codeAndNameSubmit" name="codeAndNameSubmit" value="${assetStanBookSubmit.codeAndNameSubmit }">
    <input id="materialSearchCodeSubmit" name="materialSearchCodeSubmit"
           value="${assetStanBookSubmit.materialSearchCodeSubmit }">
</form>
<!--点击物资编码时提交的表单  -->
<form id="goToAssetViewDetailForm" action="asset_view_detailw" style="display: none" method="post">
    <input id="materialCode" name="materialCode"/>
    <input id="assetIdSet" name="floorNumId"/>
    <input id="materialSearchCode" name="materialSearchCode"/>
    <input id="assetCode" name="assetCode"/>
    <input id="assetChsName" name="assetChsName"/>
    <input id="assetStatusView" name="assetStatus"/>
    <input id="backToLastPageUrl" name="backToLastPageUrl" value="asset_stan_book_material"/>
</form>
</body>
</html>