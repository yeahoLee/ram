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
<input id="processDefinitionKey" type="hidden">
<input id="nodeId" type="hidden" value="${approveToDoDto.nodeId}">
<input id="taskId" type="hidden" value="${taskId}"/>
<input id="processInstanceId" type="hidden" value="${processInstanceId}"/>
<div style="z-index: 999;height:50px;width:100%;padding-top:0px;padding-left:10px;text-align:left;position:fixed;top:0px;left:0px;background:white;">
    <c:if test="${approveToDoDto ne null}">
        <c:choose>
            <c:when test="${read eq true}">
                <div>
                    <button class="btn btn-success" onclick="agree();">
                        <i class="fa fa-check align-top bigger-125"></i>
                        <span class="btn-font">已阅</span>
                    </button>
                </div>
            </c:when>
            <c:when test="${draft eq true}">
                <div>
                    <button class="btn btn-success" onclick="agree();">
                        <i class="fa fa-check align-top bigger-125"></i>
                        <span class="btn-font">发送</span>
                    </button>
                </div>
            </c:when>
            <c:otherwise>
                <div>
                    <button class="btn btn-success" onclick="agree();">
                        <i class="fa fa-check align-top bigger-125"></i>
                        <span class="btn-font">同意</span>
                    </button>
                    <button class="btn btn-danger" onclick="disAgree();">
                        <i class="fa fa-remove align-top bigger-125"></i>
                        <span class="btn-font">不同意</span>
                    </button>
                    <button class="btn btn-primary" onclick="location='todo_work'">
                        <i class="fa fa-mail-reply align-top bigger-125"></i>
                        <span class="btn-font">返回</span>
                    </button>
                </div>
            </c:otherwise>
        </c:choose>
    </c:if>

    <c:if test="${approveFinishedDto ne null}">
        <div>
            <c:if test="${revoke eq true}">
                <button class="btn btn-info" onclick="revork();">
                    <i class="fa fa-times align-top bigger-125"></i>
                    <span class="btn-font">撤回</span>
                </button>
            </c:if>
            <button class="btn btn-primary" onclick="location='finished_work'">
                <i class="fa fa-mail-reply align-top bigger-125"></i>
                <span class="btn-font">返回</span>
            </button>
        </div>
    </c:if>
</div>

<%--审批基本信息--%>
<div style="${(approveToDoDto ne null || approveFinishedDto ne null)? 'margin-top: 60px;':''}">
    <span class="span-title">审批基本信息</span>
    <hr class="hr-css"/>
    <div class="col-lg-12 col-md-12 col-sm-12" style="padding-right: 15px; padding-left: 0px;">
        <c:if test="${approveToDoDto ne null}">
            <table class="table table-bordered">
                <tr>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>
                        流程单号
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveToDoDto.approveNumber}</td>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>
                        流程标题
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveToDoDto.formName}</td>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>
                        流程发起人
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveToDoDto.processInstanceStarter}</td>
                </tr>
                <tr>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>
                        流程发起时间
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveToDoDto.processInstanceStartTimeStr}</td>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>
                        上一环节发送人
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveToDoDto.previousNodeSendUserName}</td>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>
                        上一环节发送时间
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveToDoDto.startTimeStr}</td>
                </tr>
                <tr>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>当前环节
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveToDoDto.taskName}</td>
                </tr>
            </table>
        </c:if>

        <c:if test="${approveFinishedDto ne null}">
            <table class="table table-bordered">
                <tr>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>
                        流程单号
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveFinishedDto.approveNumber}</td>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>
                        流程标题
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveFinishedDto.formName}</td>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>
                        流程发起人
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveFinishedDto.starter}</td>
                </tr>
                <tr>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>
                        流程发起时间
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveFinishedDto.processInstanceStartTimeStr}</td>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>当前环节
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveFinishedDto.runningProcesses}</td>
                    <td class="background_color col-lg-2 col-md-2 col-sm-2">
                        <i class="fa fa-list-ol"></i>当前处理人
                    </td>
                    <td class="col-lg-2 col-md-2 col-sm-2">${approveFinishedDto.runningProcessUsers}</td>
                </tr>
            </table>
        </c:if>
    </div>
</div>
</body>
</html>
