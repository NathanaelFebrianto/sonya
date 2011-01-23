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
	.springLength(100);

force.link.add(pv.Line);


force.node.add(pv.Dot)
    .size(function(d) (d.linkDegree + 4) * Math.pow(this.scale, -1.5))
    .fillStyle(function(d) d.fix ? "brown" : colors(d.group))
    .strokeStyle(function() this.fillStyle().darker())
    .lineWidth(1)
    .title(function(d) d.nodeName)
    .event("mousedown", pv.Behavior.drag())
    .event("drag", force);
/*
	.add(pv.Image)
	 .imageWidth(50)
     .imageHeight(50)
	 .url("http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs645.snc3/27435_1480697938_9293_q.jpg")
*/

vis.render();

</script>
</body>
</html>