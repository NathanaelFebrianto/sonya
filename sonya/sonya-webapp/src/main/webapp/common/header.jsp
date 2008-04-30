<%@ include file="/common/taglibs.jsp"%>

<div class="top">
	<c:if test="${pageContext.request.locale.language != 'en'}">
		<a href="<c:url value='/?locale=en'/>">English</a>	
	</c:if>
	<c:if test="${pageContext.request.locale.language != 'ko'}">
		<a href="<c:url value='/?locale=ko'/>">Korean</a>	
	</c:if>
	| <a href=#><fmt:message key="signin"/></a>
</div>

<div class="left">
	<a href="<fmt:message key="company.url"/>">
		<img src="<c:url value="/images/logo.gif"/>" alt="<fmt:message key="webapp.name"/>"/>
	</a>
</div>

<div class="center">	
	<h1><fmt:message key="webapp.name"/></h1>	
</div>


