<!DOCTYPE html PUBLIC 
	"-//W3C//DTD XHTML 1.1 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	
<%@taglib prefix="s" uri="/struts-tags" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>Social Network Analysis</title>
</head>
<body>

<script>
var main = window.open("graph", "_blank","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=1200,height=800,top=0,left=0");

if (main != null) {
	self.opener = self;
	window.close();
} else {
	alert('popup blocked.');
}
</script>

</body>
</html>
