<%--
  Created by IntelliJ IDEA.
  User: yeaho
  Date: 2020/7/20
  Time: 15:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
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
</body>
</html>
