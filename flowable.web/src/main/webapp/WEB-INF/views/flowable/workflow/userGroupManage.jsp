<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="cn">
<head>
    <title>用户组管理</title>
    <jsp:include page="../../libInclude.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="resources/css/base/baseCommon.css">
    <script src="resources/js/flowable/workflow/userGroupManage.js"></script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 40px;">
                    <form id="userGroupSearchForm" style="margin-bottom: 0px">
                        <div  style="float:left; margin-top: 5px;">
                            <div style="margin-right: 8px;float: left;">
                                用户组名：
                                <input id="qName" class="textbox"/>
                            </div>
                        </div>
                        <div style="float:right;">
                            <button class="btn btn-primary" onClick="searchByQuerys();">
                                <i class="fa fa-search" style="margin-right: 5px;"></i>搜索</span>
                            </button>
                            <button class="btn btn-primary" onClick="resetQuerys();">
                                <i class="fa fa-refresh" style="margin-right: 5px;"></i>重置</span>
                            </button>
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </div>
    <div data-options="region:'center',border:false">
        <table id="userGroupTable"></table>
    </div>
    <div id="addButton" class="button-padding">
        <button class="btn btn-primary" style="margin:5px;" onClick="openAddUserGroupDialog();">
            <i class="fa fa-plus" style="margin-right: 5px;"></i>新增</span>
        </button>
    </div>
</div>

<!-- 新增用户组 -->
<div id="addUserGroupDialog" title="新增/修改" class="easyui-dialog" data-options="width:400,closed:true,buttons:'#addUserGroupDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <form id="addUserGroupForm">
                <input id = "id" name="id" type="hidden">
                <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="gray">编码:</div>
                    <input id="groupCode" name="groupCode" class="textbox" />
                </div>
                <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="gray">名称:</div>
                    <input id="groupName" name="groupName" class="textbox" />
                </div>
                <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="gray">描述:</div>
                    <input id="groupDescription" name="groupDescription" class="textbox" />
                </div>
            </form>
        </div>
    </div>
</div>
<div id="addUserGroupDialogButtons">
    <button class="btn btn-primary" onClick="createUserGroup();" style="margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-save" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#addUserGroupDialog').dialog('close');" style="margin-right: 5px;">
        <i class="fa fa-close" style="margin-right: 5px;"></i>关闭
    </button>
</div>

<!-- 添加用户 -->
<div id="userDialog" class="easyui-dialog" title="添加用户"
     data-options="closed:true, width:980, buttons:'#userDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <input id="groupId" type="hidden">
            <div class="col-lg-12 col-md-12 col-sm-12" style="height: 35px;">
                部门名称：<input id="deptIdAddCombobox" class="easyui-combotree"/>
                用户名称：<input id="userId" class="easyui-combobox" data-options = "valueField:'value1',textField:'text',"style="margin-right: 5px;"/>
                <button class="btn btn-primary" onClick="addUser();" style="margin-left:5px; margin-right: 5px;">
                    <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>添加
                </button>
            </div>
        </div>
    </div>
    <table id="userDataGridTable" style="height: 350px;"></table>
</div>
<div id="userDialogButtons">
    <button class="btn btn-primary" onclick="$('#userDialog').dialog('close');" style="margin-right:5px;">
        <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
    </button>
</div>
</body>
</html>
