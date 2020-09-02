<%--
  Created by IntelliJ IDEA.
  User: yeaho
  Date: 2020/8/3
  Time: 10:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="resources/js/flowable/approveCommon.js"></script>
</head>
<input id="processInstanceId" type="hidden" value="${processInstanceId}">
<body>
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
</body>
</html>
