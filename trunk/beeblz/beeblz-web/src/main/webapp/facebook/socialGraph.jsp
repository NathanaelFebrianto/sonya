<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Beeblz - Social Graph for Facebook</title>
</head>
<body>

<%
	String accessToken = request.getParameter("fb_access_token");

%>
  
 <!-- Applet Area -->
<object
    classid = "clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
    codebase = "http://java.sun.com/update/1.6.0/jinstall-6u11-windows-i586.cab#Version=6,0,0,3"
    WIDTH = 1000 HEIGHT = 680 >
    <PARAM NAME = CODE VALUE = com.beeblz.view.GraphApplet >
    <param name = CODEBASE value = "/" >
    <param name = ARCHIVE value = 
	   		"./signedjar/beeblz-facebook.jar, ./signedjar/derby.jar, 
	   		./signedjar/jung-algorithms.jar, ./signedjar/jung-api.jar, 
	   		./signedjar/jung-graph-impl.jar, ./signedjar/jung-io.jar,	
	   		./signedjar/log4j.jar,  ./signedjar/restfb.jar,
	   		./signedjar/substance.jar, ./signedjar/trident.jar,
	   		./signedjar/collections-generic.jar" >
	</param>
    <param name = "type" value = "application/x-java-applet;version=1.6">
    <param name = "scriptable" value = "false">
    
    <param name = "access_token" value = <%=accessToken%>>

    <comment>
	<embed
            type = "application/x-java-applet;version=1.6" \
            CODE = com.beeblz.view.GraphApplet \
			CODEBASE = "/" \
			ARCHIVE = 
	   		"./signedjar/beeblz-facebook.jar, ./signedjar/derby.jar, 
	   		./signedjar/jung-algorithms.jar, ./signedjar/jung-api.jar, 
	   		./signedjar/jung-graph-impl.jar, ./signedjar/jung-io.jar,	
	   		./signedjar/log4j.jar,  ./signedjar/restfb.jar,
	   		./signedjar/substance.jar, ./signedjar/trident.jar,
	   		./signedjar/collections-generic.jar" \
            WIDTH = 1000 \
            HEIGHT = 680
	    scriptable = false
	    pluginspage = "http://java.sun.com/products/plugin/index.html#download"
	    access_token = <%=accessToken%> >
		<noembed>
            alt="Your browser understands the &lt;APPLET&gt; tag but isn't running the applet, for some reason."
	Your browser is completely ignoring the &lt;APPLET&gt; tag!
		</noembed>
	</embed>
    </comment>
</object>
<!--"End of Applet Area"-->
  
</body>
</html>