<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Social Graph</title>
</head>
<body>
  <p>
  Welcome to Beeblz - Social Graph!
  </p>
  <p>
  <%= request.getParameter("fb_access_token") %>
  </p>
</body>
</html>