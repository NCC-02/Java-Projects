import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * Intuitive Topological Sort implementation that uses a different 
 * @author Nicolas Coles
 * @version 1.0
 *
 */
public class IntuitiveTopological implements TopologicalSort{

	private Iterable<Integer> order;
	
	public IntuitiveTopological(BetterDiGraph graph) {
		topologicalOrder(graph);
	}
	
	public HashMap<Integer, Integer> inDegreesMap(BetterDiGraph graph) {
		HashMap<Integer, Integer> in = new HashMap<>();
		for (int i : graph.vertices()) {
			in.put(i, graph.getIndegree(i));
		}
		return in;
	}
	
	public void topologicalOrder(BetterDiGraph graph) {
		LinkedList<Integer> topOrder = new LinkedList<>(); // topology order
		
		while (graph.getVertexCount() > 0) {
			HashMap<Integer, Integer> inDegree = inDegreesMap(graph);
			for (int i : inDegree.keySet()) {
				if (inDegree.get(i) == 0) { //adds the node with in-degree zero to the topological ordering and removes it from the graph
					topOrder.add(i); 		// until graph is empty.
					graph.removeVertex(i);
				}
			}
		}
		order = topOrder;
	}
	
	
	@Override
	public Iterable<Integer> order() {
		return order;
	}

	@Override
	public boolean isDAG() {
		return order == null;
	}

}
