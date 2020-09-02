<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>固定资产动态视图</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/assetDynamicView.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }
    </style>
</head>
<body>
<div class="easyui-layout" fit="true">
    <div data-options="region:'center',border:false">
        <table id="dataGridTable"></table>
    </div>
</div>
<div id="creatBt">
    <button class="btn btn-primary" onclick="openCreateDialog()"
            style="margin-left:5px; margin-top: 5px; margin-bottom: 5px;">
        <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>增加视图
    </button>
</div>
<!--完成创建的dialog  -->
<div id="createDynamicViewDialog" class="easyui-dialog" title="增加视图"
     data-options="width:800, closed: true, buttons:'#createDynamicViewDialogButton'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-list-alt"></i>视图名称：</div>
                <input name="assetViewName" class="textbox"/>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-recycle"></i>资产状态：</div>
                <input id="assetStatus" name="assetStatus" class="easyui-combobox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray"><i class="fa fa-server"></i>安装位置：</div>
                <div class="gray" style="width:568px; text-align: left; padding-left: 15px;"><!-- width:500px; -->
                    <span id="showCodeAndData" style="position:relative;left:-15px"></span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a onclick="$('#instSiteCodeDialog').dialog('center').dialog('open');"
                       style="width:50px;padding-right: 15px;">选择位置</a>
                </div>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray"><i class="fa fa-tags"></i>资产类型：</div>
                <%--<input id="assetType" name="assetType" class="easyui-combobox"/>--%>
                <div class="gray" style="width:568px; text-align: left; padding-left: 15px;">
                    <span id="showAssetTypeCodeAndData" style="position:relative;left:-15px"></span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a id="showAssetTypeCodeAndDataButton"
                       onclick="$('#assetTypeDialog').dialog('center').dialog('open');"
                       style="width:50px;padding-right: 15px;">选择资产类别</a>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-sitemap"></i>管理部门：</div>
                <%--<input id="manageDept" name="manageDept" class="easyui-combobox"--%>
                <%--data-options="url:'base/dept_combo_by_querys?showType=0', method:'POST'"/>--%>
                <select id="manageDept" name="manageDept" class="easyui-combotree" style="width: 180px;"
                        data-options="url: 'base/get_dept_tree'"></select>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-sitemap"></i>使用部门：</div>
                <%--<input id="useDept" name="useDept" class="easyui-combobox" --%>
                <%--data-options="url:'base/dept_combo_by_querys?showType=0', method:'POST'"/>--%>
                <select id="useDept" name="useDept" class="easyui-combotree" style="width: 180px;"></select>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-user"></i>管理员：</div>
                <%--<input id="managerId" name="managerId" class="easyui-combobox" --%>
                <%--data-options="url:'base/user_combo', mode: 'remote', method:'POST'"/>--%>
                <input id="managerId" class="easyui-combobox" name="managerId"
                       data-options="url:'base/perm_user_datagrid_code?code=zcgly', mode: 'remote', method:'POST'"/>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-user"></i>使用人：</div>
                <%--<input id="userId" name="userId" class="easyui-combobox" --%>
                <%--data-options="url:'base/user_combo', mode: 'remote', method:'POST'"/>--%>
                <input id="userId" name="userId" class="easyui-combobox"/>
            </div>
        </div>
    </div>
</div>
<div id="createDynamicViewDialogButton" style="text-align: right;">
    <button id="createBtn" class="btn btn-primary" style="margin-right: 5px;" onClick="createDynamicView()">
        <i class="fas fa-save" style="margin-right: 5px;"></i>保存
    </button>
    <button id="closeBtn" class="btn btn-primary" style="margin-right: 5px;"
            onClick="$('#createDynamicViewDialog').dialog('close');">
        <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<div id="instSiteCodeDialog" class="easyui-dialog" title="添加安装位置" data-options="width:880, height:435, closed: true">
    <iframe class="embed-responsive-item" src="asset_save_place" style="width: 100%;height: 100%;" frameborder="no"
            border="0" marginwidth="0" marginheight="0" allowtransparency="yes"></iframe>
    <input name="rowIdHidden" type="hidden">
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
    <input name="rowIdHidden" type="hidden">
</div>

<!--完成修改的dialog  -->
<div id="modifyDynamicViewDialog" class="easyui-dialog" title="修改视图"
     data-options="width:800,height:240, closed: true, buttons:'#modifyDynamicViewDialogButton'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-list-alt"></i>视图名称：</div>
                <input name="assetViewName" class="textbox"/>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-recycle"></i>资产状态：</div>
                <input id="modifyAssetStatus" name="assetStatus" class="easyui-combobox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray"><i class="fa fa-server"></i>安装位置：</div>
                <div class="gray" style="width:566px; text-align: left; padding-left: 15px;"><!-- width:500px; -->
                    <span id="modifyDialogShowCodeAndData" style="position:relative;left:-15px"></span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a onclick="$('#instSiteCodeDialog').dialog('center').dialog('open');"
                       style="width:50px;padding-right: 15px;">选择位置</a>
                </div>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray"><i class="fa fa-tags"></i>资产类型：</div>
                <%--<input id="modifyAssetType" name="assetType" class="easyui-combobox"/>--%>
                <div class="gray" style="width:580px; text-align: left; padding-left: 15px;">
                    <span id="modifyShowAssetTypeCodeAndData" style="position:relative;left:-15px"></span>&nbsp;&nbsp;&nbsp;&nbsp;
                    <a onclick="$('#assetTypeDialog').dialog('center').dialog('open');" style="padding-right: 15px;">选择资产类别</a>
                </div>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-sitemap"></i>管理部门：</div>
                <%--<input id="manageDeptModify" name="manageDept" class="easyui-combobox" --%>
                <%--data-options="url:'base/dept_combo_by_querys?showType=0', method:'POST'"/>--%>
                <select id="manageDeptModify" name="manageDept" class="easyui-combotree" style="width: 180px;"
                        data-options="url: 'base/get_dept_tree'"></select>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-sitemap"></i>使用部门：</div>
                <%--<input id="useDeptModify" name="useDept" class="easyui-combobox" --%>
                <%--data-options="url:'base/dept_combo_by_querys?showType=0', method:'POST'"/>--%>
                <select id="useDeptModify" name="useDept" class="easyui-combotree" style="width: 180px;"
                        data-options="url: 'base/get_dept_tree'"></select>
            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-user"></i>管理员：</div>
                <%--<input id="managerIdModify" name="managerId" class="easyui-combobox" --%>
                <%--data-options="url:'base/user_combo', mode: 'remote', method:'POST'"/>--%>
                <input id="managerIdModify" name="managerId" class="easyui-combobox"
                       data-options="url:'base/perm_user_datagrid_code?code=zcgly', mode: 'remote', method:'POST'"/>

            </div>
            <div class="col-lg-6 col-md-6 col-sm-6">
                <div class="gray"><i class="fa fa-user"></i>使用人：</div>
                <%--<input id="userIdModify" name="userId" class="easyui-combobox" --%>
                <%--data-options="url:'base/user_combo', mode: 'remote', method:'POST'"/>--%>
                <input id="userIdModify" name="userId" class="easyui-combobox"/>
            </div>
            <input name="modifyDnamicViewHiddenId" type="hidden" value="defalut">
        </div>
    </div>
</div>
<div id="modifyDynamicViewDialogButton">
    <button class="btn btn-primary" onclick="modifyDynamicView()" style="margin-right:5px;">
        <i class="fas fa-save" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onclick="closeModifyDynamicView()" style="margin-right:5px;">
        <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
    </button>
</div>
</body>
</html>