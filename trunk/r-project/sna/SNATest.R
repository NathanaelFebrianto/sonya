# SNA Test
# 
# Author: Younggue Bae
###############################################################################

library(igraph)
library(ggplot2)
library(animation)

###
# 1. Load the data into graph
###

edgelist <- read.csv("graph_edges.csv", header=T)
attributes <- read.csv("graph_vertices.csv", header=T)
graph <- graph.data.frame(d = edgelist, directed = FALSE, vertices = attributes)
print(graph)

# shortest path
shortest.paths(graph, V(graph)[0])

###
# 2. Plot Betweenness Centrality vs. Eigenvector Centrality
###

# Store metrics in new data frame
cent <- data.frame(V(graph)$name, betweenness = betweenness(graph), eigenvector = evcent(graph)$vector)
print(cent)

res <- lm(eigenvector~betweenness, data = cent)$residuals
print(res)
cent <- transform(cent, res = res)
print(cent)

p <- ggplot(cent, 
		aes(x = betweenness, 
		y = eigenvector, 
		label = cent$V.graph..name, 
		#label = rownames(cent), 
		colour = res, 
		size = abs(res)))

p + geom_text() + opts(title = "Key Actor Analysis for Sample Graph")


###
# 3. Plot a graph
###

# set node label
V(graph)$label <- V(graph)$name

# set vertex size
V(graph)$vertex.size =10

# set the edge arrow width
E(graph)$arrow.size=1.6

# set edge color (named colors per the list above)
E(graph)$color = "blue"

plot.igraph(graph)
l <- layout.fruchterman.reingold(graph, niter = 1000)


###
# 4A. COMMUNITY DETECTION: HIERARCHICAL CLUSTERING
###

# This is a generic form of cluster analysis that seeks to form 
# clusters based on a given distance metric between two nodes. For 
# this lab, we'll use the default settings of the dist() and 
# hclust() functions, which results in clusters being constructed
# based on Euclidean distance and complete linkages between 
# clusters. Other options exist for those who want to do additional
# exploring - just type "?hclust" and "?dist".

# The first step is to coerce our data into an adjacency matrix.
m_friend_matrix <- get.adjacency(graph)
m_friend_matrix

# Now we can use the built-in function dist() to compute a
# "distance matrix" showing how structurally dissimilar each
# vertex is to each other vertex. Note that higher values indicate
# greater dissimilarity.
m_friend_dist <- dist(m_friend_matrix)
m_friend_dist

# hclust() performs a hierarchical agglomerative NetCluster 
# operation based on the values in the dissimilarity matrix 
# yielded by dist() above. 
m_friend_hclust <- hclust(m_friend_dist)
m_friend_hclust

# The default way to visualize clusters is a tree structure called
# a dendrogram. The y-axis values on the dendrogram show the 
# Euclidean distances between nodes. Since the hclust() default
# is complete linkage, the distances between one or more nodes and
# a cluster of nodes is the Euclidean distance of the two nodes 
# farthest apart.
plot(m_friend_hclust)
# Question #1 - How many clusters would you select here and why?


###
# 3B. COMMUNITY DETECTION: EDGE BETWEENNESS METHOD (
###

# The edge-betweenness score of an edge measures the number of
# shortest paths from one vertex to another that go through it. 
# The idea of the edge-betweenness based community structure
# detection is that it is likely that edges connecting separate
# cluster have high edge-betweenness, as all the shortest paths
# from one cluster to another must traverse through them. So if we
# iteratively remove the edge with the highest edge-betweenness
# score we will get a hierarchical map of the communities in the
# graph. 
#
# The following function will find the betweenness for each
# vertex.
friend_comm_eb <- edge.betweenness.community(graph, directed = FALSE)
friend_comm_eb

# This process also lends itself to visualization as a dendrogram.
# The y-axis reflects the distance metric used by the edge betweennes 
# algorithm; for more on this, see M Newman and M Girvan: Finding and 
# evaluating community structure in networks, Physical Review E 69, 026113
# (2004), http://arxiv.org/abs/cond-mat/0308217. 
#plot(as.dendrogram(friend_comm_eb))

# Question #3 - How many clusters would you select here and why?


# The following code produces an animation of the edge-betweeness
# process. It's easiest to run all of this code at once. The 
# result is a set of .png files that will be saved to the default 
# working directory (or the working directory specified by setwd(), 
# if any). Note that the code may throw an error, but this doesn't 
# necessarily mean it didn't work; check the appropriate folder 
# for the .png files to see. Highlight all the .png files and open 
# them in Preview as a slideshow. 

# Before running this code, you need to install ImageMagick:
# http://www.imagemagick.org/script/binary-releases.php
# Scroll down to the Windows/Mac section (you probably do not want
# the Unix files at the top.) 

# *** START ANIMATION CODE ***

jitter.ani <- function(x, g) {
	
	l <- layout.kamada.kawai(g, niter = 1000)
	ebc <- edge.betweenness.community(g)
	
	colbar <- rainbow(6)
	colbar2 <- c(rainbow(5), rep("black",15))
	
	for (i in 1:x) {
		g2 <- delete.edges(g, ebc$removed.edges[seq(length=i-1)])
		eb <- edge.betweenness(g2)
		cl <- clusters(g2)$membership
		q <- modularity(g, cl)
		E(g2)$color <- "grey"
		E(g2)[ order(eb, decreasing=TRUE)[1:5]-1 ]$color <- colbar2[1:5]
		
		E(g2)$width <- 1
		E(g2)[ color != "grey" ]$width <- 2
		
		plot(g2, layout=l, vertex.size=12,
				edge.label.color="red", vertex.color=colbar[cl+2],
				edge.label.font=2)
		title(main=paste("Q=", round(q,3)), font=2)
		ty <- seq(1, by=-strheight("1")*1.5, length=20)
		text(-1.3, ty, adj=c(0,0.5), round(sort(eb, dec=TRUE)[1:20],2),
				col=colbar2, font=2)
	}
}

saveMovie(jitter.ani(5, graph), interval = 1, outdir = getwd())

# *** END ANIMATION CODE ***

#############################################
# Plot the clusters of graph.
#############################################
graph.cluster <- function(x, g) {
	
	l <- layout.kamada.kawai(g, niter = 1000)
	ebc <- edge.betweenness.community(g)
	
	colbar <- rainbow(6)
	colbar2 <- c(rainbow(5), rep("black",15))
	
	V(g)$label <- V(graph)$name
	
	q_max <- 0
	g2_final <- NULL
	eb_final <- NULL
	cl_final <- NULL
	
	for (i in 1:x) {
		g2 <- delete.edges(g, ebc$removed.edges[seq(length=i-1)])
		eb <- edge.betweenness(g2)
		cl <- clusters(g2)$membership
		q <- modularity(g, cl)		
		if (q > q_max) {
			q_max <- q
			g2_final <- g2
			eb_final <- eb
			cl_final <- cl
		}			
	}
	cat("MAX MODULARITY == " , q_max)
	
	plot(g2_final, layout=l, vertex.size=12,
			edge.label.color="red", vertex.color=colbar[cl_final+2],
			edge.label.font=2)
	title(main=paste("Q=", round(q_max,3)), font=2)
	ty <- seq(1, by=-strheight("1")*1.5, length=20)
	text(-1.3, ty, adj=c(0,0.5), round(sort(eb_final, dec=TRUE)[1:20],2),
			col=colbar2, font=2)
}

graph.cluster(6, graph)