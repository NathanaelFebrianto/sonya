<%@ include file="/common/taglibs.jsp"%>

<div id="branding">
	<h1><fmt:message key="webapp.name"/></h1></a>
</div>

<div id="toplink">
	<c:if test="${pageContext.request.locale.language != 'en'}">
		<a href="<c:url value='/?locale=en'/>">English</a>	
	</c:if>
	<c:if test="${pageContext.request.locale.language != 'ko'}">
		<a href="<c:url value='/?locale=ko'/>">Korean</a>	
	</c:if>
	| <a href=#><fmt:message key="login"/></a>
</div>

<%-- Put constants into request scope --%>
<appfuse:constants scope="request"/>