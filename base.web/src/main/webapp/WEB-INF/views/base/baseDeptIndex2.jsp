<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>部门列表</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/base/baseDeptIndex2.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/base/baseCommon.css">
</head>
<body>
<div class="easyui-layout" fit="true">
    <div data-options="region:'center',border:false">
        <table id="dataGridTable"  >></table>
        <div id="dataGridTableButtons">
            <button class="btn btn-primary" onClick="openCreateDeptDialig();"
                    style="margin-left: 5px; margin-right: 5px; margin-top: 5px; margin-bottom: 5px;">
                <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>新增部门
            </button>
        </div>
    </div>
</div>
<!-- 赋值用户 -->
<div id="deptUserListDialog" title="人员设置" class="easyui-dialog" data-options="width:400, height:395, closed:true">
    <input name="deptId" type="hidden">
    <table id="deptUserDataGridTable"></table>
    <div id="permUserDataGridTableButtons">
        <button class="btn btn-primary" onClick="openAddUserToDeptDialog()"
                style="margin-top: 5px; margin-bottom: 5px; margin-left: 5px; margin-right: 5px;">
            <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>新增人员
        </button>
        <input name="userId" class="textbox"/>
        <button class="btn btn-primary" onClick="searchByQuerys();"
                style="margin-top: 5px; margin-bottom: 5px; margin-left: 5px;">
            <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>搜索
        </button>
        <!-- <button class="btn btn-primary" onClick="deleteDeptUserSubmit();" style="margin-left: 5px;">
            <i class="fa fa-trash align-top bigger-125" style="margin-right: 5px;"></i>删除
        </button> -->
    </div>
</div>
<!-- 新增部门 -->
<div id="createDeptDialog" title="新增部门" class="easyui-dialog"
     data-options="width:400,closed:true,buttons:'#createDeptDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">父类中文:</div>
                <div><input id="pdId" class="easyui-combobox"
                            data-options="url:'base/dept_combo_by_querys?showType=0', method:'POST'"/></div>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">编码:</div>
                <input name="deptCode" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">中文名:</div>
                <input name="deptName" class="textbox"/>
            </div>
        </div>
    </div>
</div>
<div id="createDeptDialogButtons">
    <button class="btn btn-primary" onClick="createDeptSubmit();" style="margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#createDeptDialog').dialog('close');" style="margin-right: 5px;">
        <i class="fa fa-close align-top bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 新增用户 -->
<div id="addUserToDeptDialog" title="新增人员" class="easyui-dialog"
     data-options="width:400, height:180,closed:true, buttons:'#addUserToDeptDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">用户名：</div>
                <input id="userId" class="easyui-combobox"
                       data-options="url:'base/user_combo', mode: 'remote', method:'POST'"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">资产管理员：</div>
                <label style="font-size:12px;">
                    <input name="manager" type="radio" value="true"
                           style="vertical-align:middle; margin-top:1px; margin-bottom:1px;">&nbsp;&nbsp;&nbsp;&nbsp;是
                </label>&nbsp;&nbsp;&nbsp;&nbsp;
                <label style="font-size:12px;">
                    <input name="manager" type="radio" value="false" checked="true"
                           style="vertical-align:middle; margin-top:1px; margin-bottom:1px;">&nbsp;&nbsp;&nbsp;&nbsp;否 
                </label>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">部门领导：</div>
                <label style="float:left;font-size:12px;">
                    <input name="leader" type="radio" value="true"
                           style="vertical-align:middle; margin-top:1px; margin-bottom:1px;">&nbsp;&nbsp;&nbsp;&nbsp;是
                </label>&nbsp;&nbsp;&nbsp;&nbsp;
                <label style="font-size:12px;">
                    <input name="leader" type="radio" value="false" checked="true"
                           style="vertical-align:middle; margin-top:1px; margin-bottom:1px;">&nbsp;&nbsp;&nbsp;&nbsp;否 
                </label>
            </div>
        </div>
    </div>
</div>
<div id="addUserToDeptDialogButtons">
    <button class="btn btn-primary" onClick="setDeptUserSubmit();" style="margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#addUserToDeptDialog').dialog('close');" style="margin-right: 5px;">
        <i class="fa fa-close align-top bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
</body>
</html>