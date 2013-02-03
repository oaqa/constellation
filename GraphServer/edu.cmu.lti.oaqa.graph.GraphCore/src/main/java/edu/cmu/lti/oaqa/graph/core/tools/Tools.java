package edu.cmu.lti.oaqa.graph.core.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

public class Tools {
	
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
			return vertex; //created vertex
		}
		else{
			return vertex; //found vertex
		}	
	}
	
	public static void clearGraph(Neo4jGraph graph){
		int count=0;
		System.out.print("clearing... ");
		
		Iterable<Edge> edges = graph.getEdges();
		for(Edge edge:edges){
			graph.removeEdge(edge);
			count++;
		}
		System.out.print(count+" edges removed, ");
		count=0;
		Iterable<Vertex> vertices = graph.getVertices();
		for(Vertex vertex:vertices){
			graph.removeVertex(vertex);
			count++;
		}
		System.out.print(count+" vertices removed, ");
		count=0;

		Set<String> keys = graph.getInternalIndexKeys(Vertex.class);
		Iterator<String> Itty = keys.iterator();
		while(Itty.hasNext()){
			String key = Itty.next();
			graph.dropKeyIndex(key, Vertex.class);
			count++;
		}
		System.out.print(count+" indices removed... ");
		System.out.println("done");

		System.out.print("final: ");
		count=0;
		edges = graph.getEdges();
		for(Edge edge:edges){
			count++;
		}
		System.out.print(count+" edges, ");
		count=0;
		vertices = graph.getVertices();
		for(Vertex vertex:vertices){
			count++;
		}
		System.out.print(count+" vertices, ");
		count=0;
		keys = graph.getInternalIndexKeys(Vertex.class);
		Itty = keys.iterator();
		while(Itty.hasNext()){
			String key = Itty.next();
			System.out.println("key: "+key);
			count++;
		}
		System.out.println(count+" indices");	
		System.out.println("");
	}
	
	public static void clearGraph(Graph graph){
		int count=0;
		System.out.print("clearing... ");
		
		Iterable<Edge> edges = graph.getEdges();
		for(Edge edge:edges){
			graph.removeEdge(edge);
			count++;
		}
		System.out.print(count+" edges removed, ");
		count=0;
		Iterable<Vertex> vertices = graph.getVertices();
		for(Vertex vertex:vertices){
			graph.removeVertex(vertex);
			count++;
		}
		System.out.print(count+" vertices removed, ");
		System.out.println("done");

		System.out.print("final: ");
		count=0;
		edges = graph.getEdges();
		for(Edge edge:edges){
			count++;
		}
		System.out.print(count+" edges, ");
		count=0;
		vertices = graph.getVertices();
		for(Vertex vertex:vertices){
			count++;
		}
		System.out.println(count+" vertices, ");
		System.out.println("");
	}
	
	public static boolean connectionExists(Vertex v1, Vertex v2, String label, Boolean directed){
		if(directed==true) return v1.query().labels(label).direction(Direction.OUT).count()>0;
		else return v1.query().labels(label).direction(Direction.BOTH).count()>0;
	}
	
	public static boolean connectionExists(Vertex v1, Vertex v2, Boolean directed){
		if(directed==true) return v1.query().direction(Direction.OUT).count()>0;
		else return v1.query().direction(Direction.BOTH).count()>0;
	}
	
	public static boolean connectionExists(Vertex v1, Vertex v2, String label){
		return v1.query().labels(label).direction(Direction.BOTH).count()>0;
	}
	
	public static boolean connectionExists(Vertex v1, Vertex v2){
		return v1.query().direction(Direction.BOTH).count()>0;
	}
	
	public static List combineLists(List...lists){ //needs testing
		List outList = new ArrayList();
		for(List list:lists){
			outList.addAll(list);
		}
	return outList;
	}
	
	public static List<Edge> getEdges(Vertex v1, Vertex v2){
		List<Edge> edges = new ArrayList<Edge>();
		for(Edge e:v1.query().direction(Direction.OUT).edges()){
			if(e.getVertex(Direction.IN).equals(v2)) edges.add(e);
		}
		System.out.println("here!!");
		for(Edge e:v1.query().direction(Direction.IN).edges()){
			if(e.getVertex(Direction.OUT).equals(v2)) edges.add(e);
		}
		return edges;
	}
	
	public static Vertex getSingularVertexFromIndex(KeyIndexableGraph graph, String key, Object value) {
		Iterator<Vertex> vertices = graph.getVertices(key, value).iterator();
		if(!vertices.hasNext()) throw new Error("No vertex could be found under the key \""+key+"\" and " +
				"the value \""+value+"\"");
		Vertex v = vertices.next();
		if(vertices.hasNext()) throw new Error("More than one vertex was found under the key \""+key+"\" and " +
				"the value \""+value+"\"");
		return v;
	}
	
	public static Vertex getOtherVertexOnEdge(Edge edge, Vertex vertex){
		Vertex inV = edge.getVertex(Direction.IN);
		Vertex outV = edge.getVertex(Direction.OUT);
		if( !(inV.equals(vertex)||outV.equals(vertex)) ) throw new Error("vertex is not part of this edge");
		
		if(inV.equals(vertex)) return outV;
		else return inV;
	}

}
