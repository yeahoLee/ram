<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>角色权限列表</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/base/basePermission.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/base/baseCommon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox, .easyui-combotree {
            width: 180px;
        }
    </style>
</head>
<body>
<div class="easyui-layout" fit="true">
    <div data-options="region:'center',border:false">
        <table id="dataGridTable"></table>
        <div id="dataGridTableButtons">
            <button class="btn btn-primary" onClick="openCreatePermDialog();"
                    style="margin-top: 5px; margin-bottom: 5px; margin-right: 5px; margin-left: 5px;">
                <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>新增角色
            </button>
            <button class="btn btn-primary" onClick="openCreatePermItemListDialog();">
                <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>角色权限管理
            </button>
        </div>
    </div>
</div>
<!-- 权限组 start -->
<!-- 新增角色组 -->
<div id="createPermDialog" title="新角色组" class="easyui-dialog"
     data-options="width:650, height:225, closed:true, buttons:'#createPermDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">角色编码:</div>
                <input name="code" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">角色名称:</div>
                <input name="chsName" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">备注:</div>
                <textarea id="createRemark" class="textbox" style="width:70%;height:60px;resize: none;"></textarea>
            </div>
        </div>
    </div>
</div>
<div id="createPermDialogButtons">
    <button class="btn btn-primary" onClick="createPermSubmit();" style="margin-right: 5px; margin-left: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#createPermDialog').dialog('close');">
        <i class="fa fa-times bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 更新角色组 -->
<div id="updatePermDialog" title="更新角色组" class="easyui-dialog"
     data-options="width:650, height:225, closed:true, buttons:'#updatePermDialogButtons'">
    <input name="id" type="hidden"/>
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">权限组编码:</div>
                <input name="code" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">权限组名称:</div>
                <input name="chsName" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">备注:</div>
                <textarea id="updateRemark" class="textbox" style="width:70%;height:60px;resize: none;"></textarea>
            </div>
        </div>
    </div>
</div>
<div id="updatePermDialogButtons">
    <button class="btn btn-primary" onClick="updatePermSubmit();" style="margin-right: 5px; margin-left: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#updatePermDialog').dialog('close');">
        <i class="fa fa-times bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 权限组 end -->
<!-- 角色权限管理 start -->
<!-- 权限项列表 -->
<div id="permItemListDialog" title="角色权限列表" class="easyui-dialog" data-options="width:500, height:395, closed:true">
    <table id="permItemDataGridTable"></table>
    <div id="permItemDataGridTableButtons">
        <button class="btn btn-primary" onClick="$('#createPermItemDialog').dialog('center').dialog('open');"
                style="margin-right: 5px; margin-left: 5px; margin-top: 5px; margin-bottom: 5px;">
            <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>新增角色权限
        </button>
    </div>
</div>
<!-- 新增权限项 -->
<div id="createPermItemDialog" title="新增角色权限" class="easyui-dialog"
     data-options="width:400,closed:true,buttons:'#createPermItemDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">角色权限编码:</div>
                <input name="code" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">角色权限名称:</div>
                <input name="chsName" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">菜单权限名称:</div>
                <input id="createMenuPermName" class="easyui-combobox"
                       data-options="url:'base/menu_perm_combobox', method:'POST', editable:false"/>
            </div>
        </div>
    </div>
</div>
<div id="createPermItemDialogButtons">
    <button class="btn btn-primary" onClick="createPermItemSubmit();" style="margin-right: 5px; margin-left: 5px;">
        <i class="fas fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#createPermItemDialog').dialog('close');">
        <i class="fa fa-times bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 更新权限项 -->
<div id="updatePermItemDialog" title="更新角色权限" class="easyui-dialog"
     data-options="width:400,closed:true,buttons:'#updatePermItemDialogButtons'">
    <input name="id" type="hidden"/>
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">权限项编码:</div>
                <input name="code" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">权限项名称:</div>
                <input name="chsName" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">菜单权限名称:</div>
                <input id="updateMenuPermName" class="easyui-combobox"
                       data-options="url:'base/menu_perm_combobox', method:'POST', editable:false"/>
            </div>
        </div>
    </div>
</div>
<div id="updatePermItemDialogButtons">
    <button class="btn btn-primary" onClick="updatePermItemSubmit();" style="margin-right: 5px; margin-left: 5px;">
        <i class="fas fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#updatePermItemDialog').dialog('close');">
        <i class="fa fa-times bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 角色权限管理 end -->
<!-- 角色用户 -->
<div id="permUserListDialog" title="角色用户列表" class="easyui-dialog" data-options="width:500, height:395, closed:true">
    <input name="permGroupId" type="hidden">
    <input id="permCode" type="hidden">
    <table id="permUserDataGridTable"></table>
    <div id="permUserDataGridTableButtons" style="padding-left: 5px;">
        <input id="userId" class="textbox"/>
        <button class="btn btn-primary" onClick="searchByQuerys();"
                style="margin-top: 5px; margin-bottom: 5px; margin-left: 5px;">
            <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>搜索
        </button>
        <button class="btn btn-primary" onClick="openAddUserToPermGroupDialog();"
                style="margin-top: 5px; margin-bottom: 5px; margin-left: 5px;">
            <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>新增角色用户
        </button>
    </div>
</div>
<!-- 菜单权限 -->
<div id="menuPermListDialog" title="菜单权限列表" class="easyui-dialog"
     data-options="width:500, height:395, closed:true, buttons:'#menuPermListDialogButtons'">
    <input name="permGroupId" type="hidden">
    <ul id="menuPermTree" class="easyui-tree" data-options="lines:true">
</div>
<div id="menuPermListDialogButtons">
    <button class="btn btn-primary" onClick="setMenuPermListSubmit();"
            style="margin-top: 5px; margin-bottom: 5px; margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-exchange align-top bigger-125" style="margin-right: 5px;"></i>变更
    </button>
    <button class="btn btn-primary" onClick="$('#menuPermListDialog').dialog('close');">
        <i class="fa fa-times bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 角色权限 -->
<div id="menuPermItemListDialog" title="角色权限列表" class="easyui-dialog"
     data-options="width:500, height:395, closed:true, buttons:'#menuPermItemListDialogButtons'">
    <input name="permGroupId" type="hidden">
    <ul id="menuPermItemTree" class="easyui-tree" data-options="lines:true">
</div>
<div id="menuPermItemListDialogButtons">
    <button class="btn btn-primary" onClick="setMenuPermItemListSubmit();"
            style="margin-top: 5px; margin-bottom: 5px; margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-exchange align-top bigger-125" style="margin-right: 5px;"></i>变更
    </button>
    <button class="btn btn-primary" onClick="$('#menuPermItemListDialog').dialog('close');">
        <i class="fa fa-times bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 角色用户 start -->
<!-- 新增角色用户 -->
<div id="addUserToPermGroupDialog" title="新增角色用户" class="easyui-dialog"
     data-options="width:400, height:150, closed:true, buttons:'#addUserToPermGroupDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <input name="permGroupId" type="hidden"/>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">部门名称:</div>
                <select id="deptIdAddCombobox" class="easyui-combobox"></select>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">用户名称:</div>
                <input id="userIdAddCombobox" class="easyui-combobox"/>
            </div>
            <!-- <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">资产管理员:</div>
                <label style="float:left;font-size:12px;">
                    <input name="manager" type="radio" value="true" style="vertical-align:middle; margin-top:1px; margin-bottom:1px;">&nbsp;&nbsp;&nbsp;&nbsp;是
                  </label>&nbsp;&nbsp;&nbsp;&nbsp;
                  <label style="font-size:12px;">
                    <input name="manager" type="radio" value="false" checked="true" style="vertical-align:middle; margin-top:1px; margin-bottom:1px;">&nbsp;&nbsp;&nbsp;&nbsp;否 
                  </label>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">部门领导:</div>
                <label style="float:left;font-size:12px;">
                    <input name="leader" type="radio" value="true" style="vertical-align:middle; margin-top:1px; margin-bottom:1px;">&nbsp;&nbsp;&nbsp;&nbsp;是
                  </label>&nbsp;&nbsp;&nbsp;&nbsp;
                  <label style="font-size:12px;">
                    <input name="leader" type="radio" value="false" checked="true" style="vertical-align:middle; margin-top:1px; margin-bottom:1px;">&nbsp;&nbsp;&nbsp;&nbsp;否 
                  </label>
            </div> -->
        </div>
    </div>
</div>
<div id="addUserToPermGroupDialogButtons">
    <button class="btn btn-primary" onClick="addUserToPermGroupSubmit();" style="margin-right: 5px; margin-left: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#addUserToPermGroupDialog').dialog('close');">
        <i class="fa fa-times bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 更新角色用户 -->
<!-- 个人权限  -->
<div id="psersonMenuPermItemListDialog" title="个人权限" class="easyui-dialog"
     data-options="width:500,height:395, closed:true, buttons:'#psersonMenuPermItemListDialogButtons'">
    <input name="personId" type="hidden">
    <input name="deptId" type="hidden">
    <ul id="personMenuPermItemTree" class="easyui-tree" data-options="lines:true">
</div>
<div id="psersonMenuPermItemListDialogButtons">
    <button class="btn btn-primary" onClick="setPersonMenuPermItemListSubmit();"
            style="margin-top: 5px; margin-bottom: 5px; margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-exchange align-top bigger-125" style="margin-right: 5px;"></i>变更
    </button>
    <button class="btn btn-primary" onClick="$('#psersonMenuPermItemListDialog').dialog('close');">
        <i class="fa fa-times bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 角色用户 end  -->
</body>
</html>