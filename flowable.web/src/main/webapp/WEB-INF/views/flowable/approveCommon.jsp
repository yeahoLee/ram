<%--
  Created by IntelliJ IDEA.
  User: yeaho
  Date: 2020/7/10
  Time: 9:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
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
