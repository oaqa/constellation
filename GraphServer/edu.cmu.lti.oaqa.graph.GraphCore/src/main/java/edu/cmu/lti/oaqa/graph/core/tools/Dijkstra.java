/*
 *  Copyright 2012 Jack Montgomery
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cmu.lti.oaqa.graph.core.tools;

import java.util.Iterator;
import java.util.SortedSet;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

/**
 * @author Jack Montgomery
 *
 */
public class Dijkstra {
	//methods for using the ShortestPathFinder statically
	
	//finds the shortest path between two vertices in a graph
	public static Path run(Vertex v1, Vertex v2, String distanceLabel, boolean ignoreDirections) throws NoPathFoundException{
		Direction direction;
		if(ignoreDirections) direction=Direction.BOTH;
		else direction=Direction.OUT;
		ShortestPathFinder finder = new ShortestPathFinder(v1,v2, distanceLabel,direction);
		return finder.run();
	}
	
	//finds the shortest path between two vertices in a weighted, directed graph
	public static Path run(Vertex v1, Vertex v2, String distanceLabel) throws NoPathFoundException{
		return run(v1,v2,distanceLabel,false);
	}
	
	//finds the shortest path between two vertices in an unweighted graph
	public static Path run(Vertex v1, Vertex v2, boolean ignoreDirections) throws NoPathFoundException{
		return run(v1,v2,null,ignoreDirections);
	}
	
	//finds the shortest path between two vertices in an unweighted, directed graph
	public static Path run(Vertex v1, Vertex v2) throws NoPathFoundException{
		return run(v1,v2,null,false);
	}
}
//This is the class that actually does all of the work finding the shortest path
class ShortestPathFinder{
	//fields
	private Vertex v1; //stores the starting vertex
	private Vertex v2; //stores the end vertex
	private KeyIndexableGraph subGraph; //in memory graph that is a subGraph of the original graph
	private Vertex currentV_T; //the farthest vertex from the starting vertex in the subGraph for which we have
							   //found a definite shortest path
	private String distanceLabel; //the label of distance property in a weighted graph
	private TreeMultimap<Integer,Vertex> verticesSoFar = TreeMultimap.create(Ordering.natural(), Ordering.arbitrary());
									//verticesSoFar is a Red-Black tree backed map that stores
									//each vertex we have added to the subGraph but have not yet found a definite shortest path for
	private Direction direction; //the direction of edges that we should consider when traversing. OUT if searching in a
								 //directed graph, BOTH if searching in an undirected graph
	private Vertex v2_T; //the subGraph representation of the end vertex for which we are trying to find the shortest path
	
	//constructor
	public ShortestPathFinder(Vertex v1, Vertex v2, String distanceLabel, Direction direction){
		this.v1=v1;
		this.v2=v2;
		this.distanceLabel=distanceLabel;
		this.direction=direction;
		this.subGraph=new TinkerGraph();
		this.subGraph.createKeyIndex("originalVertex", Vertex.class);
	}
	
	public ShortestPathFinder(Vertex v1, Vertex v2, String distanceLabel, Direction direction, KeyIndexableGraph subGraph){
		this.v1=v1;
		this.v2=v2;
		this.distanceLabel=distanceLabel;
		this.direction=direction;
		this.subGraph=subGraph;
		this.subGraph.createKeyIndex("originalVertex", Vertex.class);
		initialization();
	}
	
	//this is the method that actually executes Dijkstra's algorithm
	//It is easy to see that this this process very closely matches
	//the process described by Dijkstra in in his 1959 paper in Problem 2
	public Path run() throws NoPathFoundException{
		//cannot find a path between a vertex and itself
		if(this.v1.equals(this.v2)) throw new NoPathFoundException("cannot find a path between a vertex and itself");
		initialization();
		//step1();
		//step2();
		repeat();
		return constructPath();
	}
	
	//this step sets everything up by moving the starting vertex to be the first currentVertex
	//and sets the distance of this first current distance to 0 (because the starting vertex is
	//distance of 0 away from itself.
	private void initialization(){
		//add the starting vertex to the subGraph
		this.currentV_T=findOrCreateVertex(this.subGraph, "originalVertex",v1); //O(1)
		//set the starting vertex's distance from itself in the subgraph to 0
		this.currentV_T.setProperty("distance", 0); //O(1)
		this.currentV_T.setProperty("complete", true); //O(1)
		//here we are putting the final vertex that we are trying to find into the subGraph
		//and we are storing it in a variable so that we can efficiently check whether or not
		//we have successfully found the shortest path to it.
		this.v2_T = findOrCreateVertex(this.subGraph,"originalVertex",v2); //O(1)
	}
	
	private void step1(){
		Vertex currentVertex = (Vertex) currentV_T.getProperty("originalVertex"); //O(1)
		//get all of the edges of the currentVertex (either outgoing or both directions depending on
		//whether we are working with a directed or undirected graph (which is specified by the user)
		Iterable<Edge> incidentEdges = currentVertex.query().direction(this.direction).edges(); //O(n) where n is the number of vertices
																								//adjacent to the currentVertex
		//get the distance from the starting vertex of the current vertex
		int currentDistance = (Integer) currentV_T.getProperty("distance"); //O(1)
		for(Edge edge:incidentEdges){
			Vertex neighbor = getOtherVertexOnEdge(edge,currentVertex); //O(1)
			//searches for neighboring vertex to the currentVertex in the subGraph. If it doesn't
			//find it, then it puts it in the subGraph with a distance of -1 (undefined)
			Vertex neighbor_T = findOrCreateVertex(this.subGraph, "originalVertex", neighbor); //O(log(n)) see helper method for runtime explaination
			//if we have already found the shortest path to this neighbor then continue on to the next neighbor
			if(((Boolean)neighbor_T.getProperty("complete"))==true){
				continue;
			}
			//gets the distance of the edge or if the graph is unweighted then uses 1 as a weight
			int distanceToNeighbor = getDistanceToNeighbor(edge); //O(1)
			//gets the current distance to the starting vertex of the neighboring vertex of the currentVertex
			int neighborsCurrentDistance = (Integer) neighbor_T.getProperty("distance"); //O(1)
			//if the neighbor's current distance to the starting vertex is not defined or if it is strictly
			//greater than the distance of the currentVertex to the starting vertex plus the distance from
			//the currentVertex to the neighbor vertex, then we adjust the neighbor's shortest path to the
			//the starting vertex to be through the currentVertex
			if(currentDistance+distanceToNeighbor<neighborsCurrentDistance ||
					neighborsCurrentDistance==-1){
				//set the neighbor's distance to be the the distance of the currentVertex to the starting
				//vertex plus the distance from the currentVertex to the neighbor vertex
				neighborsCurrentDistance=currentDistance+distanceToNeighbor;//O(1)
				//get rid of any incoming edges that the neighboring vertex has in the subGraph. This
				//is O(1) because our process only allows for one shortest path thus at most one edge
				//can exist to any vertex in the subGraph at any given time.
				for(Edge e:neighbor_T.query().direction(Direction.IN).edges()) subGraph.removeEdge(e);
				//create an edge in the subGraph from the currentVertex to the neighboring vertex
				Edge newEdge = subGraph.addEdge(null, this.currentV_T,neighbor_T, "next"); //O(1)
				if(this.distanceLabel!=null) newEdge.setProperty(this.distanceLabel, distanceToNeighbor);
				//set the neighboring Vertex's distance to be the new shortest distance given it's path
				//through the currentVertex
				neighbor_T.setProperty("distance", neighborsCurrentDistance); //O(1)
			}
			//add the neighboring vertex to the list of vertices that we have come across so far in
			//the original graph and have put in our subGraph somewhere (but have not found
			//the shortest path for yet)
			this.verticesSoFar.put(neighborsCurrentDistance, neighbor_T); //O(log(n)) because insertion in a Red-Black tree is log(n)
		}
	}
	
	private boolean step2() throws NoPathFoundException{
		//if there are no incomplete vertices left in our subgraph and we still haven't found
		//a path to our end vertex, then we know that no paths exist between the starting vertex
		//and the end vertex.  We know that this is the case because if a path did exist then either
		//the end vertex would be in our subGraph and would be incomplete or a vertex that has
		//a path to our end vertex would be in our subGraph. If there are no incomplete vertices
		//vertices left in the subGraph however, we know that neither of these cases are true
		//so therefore no path exists to the end vertex from the starting vertex.
		if(this.verticesSoFar.isEmpty()) throw new NoPathFoundException("could not find a path"); //O(1)
		
		//get the distance of the vertex (or vertices) that is closest to the starting vertex from the set of vertices we have
		//come across so far but are not yet complete and then get an arbitrary vertex of the set of vertices
		//that are of that distance from the starting vertex
		//O(log(n)) because the underlying data structure uses a Red-Black tree to search for
		//the minimum element		
		Integer firstKey = this.verticesSoFar.asMap().firstKey();
		SortedSet<Vertex> closestVertices_T = this.verticesSoFar.get(firstKey);
		Vertex closestV_T = closestVertices_T.first();
		
		//remove this closest vertex from the verticesSoFar set because we now know
		//this vertex's shortest path back to the starting vertex and thus should not
		//continue to evaluate it.
		this.verticesSoFar.remove(firstKey, closestV_T);
		
		//set the currentVertex to be the closest incomplete vertex that we have in our subGraph
		this.currentV_T=closestV_T; //O(1)
		//set the currentVertex's status as complete because we have now explored all
		//vertices in the graph that are closer to the starting vertex and thus we know
		//that we have already visited all vertices that lead to this vertex from the
		//starting vertex and thus can conclude that whatever distance we have for this vertex
		//at this time is in fact the shortest possible distance.
		//We can now use the currentVertex to find shortest paths to it's adjacent vertices.
		Boolean currentV_TsStatus = ((Boolean) this.currentV_T.getProperty("complete"));
		this.currentV_T.setProperty("complete", true); //O(1)
		return currentV_TsStatus;
	}
	
	//repeat steps 1 and 2 until we have completed the end vertex (until we have found a
	//path to the end vertex that is definitely the shortest path)
	
	public boolean doSteps() throws NoPathFoundException{
		step1();
		return step2();
	}
	
	private void repeat() throws NoPathFoundException{
		while(!(Boolean)this.v2_T.getProperty("complete")){ //O(1)
			doSteps();
		}
	}
	
	//Walk through the subgraph we have created, starting at the end vertex,
	//and add each vertex that we find along the way to path object (really just
	//a linked list with an integer that stores the total path length)
	private Path constructPath(){
		Path path = new Path((Integer) this.v2_T.getProperty("distance")); //O(1)
		Vertex v=this.v2_T; //O(1)
		//add the end vertex to the Path
		path.addFirst(v); //O(1)
		//add each of the vertices that lead to the end vertex in the subGraph
		//to the Path in reverse order (so that the path is in the correct order)
		while(!v.getProperty("originalVertex").equals(v1)){ //O(1)
			v=v.query().direction(Direction.IN).vertices().iterator().next(); //O(1)
			path.addFirst(v); //O(1)
		}
		return path;
	}
	
	//given an edge and a vertex, this method finds the other vertex on that edge
	//edges have only two vertices attached to them and each has O(1) lookup time
	//so this method is worst case O(1)
	private Vertex getOtherVertexOnEdge(Edge edge, Vertex vertex){
		Vertex inV = edge.getVertex(Direction.IN); //O(1)
		Vertex outV = edge.getVertex(Direction.OUT); //O(1)
		if( !(inV.equals(vertex)||outV.equals(vertex)) ) throw new Error("vertex is not part of this edge"); //O(1)
		if(inV.equals(vertex)) return outV; //O(1)
		else return inV; //O(1)
	}
	
	//this method looks up the distance that an edge has or return 1
	//if the edge has no distance (like in an unweighted graph, it weights all
	//edge equally). This has O(1) runtime because the distance of an edge
	//is a field of the edge object so its lookup is O(1)
	private int getDistanceToNeighbor(Edge edge){
		if(this.distanceLabel==null){ //O(1)
			return 1; //O(1)
		}
		else{
			int distance = (Integer) edge.getProperty(this.distanceLabel); //O(1)
			if(distance<0) throw new Error("found a negative distance, this breaks the algorithm"); //O(1)
			return distance; //O(1)
		}
	}
	
	//this method checks to see if a vertex is in the subGraph already or not. If it is in the subGraph
	//it returns that vertex as it exists in the subGraph, if not, then it adds that vertex to the subGraph.
	//No vertex in the original graph actually exists in the subGraph, the subGraph only contains
	//vertices that represent the original graph's vertices and each subGraph vertex has a pointer
	//directly to the original graph's vertex which it represents.
	private static Vertex findOrCreateVertex(KeyIndexableGraph graph, String key, Object value){
		Vertex vertex = null;
		Iterator<Vertex> vertices = graph.getVertices(key, value).iterator(); //O(log(n)) where n is
																			  //the number of vertices in the subgraph
		if(vertices.hasNext()) vertex=vertices.next(); //O(1)
		
		if (vertices.hasNext()){ //O(1)
			throw new Error("found more than one vertex with key/value pair: "+key+":"+value);
		}
		
		if (vertex==null){
			vertex=graph.addVertex(null); //O(1)
			vertex.setProperty(key, value); //O(1)
			vertex.setProperty("distance", -1); //O(1)
			vertex.setProperty("complete", false); //O(1)
			return vertex; //created vertex //O(1)
		}
		else{
			return vertex; //found vertex //O(1)
		}	
	}
}
