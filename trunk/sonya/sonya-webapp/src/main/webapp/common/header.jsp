<%@ include file="/common/taglibs.jsp"%>

<div class="top">
<c:if test="${pageContext.request.locale.language != 'en'}">
	<span class="header"><a href="<c:url value='/?locale=en'/>">English</a></span>
</c:if>
<c:if test="${pageContext.request.locale.language != 'ko'}">
	<span class="header"><a href="<c:url value='/?locale=ko'/>">Korean</a></span>
</c:if> <span class="header">|</span> <span class="header"><a href="<c:url value='/login.jsp'/>">
	<fmt:message key="signin" /></a></span>
</div>

<div class="branding">
<a href="<fmt:message key="company.url"/>">
<img src="<c:url value="/images/logo.png"/>" alt="<fmt:message key="webapp.name"/>" /> </a></div>

<div class="center">
<h1><span class="header"><fmt:message key="webapp.name" /></span></h1>
</div>


