package edu.cmu.lti.oaqa.graph.constellation;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;

import edu.cmu.lti.oaqa.graph.canonical.GraphSearch;
import edu.cmu.lti.oaqa.graph.core.tools.Search;


public class CMUGraphSearch implements GraphSearch{
	
	private KeyIndexableGraph graph;
	
	public Iterable<Vertex> textSearch(String searchString) {
		// TODO add more indexes
		return Search.searchVertices(graph,searchString, 100, "name");
	}

	public Iterable<Vertex> typeSearch(String searchString) {
		return Search.searchVertices(graph,searchString, 100, "type");
	}

	public void setGraph(KeyIndexableGraph graph) {
		this.graph=graph;
	}
	
}