package edu.cmu.lti.oaqa.graph.core.tools;

import java.util.Set;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Features;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;

public class CoreGraph implements KeyIndexableGraph {

	private KeyIndexableGraph graph;
	private Tools tools = new Tools();
	
	public CoreGraph(KeyIndexableGraph graph){
		this.graph=graph;
	}
	
	
	//getter and setters
	public KeyIndexableGraph getUnderlyingGraph() {
		return graph;
	}
		
		
	//new methods
	public Vertex getOtherVertexOnEdge(Vertex vertex, Edge edge){
		return this.tools.getOtherVertexOnEdge(edge, vertex);
	}
	
	public Iterable<Edge> getEdgesBetweenTwoVertices(Vertex v1, Vertex v2){
		return this.tools.getEdges(v1, v2);
	}
	
	public Vertex getSingularVertexFromIndex(String key,Object value){
		return this.tools.getSingularVertexFromIndex(this.graph, key, value);
	}
	
	public void clearGraph(){
		//TODO need to change this so that it drops keys too
		this.tools.clearGraph(graph);
	}
	
	public Vertex findOrCreateVertex(String key, Object value){
		return this.tools.findOrCreateVertex(this.graph, key, value);
	}
	
	public Iterable<Vertex> multiIndexSearch(Object searchObject){
		//TODO figure out how to utilize the graphcore search here
		return null;
	}
	
	public Iterable<Vertex> findShortestPath(Vertex v1, Vertex v2){
		//TODO need to implement Dijkstra or DoubleDijkstra search here
		return null;
	}
	
	//implemented methods	
	public Features getFeatures() {
		return graph.getFeatures();
	}

	public Vertex addVertex(Object id) {
		return graph.addVertex(id);
	}

	public Vertex getVertex(Object id) {
		return graph.getVertex(id);
	}

	public void removeVertex(Vertex vertex) {
		graph.removeVertex(vertex);	
	}

	public Iterable<Vertex> getVertices() {
		return graph.getVertices();
	}

	public Iterable<Vertex> getVertices(String key, Object value) {
		return graph.getVertices(key, value);
	}

	public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex,
			String label) {
		return graph.addEdge(id, outVertex, inVertex, label);
	}

	public Edge getEdge(Object id) {
		return graph.getEdge(id);
	}

	public void removeEdge(Edge edge) {
		graph.removeEdge(edge);
	}

	public Iterable<Edge> getEdges() {
		return graph.getEdges();
	}

	public Iterable<Edge> getEdges(String key, Object value) {
		return graph.getEdges(key, value);
	}

	public void shutdown() {
		graph.shutdown();
	}

	public <T extends Element> void dropKeyIndex(String key,
			Class<T> elementClass) {
		graph.dropKeyIndex(key, elementClass);
	}

	public <T extends Element> void createKeyIndex(String key,
			Class<T> elementClass) {
		graph.createKeyIndex(key, elementClass);
	}

	public <T extends Element> Set<String> getIndexedKeys(Class<T> elementClass) {
		return graph.getIndexedKeys(elementClass);
	}

}
