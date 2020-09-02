<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>密码修改</title>
    <jsp:include page="../libInclude.jsp"></jsp:include>
    <script type="text/javascript" src="resources/js/base/baseChangePwd.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/base/baseCommon.css">
    <style type="text/css">
        .textbox {
            width: 180px;
        }

        .lab_css {
            color: red;
        }
    </style>
</head>
<body>
<input id="password" type="hidden" value="${userInfoDto.password }">
<div id="change_pwd" class="easyui-layout" fit="true">
    <div data-options="region:'north',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div class="col-lg-12 col-md-12 col-sm-12" style="height: 35px; padding-left: 5px;">
                    <button class="btn btn-primary" onClick="changePwd();" style="margin-right: 5px;">
                        <i class="fa fa-save align-top bigger-125" style="margin-right: 5px;"></i>保存
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div data-options="region:'center',border:false">
        <div class="container-fluid">
            <div class="row" style="margin-top: 5px;">
                <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="gray">旧密码:</div>
                    <input name="oldPwd" class="textbox" type="password"
                           data-options="url:'base/dept_combo_by_querys?showType=0', method:'POST'"/>
                    &nbsp;<label id="oldPwdLab" class="lab_css"></label>
                </div>
                <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="gray">新密码:</div>
                    <input name="newPwd" class="textbox" type="password"/>
                    &nbsp;<label id="newPwdLab" class="lab_css"></label>
                </div>
                <div class="col-lg-12 col-md-12 col-sm-12">
                    <div class="gray">确认密码:</div>
                    <input name="confirmPwd" class="textbox" type="password"/>
                    &nbsp;<label id="confirmPwdLab" class="lab_css"></label>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>