import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.HashMap;
/**
 * 
 * @author Nicolas Coles
 * @version 1.0
 *
 */
public class BetterDiGraph implements EditableDiGraph {
	
	private int V;
	private int E;
	private HashMap<Integer, LinkedList<Integer>> adj;
	
	public BetterDiGraph(int V) {
		if (V < 0)
			throw new IllegalArgumentException();
		this.V = V;
		this.E = 0;
		this.adj = new HashMap<>();
	}
	
	public BetterDiGraph() {
		this(0);
	}
	
	@Override
	public void addEdge(int v, int w) {
		if (!containsVertex(v)) //same as contains in a symbol table
			addVertex(v);
		if (!containsVertex(w))
			addVertex(w);
		
		adj.get(v).push(w);
		E++;
	}

	@Override
	public void addVertex(int v) {
		if (!containsVertex(v)) {
			adj.put(v, new LinkedList<>());
			V++;
		}
		else
			System.out.println("Vertex already exists.");
	}

	@Override
	public Iterable<Integer> getAdj(int v) {
		return adj.get(v);
	}

	@Override
	public int getEdgeCount() {
		return E;
	}

	@Override
	public int getIndegree(int v) throws NoSuchElementException {
		if (!containsVertex(v))
			throw new NoSuchElementException("Vertex " + v + " does not exist");
		int index = 0;
//		for (int i = 0; i < vertices(); i++) {
		for (int i : vertices()) {
			if (i != v)
				if (adj.get(i).contains(v))
					index++;
		}
		return index;
	}

	@Override
	public int getVertexCount() {
		
		return V;
	}

	@Override
	public void removeEdge(int v, int w) {
		if (!containsVertex(v)) {
			System.out.println("Vertex " + v + " does not exist");
			return;
		}
		LinkedList<Integer> curr = adj.get(v);
		int index = curr.indexOf(w);
		if (index >= 0) { //
			curr.remove(index);
			E--;
		}
	}

	@Override
	public void removeVertex(int v) {
		if (!containsVertex(v)) {
			System.out.println("Vertex " + v + " does not exist.");
			return;
		}
		for (int i : adj.keySet()) { //all keys in the table in sorted order
			if (i != v) {
				if (adj.get(i).indexOf(v) >= 0)
					removeEdge(i, v);
			}
		}
		
		adj.remove(v);
		V--;
		
	}

	@Override
	public Iterable<Integer> vertices() {
		return adj.keySet();
	}

	@Override
	public boolean isEmpty() {
		return V == 0;
	}

	@Override
	public boolean containsVertex(int v) {
		
		return adj.containsKey(v);
	}
	

}
