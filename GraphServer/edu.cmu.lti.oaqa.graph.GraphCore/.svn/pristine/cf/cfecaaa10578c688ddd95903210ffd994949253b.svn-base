package edu.cmu.lti.oaqa.graph.core.tools;

import java.util.LinkedList;

import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Vertex;

public class Path2{
	private LinkedList<Vertex> vertices = new LinkedList<Vertex>();
	private int length;
	
	public int getDistance(){
		return this.length;
	}
	
	public void addFirst(Vertex v) {
		vertices.addFirst(v);
	}
	
	public void add(Vertex v){
		vertices.add(v);
	}
	
	@Override
	public Path2 clone(){
		Path2 newPath = new Path2();
		newPath.length=this.length;
		newPath.vertices=Lists.newLinkedList(this.vertices);
		return newPath;
	}

	@Override
	public String toString(){
		String returnString = "[";
		for(Vertex v:this.vertices){
			String vertexName = v.getProperty("name").toString();
			if(vertexName!=null){
				returnString+=vertexName+" --> ";
			}
			else returnString+=v.toString()+" --> ";
		}
		returnString=returnString.substring(0, returnString.length()-5)+"]";
		return returnString;
	}
}