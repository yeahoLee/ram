<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>用户列表</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/base/baseUserIndex.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/base/baseCommon.css">
</head>
<body>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12"
                     style="height: 35px; padding-left: 5px; font-size: 13px;">
                    模糊查询：<input id="searchFilterText" class="textbox" style="margin-right: 5px;"/>
                    <button class="btn btn-primary" onClick="searchByQuerys();" style="margin-right: 5px;">
                        <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>搜索
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div data-options="region:'center',border:false">
        <table id="dataGridTable"></table>
        <div id="dataGridTableButtons">
            <button class="btn btn-primary" onClick="openDialog('createUserDialog');"
                    style="margin-left: 5px; margin-right: 5px; margin-top: 5px; margin-bottom: 5px;">
                <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>新增
            </button>
            <button class="btn btn-primary" onClick="syncCom();" style="margin-right: 5px;">
                <i class="fa fa-spinner align-top bigger-125" style="margin-right: 5px;"></i>同步用户（UUV）
            </button>
            <a class="btn btn-primary" href="resources/downLoadFile/importUserInfo.xls" style=" margin-right: 5px;">
                <i class="fa fa-download align-top bigger-125" style="margin-right: 5px;"></i>下载模板（.xls）
            </a>
            <button class="btn btn-primary" onClick="openDialog('importUserXlsDialog');" style=" margin-right: 5px;">
                <i class="fa fa-upload align-top bigger-125" style="margin-right: 5px;"></i>导入用户（.xls）
            </button>
        </div>
    </div>
</div>
<!-- 新增 -->
<div id="createUserDialog" title="新增" class="easyui-dialog"
     data-options="width:400,closed:true,buttons:'#createUserDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <!-- <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">用户编码:</div>
                <input name="userCode" class="textbox" />
            </div> -->
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">用户名:</div>
                <input name="userName" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">中文名:</div>
                <input name="chsName" class="textbox"/>
            </div>
        </div>
    </div>
</div>
<div id="createUserDialogButtons">
    <button class="btn btn-primary" onClick="createUserSubmit();" style="margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#createUserDialog').dialog('close');" style="margin-right: 5px;">
        <i class="fa fa-close align-top bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 更新 -->
<div id="updateUserDialog" title="更新" class="easyui-dialog"
     data-options="width:400,closed:true,buttons:'#updateUserDialogButtons'">
    <input name="id" type="hidden"/>
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <!-- <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">用户名:</div>
                <input name="userCode" class="textbox" />
            </div> -->
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">用户名:</div>
                <input name="userName" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">中文名:</div>
                <input name="chsName" class="textbox"/>
            </div>
        </div>
    </div>
</div>
<div id="updateUserDialogButtons">
    <button class="btn btn-primary" onClick="updateUserSubmit();" style="margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#updateUserDialog').dialog('close');" style="margin-right: 5px;">
        <i class="fa fa-close align-top bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 导入 -->
<div id="importUserXlsDialog" class="easyui-dialog" title="导入文件"
     data-options="closed:true, width:400, buttons:'#importUserXlsDialogButtons'">
    <form id="importForm" method="POST" enctype="multipart/form-data"
          style="margin-left: 5px; margin-bottom: 5px; margin-top: 5px;">
        <input type="file" name="uploadFileData"/>
    </form>
</div>
<div id="importUserXlsDialogButtons">
    <button class="btn btn-primary" onClick="importUserXls();" style="margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-upload align-top bigger-125" style="margin-right: 5px;"></i>导入
    </button>
    <button class="btn btn-primary" onClick="$('#importUserXlsDialog').dialog('close');" style="margin-right: 5px;">
        <i class="fa fa-close align-top bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
</body>
</html>