<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Firebird</title>
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
