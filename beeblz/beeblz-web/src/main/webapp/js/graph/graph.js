/*
 * Copyright Beeblz.com.
 *
 */

/*
 * Graph 
 */
var Graph = function() {
	this.vertices = [];
	this.edges = [];
};

Graph.prototype = {
	/*
	 * add a node 
	 */
	addVertex : function(index, id, name, picture, group) {
		this.vertices.push({"index" : index, "id" : id, "nodeName" : name, "picture" : picture, "group" : group});
	},

	/*
	 * add a edge 
	 */
	addEdge : function(source, target, weight) {
		this.edges.push({"source" : source, "target" : target, "value" : weight});
	}

};

/*
 * Renderer base class
 */
Graph.Renderer = {};

/*
 * Renderer implementation using Protovis
 */
Graph.Renderer.Protovis = function(graph, width, height) {
	this.graph = graph;
	var width = width;
	var height = height;
	var colors = pv.Colors.category19();

	var vis = new pv.Panel()
	    .width(width)
	    .height(height)
	    .fillStyle("white")
	    .event("mousedown", pv.Behavior.pan())
	    .event("mousewheel", pv.Behavior.zoom());

	var force = vis.add(pv.Layout.Force)
	    .nodes(graph.vertices)
	    .links(graph.edges)
	    .springLength(50) 
	    .chargeConstant(-1750)
	    .bound(true);

	force.link.add(pv.Line)
	    .lineWidth(0.5);

	force.node.add(pv.Dot)
	    .size(500)
	    .fillStyle(function(d) d.fix ? "brown" : colors(d.group))
	    .strokeStyle(function() this.fillStyle().darker())
	    .lineWidth(1)
	    .title(function(d) d.nodeName)
	    .event("mousedown", pv.Behavior.drag())
	    .event("drag", force)
	    .add(pv.Label)
	    	.text(function(d) d.nodeName)
	    	.font("11px tahoma")
			.textAlign("right")
	  	.add(pv.Image)
	    	.left(function(d) d.x - (30/2))
	    	.top(function(d) d.y - (30/2))
	    	.url(function(d) d.picture)
	    	.imageWidth(30)
	    	.imageHeight(30)
	    	.width(30)
	    	.height(30)
			.fillStyle(null)
	    	.strokeStyle(null);

	vis.render();
};

Graph.Renderer.Protovis.prototype = {
};

