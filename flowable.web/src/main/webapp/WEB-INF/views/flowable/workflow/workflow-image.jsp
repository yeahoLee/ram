<%--
  Created by IntelliJ IDEA.
  User: shiyz000
  Date: 2020/6/23
  Time: 17:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>流程图</title>
    <jsp:include page="../../libInclude.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="resources/css/base/baseCommon.css">
</head>
<body>
    <div style="text-align:center;margin:0 auto;">
        <h2>${workFlowImageName}</h2>
        <img src="${workFlowImageUrl}">
    </div>

</body>
</html>
