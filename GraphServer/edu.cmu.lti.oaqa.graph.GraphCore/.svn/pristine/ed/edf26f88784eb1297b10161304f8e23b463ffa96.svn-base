package edu.cmu.lti.oaqa.graph.core.tools;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public class DoubleDijkstra {
	
	public static Path run(Vertex v1, Vertex v2, String distanceLabel, Direction direction) throws NoPathFoundException{
		return new BidirectionalFinder(v1,v2,distanceLabel,direction).run();
	}
}
	
class BidirectionalFinder{
	private KeyIndexableGraph subGraph = new TinkerGraph();
	private ShortestPathFinder d1;
	private ShortestPathFinder d2;
	private Vertex v1;
	private Vertex v2;
	
	public BidirectionalFinder(Vertex v1, Vertex v2, String distanceLabel, Direction direction) throws NoPathFoundException{
		this.d1 = new ShortestPathFinder(v1, v2, distanceLabel, direction, this.subGraph);
		this.d2 = new ShortestPathFinder(v2, v1, distanceLabel, direction, this.subGraph);
		run();
		d1.doSteps();
	}

	public Path run() throws NoPathFoundException {
		while(!this.d1.doSteps() && !this.d2.doSteps()){}
		return constructPath();
	}

	private Path constructPath() {
		Vertex v1_t = this.subGraph.getVertices("originalVertex", this.v1).iterator().next();
		Vertex v2_t = this.subGraph.getVertices("originalVertex", this.v2).iterator().next();
		Path path = new Path();
		//while(true){
			//prevVertex=
			//path.add(v)
		//}
		return null;
		
	}
}
