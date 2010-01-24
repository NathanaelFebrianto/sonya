<!DOCTYPE html PUBLIC 
	"-//W3C//DTD XHTML 1.1 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	
<%@taglib prefix="s" uri="/struts-tags" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>Graph</title>
	<s:head />
</head>
<body>

<!-- Applet Area -->
<object
    classid = "clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
    codebase = "http://java.sun.com/update/1.6.0/jinstall-6u11-windows-i586.cab#Version=6,0,0,3"
    WIDTH = 1000 HEIGHT = 550 >
    <PARAM NAME = CODE VALUE = org.firebird.graph.view.GraphApplet >
    <param name = CODEBASE value = "/firebird-web" >
    <param name = ARCHIVE value = 
	   		"./client-lib/firebird-common.jar, ./client-lib/firebird-collector.jar, 
	   		./client-lib/firebird-graph.jar, ./client-lib/firebird-io.jar, 
	   		./client-lib/forms-1.0.5.jar, ./client-lib/jung-3d-2.0.jar, 
	   		./client-lib/jung-3d-demos-2.0.jar, ./client-lib/jung-algorithms-2.0.jar, 
	   		./client-lib/jung-api-2.0.jar, ./client-lib/jung-graph-impl-2.0.jar, 
	   		./client-lib/jung-io-2.0.jar, ./client-lib/jung-jai-2.0.jar, 
	   		./client-lib/jung-jai-samples-2.0.jar, ./client-lib/jung-samples-2.0.jar, 
	   		./client-lib/jung-visualization-2.0.jar, ./client-lib/looks-1.2.2.jar, 
	   		./client-lib/looks-win-1.2.0.jar, ./client-lib/plastic-1.2.0.jar, 
	   		./client-lib/collections-generic-4.01.jar, ./client-lib/colt-1.2.0.jar, 
	   		./client-lib/concurrent-1.3.4.jar, ./client-lib/stax-api-1.0.1.jar,
	   		./client-lib/wstx-asl-3.2.6.jar, ./client-lib/twitter4j-2.0.10.jar" >
    <param name = "type" value = "application/x-java-applet;version=1.6">
    <param name = "scriptable" value = "false">

    <comment>
	<embed
            type = "application/x-java-applet;version=1.6" \
            CODE = org.firebird.graph.view.GraphApplet \
			CODEBASE = "/firebird-web" \
			ARCHIVE = 
			"./client-lib/firebird-common.jar, ./client-lib/firebird-collector.jar, 
	   		./client-lib/firebird-graph.jar, ./client-lib/firebird-io.jar, 
	   		./client-lib/forms-1.0.5.jar, ./client-lib/jung-3d-2.0.jar, 
	   		./client-lib/jung-3d-demos-2.0.jar, ./client-lib/jung-algorithms-2.0.jar, 
	   		./client-lib/jung-api-2.0.jar, ./client-lib/jung-graph-impl-2.0.jar, 
	   		./client-lib/jung-io-2.0.jar, ./client-lib/jung-jai-2.0.jar, 
	   		./client-lib/jung-jai-samples-2.0.jar, ./client-lib/jung-samples-2.0.jar, 
	   		./client-lib/jung-visualization-2.0.jar, ./client-lib/looks-1.2.2.jar, 
	   		./client-lib/looks-win-1.2.0.jar, ./client-lib/plastic-1.2.0.jar, 
	   		./client-lib/collections-generic-4.01.jar, ./client-lib/colt-1.2.0.jar, 
	   		./client-lib/concurrent-1.3.4.jar, ./client-lib/stax-api-1.0.1.jar,
	   		./client-lib/wstx-asl-3.2.6.jar, ./client-lib/twitter4j-2.0.10.jar" \
            WIDTH = 1000 \
            HEIGHT = 550
	    scriptable = false
	    pluginspage = "http://java.sun.com/products/plugin/index.html#download">
	    <noembed>
            alt="Your browser understands the &lt;APPLET&gt; tag but isn't running the applet, for some reason."
	Your browser is completely ignoring the &lt;APPLET&gt; tag!
            </noembed>
	</embed>
    </comment>
</object>

<!--
<APPLET CODE = CardTest.class WIDTH = 400 HEIGHT = 300>
alt="Your browser understands the &lt;APPLET&gt; tag but isn't running the applet, for some reason."
	Your browser is completely ignoring the &lt;APPLET&gt; tag!

</APPLET>
-->
<!--"End of Applet Area"-->

</body>
</html>
	