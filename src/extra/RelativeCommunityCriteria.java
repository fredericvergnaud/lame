package extra;

import java.util.Set;
import java.util.Vector;

import edu.uci.ics.jung.algorithms.filters.FilterUtils;
import edu.uci.ics.jung.graph.Graph;

public abstract class RelativeCommunityCriteria<V,E> implements RelativeCriteria<V>{
	protected Graph<V, E> graph;
	//protected Vector<Graph<V,E>> communities;
	
	public RelativeCommunityCriteria (Graph<V, E> graph){
		this.graph = graph;
	}
	
	protected abstract double evaluateCommunities(Vector<Graph<V,E>> communities);

	@Override
	public double evaluate(Vector<Set<V>> clusters) {
		return evaluateCommunities(new Vector<Graph<V,E>>(FilterUtils.createAllInducedSubgraphs(clusters, graph)));
	}
	
}
