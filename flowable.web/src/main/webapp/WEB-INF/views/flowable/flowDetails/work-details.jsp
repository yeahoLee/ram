<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="cn">
<head>
    <title>流程详情页</title>
    <jsp:include page="../../common/common-base-style.jsp"></jsp:include>
</head>
<body class="no-skin" style="background-color:#F5F5F5;">
	<!-- 固定菜单条 -->
	<div class="top-menu-bar">
		<div class="top-btns-navigation">
			<i class="fa fa-home"></i>
			<span class="navigatio-home" >个人工作台</span>
			<span class="glyphicon glyphicon-chevron-right" ></span>
			<span class="navigation-current-position" >流程详情页</span>
		</div>
		<!-- 更多按钮 -->
		<div class="top-btns-right">
		    <div class="dropdown">
				<button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			    	<span class="btn-font">更多...</span>
			    	<span class="caret"></span>
				</button>
				<ul class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenu">
				    <li><a href="#">Action</a></li>
				    <li><a href="#">Another action</a></li>
				    <li><a href="#">Something else here</a></li>
				    <li role="separator" class="divider"></li>
				    <li><a href="#">Separated link</a></li>
				</ul>
			</div>
		</div>
		<div class="top-btns-right">
			<button class="btn btn-success">
		        <i class="fa fa-check align-top bigger-125"></i>
		        <span class="btn-font">同意</span>
		    </button>
		    <button class="btn btn-danger">
		        <i class="fa fa-remove align-top bigger-125"></i>
		        <span class="btn-font">不同意</span>
		    </button>
		    <button class="btn btn-primary">
		        <i class="fa fa-hand-stop-o align-top bigger-125"></i>
		        <span class="btn-font">驳回</span>
		    </button>
		    <button class="btn btn-primary">
		        <i class="fa fa-undo align-top bigger-125"></i>
		        <span class="btn-font">退回</span>
		    </button>
	    </div>
	</div>
	<!-- 主题内容 -->
	<div class="details-main-body">
		<!-- div1 -->
		<div class="col-lg-12 col-md-12 col-sm-12">
			<table class="table table-bordered">
				<tr>
					<td class="table-head" colspan="6">基本信息</td>
				</tr>
				<tr>
			    	<td class="table-title">
			    		<i class="fa fa-list-ol"></i>
			    		流程单号
			    	</td>
			    	<td class="table-content"></td>
			    	<td class="table-title">
			    		<i class="fa fa-list-ol"></i>
			    		流程标题
		    		</td>
			    	<td class="table-content"></td>
			    	<td class="table-title">
			    		<i class="fa fa-list-ol"></i>
			    		流程发起人
		    		</td>
			    	<td class="table-content"></td>
				</tr>
				<tr>
			    	<td class="table-title">
			    		<i class="fa fa-list-ol"></i>
			    		流程发起时间
			    	</td>
			    	<td class="table-content"></td>
			    	<td class="table-title">
			    		<i class="fa fa-list-ol"></i>
			    		上一环节发送人
		    		</td>
			    	<td class="table-content"></td>
			    	<td class="table-title">
			    		<i class="fa fa-list-ol"></i>
			    		上一环节发送时间
		    		</td>
			    	<td class="table-content"></td>
				</tr>
				<tr>
			    	<td class="table-title">
			    		<i class="fa fa-list-ol"></i>当前环节
			    	</td>
			    	<td class="table-content"></td>
			    	<td class="table-title"></td>
			    	<td class="table-content"></td>
			    	<td class="table-title"></td>
			    	<td class="table-content"></td>
				</tr>
			</table>
		</div>
		<!-- div2 -->
		<div class="col-lg-12 col-md-12 col-sm-12">
			<table class="table table-bordered">
				<tr>
					<td class="table-head">流程审批记录</td>
				</tr>
				<tr>
					<td>
						<table id="datagrid" style="height: 415px;"></table>
					</td>
				</tr>
			</table>
		</div>
		<!-- div3 -->
		<div class="col-lg-12 col-md-12 col-sm-12">
			<table class="table table-bordered">
				<tr>
					<td class="table-head" colspan="2">审批意见</td>
				</tr>
				<tr>
					<td class="table-title">
						<i class="fa fa-comment"></i>复审意见
					</td>
					<td>
                        <textarea class="textbox" style="height:110px; width:100%; resize: none;" wrap="hard"></textarea>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<jsp:include page="../../common/common-base-javascript.jsp"></jsp:include>
	<script src="resources/js/personal/work-details.js"></script>
</body>
</html>