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
package edu.cmu.lti.graph.core.tools;

import java.util.List;

import com.google.common.collect.Lists;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import edu.cmu.lti.oaqa.graph.core.tools.Dijkstra;
import edu.cmu.lti.oaqa.graph.core.tools.DoubleDijkstra;
import edu.cmu.lti.oaqa.graph.core.tools.NoPathFoundException;
import edu.cmu.lti.oaqa.graph.core.tools.Path;

public class DoubleDijkstraTest {
	
	Vertex A,B,C,D,E,F,G,H;
	Graph graph;
	
	public static void main(String[] args) throws NoPathFoundException{
		new DoubleDijkstraTest().test();
	}
	
	public void test() throws NoPathFoundException{
		buildTestGraph();
		doTests();
	}
	
	private void doTests() throws NoPathFoundException{
		List<Vertex> vertices = Lists.newArrayList(A,B,C,D,E,F,G);
		for(Vertex v1: vertices){
			for (Vertex v2:vertices){
				try{
					Path shortestPath = DoubleDijkstra.run(v1,v2,"distance",Direction.OUT);
					System.out.println("("+v1.getProperty("name")+","+v2.getProperty("name")+"): "
					+ shortestPath.toString());
				}
				catch(NoPathFoundException e){
					System.out.println("could not find a path between "+v1.getProperty("name")+
							" and "+v2.getProperty("name"));
				}
				System.out.println();
			}
		}
	}
	
	private void buildTestGraph(){
		//graph from http://www.youtube.com/watch?v=8Ls1RqHCOPw
		String costPropertyKey = "distance";
		
		this.graph = new TinkerGraph();
		
		this.A = graph.addVertex(null);
		this.A.setProperty("name", "A");
		this.B = graph.addVertex(null);
		this.B.setProperty("name", "B");
		this.C = graph.addVertex(null);
		this.C.setProperty("name", "C");
		this.D = graph.addVertex(null);
		this.D.setProperty("name", "D");
		this.E = graph.addVertex(null);
		this.E.setProperty("name", "E");
		this.F = graph.addVertex(null);
		this.F.setProperty("name", "F");
		this.G = graph.addVertex(null);
		this.G.setProperty("name", "G");
		this.H = graph.addVertex(null);
		this.H.setProperty("name", "H");
		
		Edge edge = graph.addEdge(null, A, B, "edge");
		edge.setProperty(costPropertyKey, 20);
		
		edge = graph.addEdge(null, A, D, "edge");
		edge.setProperty(costPropertyKey, 80);
		
		edge = graph.addEdge(null, A, G, "edge");
		edge.setProperty(costPropertyKey, 90);
		
		edge = graph.addEdge(null, B, F, "edge");
		edge.setProperty(costPropertyKey, 10);
		
		edge = graph.addEdge(null, C, F, "edge");
		edge.setProperty(costPropertyKey, 50);
		
		edge = graph.addEdge(null, C, D, "edge");
		edge.setProperty(costPropertyKey, 10);
		
		edge = graph.addEdge(null, C, H, "edge");
		edge.setProperty(costPropertyKey, 20);
		
		edge = graph.addEdge(null, D, C, "edge");
		edge.setProperty(costPropertyKey, 10);
		
		edge = graph.addEdge(null, D, G, "edge");
		edge.setProperty(costPropertyKey, 20);
		
		edge = graph.addEdge(null, E, B, "edge");
		edge.setProperty(costPropertyKey, 50);
		
		edge = graph.addEdge(null, E, G, "edge");
		edge.setProperty(costPropertyKey, 30);
		
		edge = graph.addEdge(null, F, C, "edge");
		edge.setProperty(costPropertyKey, 10);
		
		edge = graph.addEdge(null, F, D, "edge");
		edge.setProperty(costPropertyKey, 40);
		
		edge = graph.addEdge(null, G, A, "edge");
		edge.setProperty(costPropertyKey, 20);
	}

}
