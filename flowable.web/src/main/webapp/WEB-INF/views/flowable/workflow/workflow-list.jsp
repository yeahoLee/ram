<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="cn">
<head>
	<title>流程管理</title>
	<jsp:include page="../../libInclude.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="resources/css/base/baseCommon.css">
	<script src="resources/js/flowable/workflow/workflow-list.js"></script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false">
		<div class="container-fluid">
			<div class="row" style="margin-top: 5px;">
				<div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 40px;">
					<form id="workFlowSearchForm" style="margin-bottom: 0px">
						<div  style="float:left; margin-top: 5px;">
							<div style="margin-right: 8px;float: left;">
								流程标题：
								<input id="qTitle" class="textbox"/>
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
		<table id="workflowListTable"></table>
	</div>
	<div id="addButton" class="button-padding">
		<button class="btn btn-primary" style="margin:5px;" onClick="openAddWorkFlowDialog();">
			<i class="fa fa-plus" style="margin-right: 5px;"></i>新增</span>
		</button>
	</div>
</div>

<!-- 新增流程 -->
<div id="addWorkFlowDialog" title="新增/修改" class="easyui-dialog" data-options="width:400,closed:true,buttons:'#addWorkFlowDialogButtons'">
	<div class="container-fluid">
		<div class="row" style="margin-top: 5px;">
			<form id="addWorkFlowForm">
				<input id = "id" name="id" type="hidden">
				<div class="col-lg-12 col-md-12 col-sm-12">
					<div class="gray">流程名称:</div>
					<input id="flowName" name="flowName"/>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12">
					<div class="gray">流程key:</div>
					<input id="flowKey" name="flowKey" class="textbox"  readonly="readonly"/>
				</div>
				<div class="col-lg-12 col-md-12 col-sm-12">
					<div class="gray">流程描述:</div>
					<input id="flowDescription" name="flowDescription" class="textbox" />
				</div>
			</form>
		</div>
	</div>
</div>
<div id="addWorkFlowDialogButtons">
	<button class="btn btn-primary" onClick="createWorkFlow();" style="margin-left: 5px; margin-right: 5px;">
		<i class="fa fa-save" style="margin-right: 5px;"></i>保存
	</button>
	<button class="btn btn-primary" onClick="$('#addWorkFlowDialog').dialog('close');" style="margin-right: 5px;">
		<i class="fa fa-close" style="margin-right: 5px;"></i>关闭
	</button>
</div>

<!-- 添加用户组 -->
<div id="groupUserDialog" class="easyui-dialog" title="添加用户组"
	 data-options="closed:true, width:980, buttons:'#groupUserDialogButtons'">
	<div class="container-fluid">
		<input name = "flowId" type="hidden">
		<input name = "key" type="hidden">
		<div class="row" style="margin-top: 5px;">
			<div class="col-lg-12 col-md-12 col-sm-12" style="height: 35px;">
				环节：<input id="process" name="process" style="margin-right: 5px;"/>
				用户组名：<input id="groupId" name="groupName" class="easyui-combobox" data-options="url:'flowable/group_user_combo',method:'GET'" style="margin-right: 5px;"/>
				<button class="btn btn-primary" onClick="addUserGroup();" style="margin-left:5px; margin-right: 5px;">
					<i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>保存
				</button>
			</div>
		</div>
	</div>
	<table id="processGroupDataGridTable" style="height: 350px;"></table>
</div>
<div id="groupUserDialogButtons">
	<button class="btn btn-primary" onclick="$('#groupUserDialog').dialog('close');" style="margin-right:5px;">
		<i class="fa fa-times" style="margin-right: 5px;"></i>关闭
	</button>
</div>
</body>
</html>
