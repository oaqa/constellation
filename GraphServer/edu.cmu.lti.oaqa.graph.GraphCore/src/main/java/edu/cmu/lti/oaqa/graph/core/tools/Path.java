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

import java.util.LinkedList;

import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Vertex;

public class Path{
	private LinkedList<Vertex> vertices = new LinkedList<Vertex>();
	private int length;
	
	public Path(){
		
	}
	
	public Path(int distance){
		this.length=distance;
	}
	
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
	public Path clone(){ //this is not used but will be useful if someday I want to support
						 //searching for multiple shortest paths
		Path newPath = new Path();
		newPath.length=this.length;
		newPath.vertices=Lists.newLinkedList(this.vertices);
		return newPath;
	}

	@Override
	public String toString(){
		String returnString = "[";
		for(Vertex v:this.vertices){
			Vertex underlyingVertex=(Vertex) v.getProperty("originalVertex");
			String underlyingvVertexName = underlyingVertex.getProperty("name").toString();
			if(underlyingvVertexName!=null){
				returnString+=underlyingvVertexName+" --> ";
			}
			else returnString+=underlyingVertex.toString()+" --> ";
		}
		returnString=returnString.substring(0, returnString.length()-5)+"]";
		returnString += " distance = " + String.valueOf(this.length);
		return returnString;
	}
}