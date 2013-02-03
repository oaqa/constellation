package edu.cmu.lti.oaqa.graph.core.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

public class Search {	
	public static List<Vertex> searchVertices(KeyIndexableGraph graph, Object searchObject, int maxResults, String...keys){
		List<Vertex> results = new ArrayList<Vertex>();
		List<Iterator<Vertex>> ittys  = new ArrayList<Iterator<Vertex>>();
		for(String key:keys){
			ittys.add(graph.getVertices(key, searchObject).iterator());	
		}
		
		int count=0;
		while(count<maxResults && !ittys.isEmpty()){
			int current = count%ittys.size();
			Iterator<Vertex> itty = ittys.get(current);
			if(itty.hasNext()){
				Vertex v = itty.next();
				if(!results.contains(v)){
					results.add(v);
					count++;
				}
			}
			else{
				ittys.remove(itty);
			}
		}
	return results;
	}
	
	public static List<Edge> searchEdges(KeyIndexableGraph graph, Object searchObject, int maxResults, String...keys){
		List<Edge> results = new ArrayList<Edge>();
		
		List<Iterator<Edge>> ittys  = new ArrayList<Iterator<Edge>>();
		for(String key:keys){
			ittys.add(graph.getEdges(key, searchObject).iterator());	
		}
		
		int count=0;
		while(count<maxResults && !ittys.isEmpty()){
			int current = count%ittys.size();
			Iterator<Edge> itty = ittys.get(current);
			if(itty.hasNext()){
				Edge e = itty.next();
				if(!results.contains(e)){
					results.add(e);
					count++;
				}
			}
			else{
				ittys.remove(itty);
			}
		}
	return results;
	}
}