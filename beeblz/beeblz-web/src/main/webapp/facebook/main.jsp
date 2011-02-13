<html>
<head>
<title>Beeblz - Social Graph for Facebook</title>
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="/css/main.css" />
<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" />
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
</head>

<body>

<script>
	$(document).ready(function() {
		$("#dialog").dialog();
		$("#menu").buttonset();
		$("#tabs").tabs();
	});
</script>

<div id=dialog title="Notice">
	Beeblz - Social Graph for Facebook(version 0.0.1- just developing now...)
	<br>
	This application is only run by the Chrome, Safari, Firefox browser. In
	the future we will provide IE.
</div>

<div id="menu">
	<input type="radio" id="clustering" name="radio" /><label for="clustering">Clustering</label> 
	<input type="radio" id="ranking" name="radio" checked="checked" /><label for="ranking">Ranking</label>
	<input type="radio" id="sharing" name="radio" /><label for="sharing">Sharing</label>
</div>

<div id="tabs">
<ul>
	<li><a href="#fragment-1"><span>Social Graph</span></a></li>
	<li><a href="#fragment-2"><span>Closeness Graph</span></a></li>
	<li><a href="#fragment-3"><span>Friends</span></a></li>
</ul>

	<div id="fragment-1">
		<iframe id="socialGraph" name="socialGraph"	src="socialGraph.jsp" 
		 height="620" width="100%" frameborder="0"
		 marginwidth="0" marginheight="0" scrolling="no"> 
		</iframe>
	</div>
	
	<div id="fragment-2">
		We will provide the closeness graph tab soon.
	</div>
	
	<div id="fragment-3">
		We will provide the friends tab soon.
	</div>
</div>

</body>
</html>
