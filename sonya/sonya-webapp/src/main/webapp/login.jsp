<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="user.login.title"/></title>
</head>
<body id="login" />

<form method="post" id="loginForm" action="<c:url value='/j_security_check'/>"
    onsubmit="saveUsername(this);return validateForm(this)">
<fieldset style="padding-bottom: 0">
<legend><fmt:message key="signin"/></legend>
<ul>
<c:if test="${param.error != null}">
    <div class="error">
        <img src="${ctx}/images/iconWarning.gif" alt="<fmt:message key='icon.warning'/>" class="icon"/>
        <fmt:message key="user.errors.password.mismatch"/>
        <%--${sessionScope.ACEGI_SECURITY_LAST_EXCEPTION.message}--%>
        <%@include file="/common/messages.jsp" %>
    </div>
</c:if>

<div>
	<label for="j_username" class="desc">
          <fmt:message key="user.username"/>: <br />
    </label>
    <input type="text" class="text medium" name="j_username" id="j_username" tabindex="1" /></p>
</div>

<div>
    <label for="j_password" class="desc">
        <fmt:message key="user.password"/>: <br />
    </label>
    <input type="password" class="text medium" name="j_password" id="j_password" tabindex="2" /></p>
</div>

<c:if test="${appConfig['rememberMeEnabled']}">
    <div>
        <input type="checkbox" class="checkbox" name="rememberMe" id="rememberMe" tabindex="3"/>
        <label for="rememberMe" class="choice"><fmt:message key="user.login.remember.me"/></label>
    </div>
</c:if>
    <div>
        <input type="submit" class="button" name="login" value="<fmt:message key='button.login'/>" tabindex="4" onclick="signup();" />
        <p>
            <fmt:message key="user.login.signup">
                <fmt:param><c:url value="/signup.html"/></fmt:param>
            </fmt:message>
        </p>
    </div>
</ul>
</fieldset>
</form>

<%@ include file="/scripts/login.js"%>
<script>
	function signup() {
		var d = $('session');
		d.innerHTML = '<a href="/logout.jsp">Sign out</a>';
	}
</script>
