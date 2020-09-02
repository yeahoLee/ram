<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>盘点单编辑</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/asset/myAssetInventoryCreate.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommon.css">
    <link rel="stylesheet" type="text/css" href="resources/css/asset/assetCommonIcon.css">
    <style type="text/css">
        .textbox, .easyui-combobox, .easyui-datebox {
            width: 180px;
        }

        .background_color {
            background-color: #F8F9F9;
            line-height: 25px;
            text-align: center;
        }

        .td {
            line-height: 25px;
            text-align: center;
        }
    </style>
</head>
<body>
<input id="UserID" value="${userInfoDto.id }" type="hidden"/>
<input id="DeptID" value="${loginDeptDto.loginDeptDto.id }" type="hidden"/>
<input id="ChsName" value="${userInfoDto.chsName }" type="hidden"/>
<input id="AssetInventoryId" value="${assetInventoryDto.id }" type="hidden"/>
<input id="MyAssetInventoryId" value="${myAssetInventoryDto.id }" type="hidden"/>
<div style="height: 50px; width: 100%; padding-top: 0px; padding-left: 10px; text-align: left; position: fixed; top: 0px; left: 0px; background: white; z-index: 9999">
    <button id="saveBtn" class="btn btn-primary"
            style="margin-right: 5px;" onclick="saveMyAssetInventorySubmit(1);">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
    </button>
    <button id="saveandsendBtn" class="btn btn-primary"
            style="margin-right: 5px;" onclick="saveMyAssetInventorySubmit(2);">
        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存并发起审批
    </button>
    <button class="btn btn-primary"
            onclick="backToLastPage();">
        <i class="fa fa-angle-left" style="margin-right: 5px;"></i>返回
    </button>
</div>
<div align="center" style="font-weight:blod;font-size:20px;padding:5px;margin-top:50px;">我的盘点单</div>
<div id="baseInfo" style="margin-top: 45px;">
    <span class="span-title">基础信息</span>
    <div>
        <table class="table table-bordered" style="margin-bottom: 0px;">
            <tr>
                <td class="background_color" style="width: 10%;">盘点任务编号</td>
                <td class="td" style="width: 15%;">${assetInventoryDto.inventoryRunningNum }</td>
                <td class="background_color" style="width: 10%;">盘点任务名称</td>
                <td class="td" style="width: 25%;" colspan="3">${assetInventoryDto.inventoryName }</td>
                <td class="background_color" style="width: 10%;">发起日期</td>
                <td class="td" style="width: 25%;">${assetInventoryDto.lanuchDateStr }</td>
            </tr>
            <tr style="height: 80px;">
                <td class="background_color">盘点任务说明</td>
                <td colspan="7">${assetInventoryDto.reason }</td>
            </tr>
            <tr>
                <td class="background_color">盘点单编号</td>
                <td class="td">${myAssetInventoryDto.myAssetInventoryCode }</td>
                <td class="background_color">盘点资产数量</td>
                <td class="td">
                    <fmt:formatNumber type="number" value="${myAssetInventoryDto.quantity }" maxFractionDigits="0"/>
                </td>
                <td class="background_color" style="width: 10%;">资产管理员</td>
                <td class="td">${myAssetInventoryDto.managerStr }</td>
                <td class="background_color">盘点单状态</td>
                <td class="td">${myAssetInventoryDto.myinventoryStatus }</td>
            </tr>
            <tr>
                <td class="background_color">盘亏</td>
                <td class="td" id="inventoryLoss">
                    <fmt:formatNumber type="number" value="${myAssetInventoryDto.inventoryLoss }"
                                      maxFractionDigits="0"/>
                </td>
                <td class="background_color">盘盈</td>
                <td class="td" id="inventoryProfit">
                    <fmt:formatNumber type="number" value="${myAssetInventoryDto.inventoryProfit }"
                                      maxFractionDigits="0"/>
                </td>
                <td class="background_color"></td>
                <td class="td"></td>
                <td class="background_color"></td>
                <td class="td"></td>
            </tr>
        </table>
    </div>

    <div id="assetList" style="margin-top: 5px;">
        <span class="span-title">盘点结果报告</span>
        <hr class="hr-css"/>
        <div class="container-fluid">
            <div>
                <div class="gray" style="height:60px;"><i class="fa fa-flag" style="margin-right: 5px;"></i>盘点结果报告</br>
                    盘亏盘盈原因：
                </div>
                <textarea id="reason" class="textbox"
                          style="width:84%;height:60px;resize: none;">${myAssetInventoryDto.reason }</textarea>
            </div>
            <div style="margin-top: 5px;">
                <div class="gray" style="height:60px;"><i class="fa fa-flag" style="margin-right: 5px;"></i>处置意见：</div>
                <textarea id="disposalAdvice" class="textbox"
                          style="width:84%;height:60px;resize: none;">${myAssetInventoryDto.disposalAdvice }</textarea>
            </div>
        </div>
    </div>

    <div id="assetList" style="margin-top: 5px;">
        <span class="span-title">盘点清单</span>
        <hr class="hr-css"/>
        <div class="container-fluid">
            <div>
                <button id="saveBtn" class="btn btn-primary"
                        style="margin-right: 5px;"
                        onclick="exportExcel()">
                    <i class="fa fa-plus align-top bigger-125"
                       style="margin-right: 5px;"></i>导出盘点单
                </button>
                <button class="btn btn-primary" onClick="$('#importAssetDialog').dialog('center').dialog('open');"
                        style="margin-right:5px;">
                    <i class="" style="margin-right: 5px;"></i>导入盘点单
                </button>
            </div>
            <div style="margin-top: 5px;">
                <table id="assetInventoryDataGrid"></table>
            </div>
        </div>
    </div>
    <!--上传物资信息Dialog  -->
    <div id="importAssetDialog" class="easyui-dialog" title="导入文件"
         data-options="closed:true,width:400,buttons:'#importAssetDialogButtons'">
        <form id="importForm" method="POST" enctype="multipart/form-data">
            <input type="file" name="uploadFileData"/>
        </form>
    </div>
    <div id="importAssetDialogButtons">
        <a id="importAssetConfirmBtn" href="javascript:void(0);" class="easyui-linkbutton"
           onclick="importAssetXls()">确定</a>
        <a href="javascript:void(0);" class="easyui-linkbutton" onclick="$('#importAssetDialog').dialog('close')">取消</a>
    </div>
    <div id="assetInventoryTempDialog" class="easyui-dialog" title="添加资产"
         data-options="closed:true, width:680, buttons:'#assetInventoryTempDialogButtons'">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <input id="TempId" type="hidden"/>
                <input id="assetId" type="hidden"/>
                <input id="oldResultStr" type="hidden"/>
                <div class="col-lg-12 col-md-12 col-sm-12" style="height: 35px;">
                    <span class="span-red-start">*</span>盘点结果：<input id="result" name="result" class="easyui-combobox"
                                                                     style="margin-right: 5px;"
                                                                     data-options="url:'ram/enum_combo_by_inventory_result', method:'GET', editable:false"/>
                </div>
                <div class="col-lg-12 col-md-12 col-sm-12" style="height: 35px;">
                    <span class="span-red-start"></span>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：<textarea
                        id="remark" class="textbox" style="width:84%;height:60px;resize: none;"></textarea>
                </div>
            </div>
        </div>
    </div>
    <div id="assetInventoryTempDialogButtons">
        <button id="uploadSaveBtn" class="btn btn-primary"
                onclick="saveAssetInventoryTemp();" style="margin-right: 5px;">
            <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
        </button>
        <button class="btn btn-primary"
                onclick="$('#assetInventoryTempDialog').dialog('close');"
                style="margin-right: 5px;">
            <i class="fa fa-times" style="margin-right: 5px;"></i>关闭
        </button>
    </div>
</body>
</html>