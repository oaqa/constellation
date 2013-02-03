package edu.cmu.lti.oaqa.graph.core.tools;

import java.util.List;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class Traversals {
	
	/*
	public static List<Vertex> combineQuery(Query q, Vertex...vertices){
		List<Vertex> results = new ArrayList<Vertex>();
		for(Vertex vertex:vertices){
			vertex.query();
			q.
		}
		return list;
	}
	*/
	
	public static List<Vertex> getAdjacentWithProperty(Vertex vertex, String key, Object value){
		List<Vertex> list = (List<Vertex>) vertex.query().has(key, value).vertices();
		return list;
	}
	
}

//who is the main character of the movie harry potter

//[harry potter]->character in->[hermione granger]
//				->character in->[ron weasley]
//				->character in->[harry potter]