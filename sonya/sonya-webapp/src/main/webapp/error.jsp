<%@ page language="java" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
    <title><fmt:message key="errorPage.title"/></title>
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/theme.css'/>" />
</head>

<body id="error">
    <div id="page">
        <div id="header">
            <jsp:include page="/common/header.jsp"/>            
        </div>
        <div id="content" class="clearfix">
			<h1><fmt:message key="errorPage.heading"/></h1>
			<%@ include file="/common/messages.jsp" %>
			
			<% if (exception != null) { %>
			    <pre><% exception.printStackTrace(new java.io.PrintWriter(out)); %></pre>
			<% } else if (request.getAttribute("javax.servlet.error.exception") != null) { %>
			    <pre><% ((Exception)request.getAttribute("javax.servlet.error.exception"))
			                           .printStackTrace(new java.io.PrintWriter(out)); %></pre>
			<% } %>
        </div>
        <div id="footer">
            <jsp:include page="/common/footer.jsp"/>
        </div>
	</div>
</body>
</html>
