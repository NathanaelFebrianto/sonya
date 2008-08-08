<%@ include file="/common/taglibs.jsp"%>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices" %>

<%
String locale = "en";
if (request.getLocale() != null) {
	locale = request.getLocale().getLanguage();
}

if (request.getSession(false) != null) {
    session.invalidate();
}

Cookie terminate = new Cookie(TokenBasedRememberMeServices.ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE_KEY, null);
String contextPath = request.getContextPath();
terminate.setPath(contextPath != null && contextPath.length() > 0 ? contextPath : "/");
terminate.setMaxAge(0);
response.addCookie(terminate);

System.out.println("current locale == " + locale);

response.sendRedirect("/?locale=" + locale);
%>

<!--  
<c:redirect url="/index.jsp"/>
-->
