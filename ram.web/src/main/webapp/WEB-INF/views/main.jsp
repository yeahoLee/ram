<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="cn">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=8,chrome=1"/>
    <meta charset="utf-8"/>
    <title>实物资产管理系统</title>

    <meta name="description" content="overview &amp; stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="resources/ace/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="resources/fontawesome/css/font-awesome.min.css"/>

    <!-- text fonts -->
    <link rel="stylesheet" href="resources/ace/css/fonts.googleapis.com.css"/>

    <!-- ace styles -->
    <link rel="stylesheet" href="resources/ace/css/ace.min.css" class="ace-main-stylesheet" id="main-ace-style"/>

    <!--[if lte IE 9]>
        <link rel="stylesheet" href="resources/ace/css/ace-part2.min.css" class="ace-main-stylesheet" />
    <![endif]-->
    <link rel="stylesheet" href="resources/ace/css/ace-skins.min.css"/>
    <link rel="stylesheet" href="resources/ace/css/ace-rtl.min.css"/>

    <!--[if lte IE 9]>
      <link rel="stylesheet" href="resources/ace/css/ace-ie.min.css" />
    <![endif]-->

    <!-- ace settings handler -->
    <script src="resources/ace/js/ace-extra.min.js"></script>

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="resources/ace/js/html5shiv.min.js"></script>
    <script src="resources/ace/js/respond.min.js"></script>
    <![endif]-->
</head>
<body class="no-skin">
<div id="navbar" class="navbar navbar-default ace-save-state navbar-fixed-top">
    <div class="navbar-container ace-save-state" id="navbar-container">
        <!-- logo -->
        <div class="navbar-header pull-left">
            <img src="resources/ace/images/header_h1.png" style="padding-top:5px; height: 55px;"></img>
        </div>
        <!-- 快捷菜单 -->
        <div class="navbar-buttons navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav">
                <%-- <c:if test="${not empty loginDeptDto}">
                    <li class="light-blue dropdown-modal">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0);">
                            <span>${loginDeptDto.deptName } 切换</span>
                            <span class="ace-icon fa fa-chevron-down"></span>
                        </a>

                        <ul class="dropdown-menu-right dropdown-navbar navbar-blue dropdown-menu dropdown-caret dropdown-close">
                            <c:forEach items="${deptInfoList}" var="deptInfo">
                                <li class="dropdown-content">
                                    <ul class="dropdown-menu dropdown-navbar navbar-blue">
                                        <li>
                                            <a href="javascript:void(0);" onclick="changeLoginDept('${deptInfo.id }');">
                                                ${deptInfo.deptName }
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                            </c:forEach>
                        </ul>
                    </li>
                </c:if> --%>
                <li class="light-blue dropdown-modal">
                    <a data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0);" onclick="logout();">
                        <span class="ace-icon fa fa-power-off"></span>
                        <span>登出</span>
                    </a>
                </li>
            </ul>
        </div>
    </div><!-- /.navbar-container -->
</div>
<div class="main-container ace-save-state" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.loadState('main-container')
        } catch (e) {
        }
    </script>

    <div id="sidebar" class="sidebar responsive ace-save-state sidebar-fixed">
        <script type="text/javascript">
            try {
                ace.settings.loadState('sidebar')
            } catch (e) {
            }
        </script>

        <ul class="nav nav-list">
            <li class="active">
                <a href="index.html">
                    <i class="fa fa-user-circle-o bigger-125" aria-hidden="true"></i>
                    <span class="menu-text">&nbsp;&nbsp;欢迎您， ${userInfoDto.chsName }</span>
                </a>
            </li>
            <!-- 动态菜单 -->
            <c:forEach items="${menuDtoList}" var="superClassMenuDto">
                <c:if test="${not empty superClassMenuDto.address}">
                    <li class="">
                        <a onclick="openIframeUrl('${superClassMenuDto.address}', '${superClassMenuDto.title}');"
                           style="cursor: pointer;>
	                        <span class=" menu-text"><i class="${superClassMenuDto.icon}"
                                                        style="margin-right: 5px;"></i>${superClassMenuDto.title}</span>
                        </a>
                    </li>
                </c:if>
                <c:if test="${empty superClassMenuDto.address}">
                    <li class="">
                        <a href="javascript:void(0);" class="dropdown-toggle">
                            <span class="menu-text"><i class="${superClassMenuDto.icon}"
                                                       style="margin-right: 5px;"></i>${superClassMenuDto.title}</span>
                            <b class="arrow fa fa-angle-down"></b>
                        </a>
                        <ul class="submenu nav-hide" style="display: none;">
                            <c:forEach items="${superClassMenuDto.subItem}" var="subClassMenuDto">
                                <tr>
                                    <li class="" style="background-color: #2B3D53">
                                        <a onclick="openIframeUrl('${subClassMenuDto.address}', '${subClassMenuDto.title}');"
                                           style="cursor: pointer;">
                                            <span class="menu-text"><i class="${subClassMenuDto.icon}"
                                                                       style="margin-right: 5px;"></i>${subClassMenuDto.title}</span>
                                        </a>
                                    </li>
                                </tr>
                            </c:forEach>
                        </ul>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
        <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
            <i id="sidebar-toggle-icon" class="ace-icon fa fa-angle-double-left ace-save-state"
               data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
        </div>
    </div>

    <div class="main-content">
        <div class="main-content-inner">
            <div class="page-content">
                <div class="page-header" style="position: absolute;left: 20px;right: 20px;">
                    <div>
						<span style="font-size: 18px; color: #C70039;">
							<i class="fa fa-home bigger-125"></i>
						</span>
                        <span>主页</span>
                        <!-- <i class="ace-icon fa fa-angle-double-right"></i> -->
                        <span id="content_title"></span>
                    </div>
                </div><!-- /.page-header -->

                <div style="height: 100%;padding-bottom: 20px;padding-top: 50px;">
                    <iframe id="content_frame" class="embed-responsive-item" src="" style="width: 100%;height: 100%;"
                            frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="yes"
                            allowtransparency="yes"></iframe>
                </div><!-- /.row -->
            </div><!-- /.page-content -->
        </div>
    </div><!-- /.main-content -->

    <div class="footer">
        <div class="footer-inner">
            <div class="footer-content">
				<span class="bigger-80">
					Copyright 2019 常州市轨道交通发展有限公司 技术支持：江苏迈恩信息科技有限公司
				</span>
            </div>
        </div>
    </div>
</div><!-- /.main-container -->

<!-- basic scripts -->
<!--[if !IE]> -->
<script src="resources/ace/js/jquery-2.1.4.min.js"></script>
<!-- <![endif]-->

<!--[if IE]>
<script src="resources/ace/js/jquery-1.11.3.min.js"></script>
<![endif]-->
<script type="text/javascript">
    if ('ontouchstart' in document.documentElement) document.write("<script src='assets/js/jquery.mobile.custom.min.js'>" + "<" + "/script>");
</script>
<script src="resources/ace/js/bootstrap.min.js"></script>

<!-- ace scripts -->
<script src="resources/ace/js/ace-elements.min.js"></script>
<script src="resources/ace/js/ace.min.js"></script>

<script src="resources/js/main.js"></script>
</body>
</html>
