<html>
<head>
<title>Force-Directed Layout</title>
<script type="text/javascript" src="../js/protovis-3.2/protovis-r3.2.js"></script>
<script type="text/javascript" src="../js/miserables.js"></script>
<style type="text/css">
body {
	margin: 0;
}
</style>
</head>
<body>
<script type="text/javascript+protovis">

var w = document.body.clientWidth,
    h = document.body.clientHeight,
    colors = pv.Colors.category19(); 

var vis = new pv.Panel()
    .width(w)
    .height(h)
    .fillStyle("white")
    .event("mousedown", pv.Behavior.pan())
    .event("mousewheel", pv.Behavior.zoom());

var force = vis.add(pv.Layout.Force)
    .nodes(miserables.nodes)
    .links(miserables.links)
    .springLength(50) 
    .chargeConstant(-1750) 
    .bound(true);


force.link.add(pv.Line)

force.node.add(pv.Dot)
    .size(500)
    .fillStyle(function(d) d.fix ? "brown" : colors(d.group))
    .strokeStyle(function() this.fillStyle().darker())
    .lineWidth(1)
    .title(function(d) d.nodeName)
    .event("mousedown", pv.Behavior.drag())
    .event("drag", force)
  	.add(pv.Image)
    	.left(function(d) d.x - (30/2))
    	.top(function(d) d.y - (30/2))
    	.url(function(d) "http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs224.ash2/48976_1825235343_2331_q.jpg")
    	.imageWidth(30)
    	.imageHeight(30)
    	.width(30)
    	.height(30)
		.fillStyle(null)
    	.strokeStyle(null);

vis.render();

</script>
</body>
</html>