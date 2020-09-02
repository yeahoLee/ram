<%--
  Created by IntelliJ IDEA.
  User: yeaho
  Date: 2020/7/20
  Time: 9:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<!-- div2 -->
<div style="margin-top: 15px;">
    <span class="span-title">流程审批记录</span>
    <hr class="hr-css"/>
    <div class="col-lg-12 col-md-12 col-sm-12" style="padding-right: 15px; padding-left: 0px;">
        <table class="table table-bordered">
            <tr>
                <td>
                    <table id="historyDatagrid" style="height: 415px;"></table>
                </td>
            </tr>
        </table>
    </div>
</div>
<!-- div3 -->
<div style="margin-top: 15px;">
    <span class="span-title">审批意见</span>
    <hr class="hr-css"/>
    <div class="col-lg-12 col-md-12 col-sm-12" style="padding-right: 15px; padding-left: 0px;">
        <table class="table table-bordered">
            <tr>
                <td class="table-title">
                    <i class="fa fa-comment"></i>复审意见
                </td>
                <td>
                    <textarea id="suggestContent" class="textbox" style="height:110px; width:100%; resize: none;" wrap="hard"></textarea>
                </td>
            </tr>
        </table>
    </div>
</div>

<%--确认审核Dialog--%>
<div id="confirmDialog" class="easyui-dialog"
     data-options="title:'确认审核',buttons:'#confirmDialogBtns',modal:true,closed:true"
     style="width:600px;height:400px">
    <div id="confirm" class="easyui-layout" data-options="fit:true">
        <div data-options="region:'west', split:true, title:'选择下一步环节', collapsible:false" style="width: 280px;">
            <div class="easyui-layout" data-options="fit:true">
                <div data-options="region:'north', split:true, minHeight:180">
                    <ul id="selectNextNode" class="list-group">
                    </ul>
                </div>
                <div data-options="region:'center', title:'已选择的步骤和处理人'">
                    <ul id="selectNodeAndUser" class="list-group">
                    </ul>
                </div>
            </div>
        </div>
        <div data-options="region:'center', split:true, title:'选择下一个人员', fit:true">
            <ul id="selectUser" class="list-group">
            </ul>
        </div>
    </div>
</div>
<div id="confirmDialogBtns">
    <button class="btn btn-primary" onclick="confirmDialogBtnsConfirm();">
        <i class="fa fa-check align-top bigger-125"></i>
        <span class="btn-font">确定</span>
    </button>
    <button class="btn btn-primary" onclick="$('#confirmDialog').dialog('close')">
        <i class="fa fa-remove align-top bigger-125"></i>
        <span class="btn-font">取消</span>
    </button>
</div>
<%--/确认审核Dialog--%>
</body>
</html>
