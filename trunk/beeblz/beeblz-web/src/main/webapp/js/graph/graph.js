/**
 * Copyright Beeblz.com.
 * graph.js
 *
 */

/**
 * Graph. 
 */
var Graph = function() {
	this.vertices = [];
	this.edges = [];
};

Graph.prototype = {
	
	/*
	 * Gets the index of vertex. 
	 * 
	 * @param id the vertex id
	 * @return index the vertex index
	 */
	getVertexIndex : function(id) {
		var numVertices = this.vertices.length;
		// alert("id == " + id + ", numVertices == " + numVertices);
		if (numVertices > 0) {
			for ( var i = 0; i < numVertices; i++) {
				var vertex = this.vertices[i];
				// alert("vertex.id == " + vertex.id + ", id == " + id);
				if (vertex.id == id) {
					// alert("vertex.id == " + vertex.id + ", vertex.index == "
					// + vertex.index);
					return vertex.index;
					break;
				}
			}
		}
		return null;
	},
		
	/*
	 * Adds a vertex.
	 * 
	 * @param id the id @param name the name @param picture the picture image
	 * url @param group the group number
	 */
	addVertex : function(id, name, picture, group) {
		var index = 0;
		if (this.vertices.length > 0) {
			index = this.vertices.length;
		}
		//alert("index == " + index);
		this.vertices.push({"index" : index, "id" : id, "nodeName" : name, "picture" : picture, "group" : group});
	},

	/*
	 * Adds a edge. 
	 * 
	 * @param source the index of source vertex
	 * @param source the index of target vertex
	 * @param value the weight
	 */
	addEdge : function(source, target, weight) {
		var sourceIndex = this.getVertexIndex(source);
		var targetIndex = this.getVertexIndex(target);
		//alert("sourceIndex == " + sourceIndex + ", targetIndex == " + targetIndex);
		this.edges.push({"source" : sourceIndex, "target" : targetIndex, "value" : weight});
	}
};

/**
 * Renderer base class.
 */
Graph.Renderer = {};

/**
 * 
 * Renderer implementation using Protovis.
 * 
 * @param graph	the Graph object
 * @param width the width of panel
 * @param height the height of panel
 * @param colors the pv.Color of nodes
 */
Graph.Renderer.Protovis = function(graph, width, height, colors) {
	var vis = new pv.Panel()
	    .width(width)
	    .height(height)
	    .fillStyle("white")
	    .event("mousedown", pv.Behavior.pan())
	    .event("mousewheel", pv.Behavior.zoom());

	var force = vis.add(pv.Layout.Force)
	    .nodes(graph.vertices)
	    .links(graph.edges)
	    .springLength(40) 
	    //.springConstant(0.09) 
	    .chargeConstant(-100)
	    .iterations(1000)
	    .bound(true);

	/*
	var collisionConstraint = pv.Constraint.collision(function(d) d.r + 3.3),    
		positionConstraint = pv.Constraint.position(function(d) d.p );
	
	var sim = pv.simulation(nodes)    
		.constraint(collisionConstraint)    
		.constraint(positionConstraint)    
		.force(pv.Force.drag());
	*/

	force.link.add(pv.Line)
	    .lineWidth(0.5);

	force.node.add(pv.Dot)
	    .size(500)
	    .fillStyle(function(d) { return d.fix ? "brown" : colors(d.group); })
	    .strokeStyle(function() { return this.fillStyle().darker(); })
	    .lineWidth(1)
	    .title(function(d) { return d.nodeName; })
	    .event("mousedown", pv.Behavior.drag())
	    .event("drag", force)
	    .add(pv.Label)
	    	.text(function(d) { return d.nodeName; })
	    	.font("11px tahoma")
			.textAlign("right")
	  	.add(pv.Image)
	    	.left(function(d) { return d.x - (30/2); })
	    	.top(function(d) { return d.y - (30/2); })
	    	.url(function(d) { return d.picture; })
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
