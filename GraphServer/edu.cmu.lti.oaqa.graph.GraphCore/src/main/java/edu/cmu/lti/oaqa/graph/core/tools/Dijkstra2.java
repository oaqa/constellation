package edu.cmu.lti.oaqa.graph.core.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.collect.Maps;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public class Dijkstra2 {
	
	public static Path2 findShortestPath(Vertex v1, Vertex v2, String distanceProperty) throws NoPathFoundException{
		
		//returns the shortest path from v1 to v2
		ShortestPathFinder2 finder = new ShortestPathFinder2(v1,v2, distanceProperty);
		return finder.run();
	}
}

class ShortestPathFinder2{
	private Vertex v1;
	private Vertex v2;
	private KeyIndexableGraph tempGraph = new TinkerGraph(); //vertices and edges with the _T denote they belong to this graph
	private Vertex currentV;
	private String distanceProperty;
	private TreeMap<Integer,Vertex> verticesSoFar = new TreeMap<Integer,Vertex>();
	private Map<Vertex,Vertex> previousVertexMap = new HashMap<Vertex,Vertex>();
	private Map<Vertex,Integer> distanceMap = new HashMap<Vertex,Integer>();
	private List<Vertex> completedVertices = new ArrayList<Vertex>();
	
	public ShortestPathFinder2(Vertex v1, Vertex v2, String distanceProperty){
		this.v1=v1;
		this.v2=v2;
		this.distanceProperty=distanceProperty;
		this.tempGraph.createKeyIndex("originalVertex", Vertex.class);
	}
	
	public ShortestPathFinder2(Vertex v1, Vertex v2){
		this.v1=v1;
		this.v2=v2;
		this.distanceProperty="distance";
	}
	
	public Path2 run() throws NoPathFoundException{
		if(this.v1.equals(this.v2)) throw new NoPathFoundException("cannot find a path between a vertex and itself");
		step1();
		step2();
		step3();
		step4();
		return constructPath();
	}
	
	private void step1(){
		//this.currentV_T=findOrCreateVertex(this.tempGraph, "originalVertex",v1);
		//this.currentV_T.setProperty("distance", 0);
		this.currentV=this.v1;
		this.distanceMap.put(this.currentV, 0);
	}
	
	private void step2(){
		Iterable<Edge> incidentEdges = this.currentV.query().direction(Direction.OUT).edges();
		int currentDistance = this.distanceMap.get(this.currentV);
		for(Edge edge:incidentEdges){
			Vertex neighbor = getOtherVertexOnEdge(edge,this.currentV);
			int distanceToNeighbor = (Integer) edge.getProperty(this.distanceProperty);
			int neighborsCurrentDistance;
			if(this.distanceMap.containsKey(neighbor)) neighborsCurrentDistance=this.distanceMap.get(neighbor);
			else neighborsCurrentDistance=-1;
			
			if(currentDistance+distanceToNeighbor<neighborsCurrentDistance ||
					neighborsCurrentDistance==-1){
				neighborsCurrentDistance=currentDistance+distanceToNeighbor;
				this.previousVertexMap.put(neighbor, this.currentV);
				this.distanceMap.put(neighbor, neighborsCurrentDistance);
			}
			this.verticesSoFar.put(neighborsCurrentDistance, neighbor);
		}
		this.completedVertices.add(currentV); //changed up to here
	}
	
	private void step3() throws NoPathFoundException{
		TreeMap<Integer, Vertex> copyOfVerticesSoFar = Maps.newTreeMap(this.verticesSoFar);
		
		Vertex closestV=null;
		boolean closestVIsCommplete = true;
		
		while(closestVIsCommplete){
			Entry<Integer, Vertex> firstEntry = copyOfVerticesSoFar.firstEntry();
			if(firstEntry==null) throw new NoPathFoundException("could not find a path");
			closestV=firstEntry.getValue();
			closestVIsCommplete=(Boolean) this.completedVertices.contains(closestV);
			copyOfVerticesSoFar.remove(firstEntry.getKey());
		}
		if(closestV==null) throw new Error();
		this.currentV=closestV;
		
	}
	
	private void step4() throws NoPathFoundException{
		while(!this.completedVertices.contains(this.v2)){
			step2();
			step3();
		}
	}
	
	private Path2 constructPath(){
		Path2 path = new Path2();
		Vertex v=this.v2;
		path.add(v);
		while(!v.equals(this.v1)){
			v=this.previousVertexMap.get(v);
			path.addFirst(v);
		}
		return path;
	}
	
	private Vertex getOtherVertexOnEdge(Edge edge, Vertex vertex){
		Vertex inV = edge.getVertex(Direction.IN);
		Vertex outV = edge.getVertex(Direction.OUT);
		if( !(inV.equals(vertex)||outV.equals(vertex)) ) throw new Error("vertex is not part of this edge");
		
		if(inV.equals(vertex)) return outV;
		else return inV;
	}
	
	/*
	public static Vertex findOrCreateVertex(KeyIndexableGraph graph, String key, Object value){
		Vertex vertex = null;
		Iterator<Vertex> vertices = graph.getVertices(key, value).iterator();
		if(vertices.hasNext()) vertex=vertices.next();
		
		if (vertices.hasNext()){
			throw new Error("found more than one vertex with key/value pair: "+key+":"+value);
		}
		
		if (vertex==null){
			vertex=graph.addVertex(null);
			vertex.setProperty(key, value);
			vertex.setProperty("distance", -1);
			vertex.setProperty("complete", false);
			return vertex; //created vertex
		}
		else{
			return vertex; //found vertex
		}	
	}
	*/
}
