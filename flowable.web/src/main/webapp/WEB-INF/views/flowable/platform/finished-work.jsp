<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="cn">
<head>
	<title>已办工作</title>
	<jsp:include page="../../libInclude.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="resources/css/base/baseCommon.css">
	<script src="resources/js/flowable/platform/finished-work.js"></script>
	<script src="resources/js/common/common.js"></script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false">
		<div class="container-fluid">
			<div class="row" style="margin-top: 5px;">
				<div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 40px;">
					<form id="finishedSearchForm" action="" onsubmit="return false" style="margin-bottom: 0px">
						<div  style="float:left; margin-top: 5px;">
							<div style="margin-right: 8px;float: left;">
								流程标题：
								<input id="formName" class="textbox"/>
							</div>
						</div>
						<div style="float:right;">
							<button class="btn btn-primary" onClick="searchByQuerys();">
								<i class="fa fa-search" style="margin-right: 5px;"></i>搜索</span>
							</button>
							<button class="btn btn-primary" onClick="searchReset();">
								<i class="fa fa-refresh" style="margin-right: 5px;"></i>重置</span>
							</button>
						</div>
					</form>
				</div>
			</div>

		</div>
	</div>
	<div data-options="region:'center',border:false">
		<table id="finishListTable"></table>
	</div>
</div>
</body>
</html>


