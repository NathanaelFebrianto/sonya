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
		if (numVertices > 0) {
			for (var i = 0; i < numVertices; i++) {
				var vertex = this.vertices[i];
				if (vertex.id == id) {
					//console.log("vertex.id == " + vertex.id + ", vertex.index == " + vertex.index);
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
		//console.log("sourceIndex == " + sourceIndex + ", targetIndex == " + targetIndex);
		this.edges.push({"source" : sourceIndex, "target" : targetIndex, "value" : weight});
	},
	
	/*
	 * Loads each node as the first value in a 3d array. 
	 * Any associated nodes are then added into the second array. 
	 * 
	 */
	loadNeighborNodes : function() {
		var nodeHash = new Array();
		var firstIndex = 0;
		for(firstIndex; firstIndex < this.vertices.length; firstIndex++) {					
			var secondIndex = 0;	
			nodeHash[firstIndex] = new Array();
					
			for(var x = 0; x < this.edges.length; x++) {
				if (this.edges[x].source == firstIndex) {	
					nodeHash[firstIndex][secondIndex] = this.edges[x].target;
					secondIndex++;						
				}
				if (this.edges[x].target == firstIndex) {
					nodeHash[firstIndex][secondIndex] = this.edges[x].source;
					secondIndex++;
				}
			}
		}
		return nodeHash;
	},
	
	/*
	 * Loads each edge as the first value in a 3d array. 
	 * Any associated edges are then added into the second array. 
	 * 
	 */
	loadNeighborEdges : function() {
		var edgeHash = new Array();
		var firstIndex = 0;
		for(firstIndex; firstIndex < this.vertices.length; firstIndex++) {					
			var secondIndex = 0;	
			edgeHash[firstIndex] = new Array();
					
			for(var x = 0; x < this.edges.length; x++) {
				if (this.edges[x].source == firstIndex) {	
					edgeHash[firstIndex][secondIndex] = x;
					secondIndex++;						
				}
				if (this.edges[x].target == firstIndex) {
					edgeHash[firstIndex][secondIndex] = x;
					secondIndex++;
				}
			}
		}
		return edgeHash;
	},
	
    /*
	 * Checks if an array contains a property.
	 * 
	 */	
	contains : function(a, obj) {
		if (a != null) {
			for (var i = 0; i < a.length; i++) {
				if (a[i] == obj) {
	    			return true;
				}
			}			
		}
		return false;
	}		

};

/**
 * Visualization base class.
 */
Graph.Visualization = {};

/**
 * 
 * Graph visualization using Protovis.
 * 
 * @param graph	the Graph object
 * @param width the width of panel
 * @param height the height of panel
 * @param colors the pv.Color of nodes
 */
Graph.Visualization.Protovis = function(graph, width, height, colors) {
	
	//construct a 3d array of nodes and their neighbors
	var nodeNeighbors = graph.loadNeighborNodes();
	//construct a 3d array of edges and their neighbors
	var edgeNeighbors = graph.loadNeighborEdges();

	var vis = new pv.Panel()
	    .width(width)
	    .height(height)
	    .fillStyle("white")
	    .event("mousedown", pv.Behavior.pan())
	    .event("mousewheel", pv.Behavior.zoom(1.3));

	var force = vis.add(pv.Layout.Force)
		.nodes(graph.vertices)
	    .links(graph.edges)
	    //.dragConstant(.1)		// default: 0.1 
	    .chargeConstant(-1750)	// default: -40 
	    .chargeMinDistance(300)	// default: 2 
	    //.chargeMaxDistance(300)	// default: 500 
	    //.chargeTheta(.9)		// default: 9
	    //.springConstant(.1)		// default: 0.1 
	    //.springDamping(.3)		// default: 0.3 
	    .springLength(200)		// default: 20 
	    //.iterations(5000)
	    .bound(false);
	
	var activeLink = null; 
	var activeSource = null; 
	var activeTarget = null; 

	force.link.add(pv.Line)
		.fillStyle(function(d) { 
			if (this.parent.index == activeLink) { 
				return "#F4292C"; 
			} else if (graph.contains(edgeNeighbors[activeNode], this.parent.index)) {
				return "#F4292C"; 
			} else { 
				return "#eeeeee"; 
			} 
		}) 
		.strokeStyle(function() { return this.fillStyle(); }) 
		.event("mouseover", function(node, link) { 
			activeLink = this.parent.index; 
			activeSource = link.sourceNode.index; 
			activeTarget = link.targetNode.index; 
		}) 
		.event("mouseout", function() { 
			activeLink = null; 
			activeSource = null; 
			activeTarget = null; 
		}) 
	    //.lineWidth(0.5);
	
	var activeNode = null;

	force.node.add(pv.Dot)
		//.size(500) // for protovis-r3.2.js
	    .shapeSize(550)
	    .fillStyle(function(d) { 
	    	var idx = d.index; 
	    	if (idx == activeNode || idx == activeSource || idx == activeTarget ) {
	    		return "#F4292C"; 
	    	}
	    	if (graph.contains(nodeNeighbors[activeNode], idx)) { 
	    		return "#FEBC00"; 
            } 
	    	return d.fix ? "brown" : colors(d.group); 
	    })	    
	    .strokeStyle(function() { return this.fillStyle().darker(); })
	    .lineWidth(1)
	    .title(function(d) { return d.nodeName; })
	    .event("mouseover", function() { activeNode = this.index; })
	    .event("mouseout", function() { activeNode = null; })
	    .event("mousedown", pv.Behavior.drag())
	    .event("drag", force)
	    /*
	    .anchor("right").add(pv.Label)
	    	.text(function(d) { return d.nodeName; })
	    	.font("11px tahoma")
	    */
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

Graph.Visualization.Protovis.prototype = {
    /*
	 * Renders the graph.
	 * 
	 */	
	/*
	render : function() {
		this.vis.render();
	},

    /*
	 * Zooms in the graph.
	 * 
	 */	
	/*
	zoomin : function() {
		this.vis.transform = new pv.Transform().scale(10); 
		this.vis.render();
	},
	
    /*
	 * Zooms out the graph.
	 * 
	 */	
	/*
	zoomout : function() {
		this.vis.transform = new pv.Transform().scale(-10); 
		this.vis.render();
	}	
	*/
};
