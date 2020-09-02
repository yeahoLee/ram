<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>数据字典</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/base/baseDictionary.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/base/baseCommon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }

        .radioCss {
            vertical-align: middle;
            margin-top: -2px;
            margin-bottom: 1px;
        }
    </style>
</head>
<body>
<div class="easyui-layout" fit="true">
    <div data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div id="searchQuerys" class="col-lg-12 col-md-12 col-sm-12" style="height: 35px; padding-left: 5px;">
                    <!-- 					<button class="btn btn-primary" onClick="$('#createCommonCodeTypeDialog').dialog('center').dialog('open');" style="margin-right: 5px;"> -->
                    <!-- 					    <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>新增类型 -->
                    <!-- 					</button>  -->
                    字典类型:
                    <input id="commonCodeType" class="easyui-combobox"
                           data-options="url:'base/dict_type_combo',method:'POST'"/>
                    <button class="btn btn-primary" onClick="searchByQuerys();" style="margin-left: 5px;">
                        <i class="fa fa-search align-top bigger-125" style="margin-right: 5px;"></i>搜索
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div data-options="region:'center',border:false">
        <table id="dataGridTable"></table>
    </div>
    <div id="dataGridTableButtons">
        <button class="btn btn-primary" onClick="openCreateCommonCodeDialog();"
                style="margin-left: 5px; margin-right: 5px; margin-top: 5px; margin-bottom: 5px;">
            <i class="fa fa-plus align-top bigger-125" style="margin-right: 5px;"></i>新增
        </button>
        <!-- <button class="btn btn-primary" onClick="$('#importSavePlaceXlsDialog').dialog('center').dialog('open')" style=" margin-right: 5px;">
            <i class="fa fa-download align-top bigger-125" style="margin-right: 5px;"></i>导入安装位置（.xls）
        </button> -->
        <button class="btn btn-primary" onClick="$('#importFileDialog').dialog('center').dialog('open')"
                style=" margin-right: 5px;">
            <i class="fa fa-download align-top bigger-125" style="margin-right: 5px;"></i>导入
        </button>
    </div>
</div>
<!-- 新增 -->
<div id="createCommonCodeDialog" title="新增" class="easyui-dialog"
     data-options="width:400,closed:true,buttons:'#createCommonCodeDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">父类中文:</div>
                <div><input id="parentDictCommonCodeNameInCreateDialog" class="easyui-combobox"/></div>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">编码:</div>
                <input name="code" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">中文名:</div>
                <input name="chsName" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">英文名:</div>
                <input name="engName" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">覆盖编码:</div>
                <input name="converCode" class="textbox"/>
            </div>
        </div>
    </div>
</div>
<div id="createCommonCodeDialogButtons">
    <button class="btn btn-primary" onClick="createCommonCodeSubmit();" style="margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#createCommonCodeDialog').dialog('close');" style="margin-right: 5px;">
        <i class="fa fa-close align-top bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 修改 -->
<div id="updateCommonCodeDialog" title="修改" class="easyui-dialog"
     data-options="width:400,closed:true,buttons:'#updateCommonCodeDialogButtons'">
    <input name="id" type="hidden"/>
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">父类中文:</div>
                <div><input id="parentDictCommonCodeNameInUpdateDialog" class="easyui-combobox"/></div>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">编码:</div>
                <input name="code" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">中文名:</div>
                <input name="chsName" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">英文名:</div>
                <input name="engName" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">覆盖编码:</div>
                <input name="converCode" class="textbox"/>
            </div>
        </div>
    </div>
</div>
<div id="updateCommonCodeDialogButtons">
    <button class="btn btn-primary" onClick="updateCommonCodeSubmit();" style="margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#updateCommonCodeDialog').dialog('close');" style="margin-right: 5px;">
        <i class="fa fa-close align-top bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 新增类型 -->
<div id="createCommonCodeTypeDialog" title="新增类型" class="easyui-dialog"
     data-options="width:400,closed:true,buttons:'#createCommonCodeTypeDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">编码:</div>
                <input name="key" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">中文名:</div>
                <input name="name" class="textbox"/>
            </div>
        </div>
    </div>
</div>
<div id="createCommonCodeTypeDialogButtons">
    <button class="btn btn-primary" onClick="createCommonCodeTypeSubmit();"
            style="margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#createCommonCodeTypeDialog').dialog('close');"
            style="margin-right: 5px;">
        <i class="fa fa-close align-top bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 导入文件 -->
<!-- <div id="importSavePlaceXlsDialog" class="easyui-dialog" title="导入文件" data-options="closed:true,width:400,buttons:'#importSavePlaceXlsDialogButtons'">
	<form id="importForm" method="POST" enctype="multipart/form-data">
		<input type="file" name="uploadFileData" />
  	</form>
</div>
<div id="importSavePlaceXlsDialogButtons">	
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="importSavePlaceXls()">确定</a>
	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="$('#importSavePlaceXlsDialog').dialog('close')">取消</a>
</div> -->
<!--一般导入  -->
<div id="importFileDialog" class="easyui-dialog" title="导入文件"
     data-options="closed:true,width:500,buttons:'#importFileDialogButtons'">
    <form id="importFormm" method="POST" enctype="multipart/form-data">
        字典类型：<input name="dictType" class="easyui-combobox"
                    data-options="url:'base/dict_type_combo',method:'POST'"/><br>
        父字典类型：<input name="dictPdType" class="easyui-combobox" data-options="url:'base/dict_type_combo',method:'POST'"/>
        <input type="file" name="uploadFileData"/>
    </form>
</div>
<div id="importFileDialogButtons">
    <button class="btn btn-primary" onClick="importFile();" style="margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#importFileDialog').dialog('close');" style="margin-right: 5px;">
        <i class="fa fa-close align-top bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
<!-- 新增 -->
<div id="createMaterialsCodeDialog" title="新增" class="easyui-dialog"
     data-options="width:400,height:350,closed:true,buttons:'#createMaterialsCodeDialogButtons'">
    <div class="container-fluid">
        <div class="row" style="margin-top: 5px;">
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">编码:</div>
                <input name="code" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">中文名:</div>
                <input name="chsName" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">物资属性:</div>
                <input name="W_PRO_CODE" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">物资类型:</div>
                <input name="W_TYPE_CODE" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">参考单价:</div>
                <input name="PRICE" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">规格型号:</div>
                <input name="MARTERIALS_SPEC" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">计量单位:</div>
                <input name="W_UNIT_CODE" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">是否进设备台账:</div>
                <label style="float:left; font-size:12px;">
                    &nbsp;&nbsp;是&nbsp;&nbsp;<input name="W_IS_PRO" type="radio" class="radioCss" value="true"
                                                    checked="true"/>
                </label>
                <label style="font-size:12px;">
                    &nbsp;&nbsp;否&nbsp;&nbsp;<input name="W_IS_PRO" type="radio" class="radioCss" value="false"/>
                </label>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">是否危化品:</div>
                <label style="float:left; font-size:12px;">
                    &nbsp;&nbsp;是&nbsp;&nbsp;<input name="IS_DAN" type="radio" class="radioCss" value="true"
                                                    checked="true"/>
                </label>
                <label style="font-size:12px;">
                    &nbsp;&nbsp;否&nbsp;&nbsp;<input name="IS_DAN" type="radio" class="radioCss" value="false"/>
                </label>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">是否直发:</div>
                <label style="float:left; font-size:12px;">
                    &nbsp;&nbsp;是&nbsp;&nbsp;<input name="IS_DIRECT" type="radio" class="radioCss" value="true"
                                                    checked="true"/>
                </label>
                <label style="font-size:12px;">
                    &nbsp;&nbsp;否&nbsp;&nbsp;<input name="IS_DIRECT" type="radio" class="radioCss" value="false"/>
                </label>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">使用状态:</div>
                <input id="MARTERIALS_STATE" class="easyui-combobox" data-options="
					valueField: 'value',
					textField: 'text',
					data: [{
						text: '停用',
						value: '0'
					},{
						text: '启用',
						value: '1'
					},{
						text: '删除',
						value: '2'
					}]"/>

            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">品牌名称:</div>
                <input name="BRAND_NAME" class="textbox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">保质期:</div>
                <input name="EXPIRATION_DATE" class="easyui-datebox"/>
            </div>
            <div class="col-lg-12 col-md-12 col-sm-12">
                <div class="gray">删除标志:</div>
                <label style="float:left; font-size:12px;">
                    &nbsp;&nbsp;是&nbsp;&nbsp;<input name="IS_DEL" type="radio" class="radioCss" value="true"
                                                    checked="true"/>
                </label>
                <label style="font-size:12px;">
                    &nbsp;&nbsp;否&nbsp;&nbsp;<input name="IS_DEL" type="radio" class="radioCss" value="false"/>
                </label>
            </div>
        </div>
    </div>
</div>
<div id="createMaterialsCodeDialogButtons">
    <button class="btn btn-primary" onClick="createMaterialsCodeSubmit();" style="margin-left: 5px; margin-right: 5px;">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button class="btn btn-primary" onClick="$('#createMaterialsCodeDialog').dialog('close');"
            style="margin-right: 5px;">
        <i class="fa fa-close align-top bigger-125" style="margin-right: 5px;"></i>关闭
    </button>
</div>
</body>
</html>