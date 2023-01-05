import java.util.HashMap;
import java.io.*;
/**
 * Program for generating kanji component dependency order via topological sort.
 * 
 * @author Nicolas Coles
 * @version 1.0
 */

public class Main {
	
    private BetterDiGraph graph;
    /**
     * Entry point for testing.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        graph = new BetterDiGraph();
		System.out.println("Run:");

    	new ColesMain().runKanji();
        
//    	SymbolTable<> ST = new HashTable<>();
    	
        //Freebie: this is one way to load the UTF8 formated character data.
        //BufferedReader indexReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data-kanji.txt")), "UTF8"));
    }
    private void runKanji() {
    	graph = new BetterDiGraph();
    	
    	try {
    		BufferedReader indexReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data-kanji.txt")), "UTF8"));
    		HashMap<Integer, Integer> kanji = new HashMap<>();
    		String curr;
    		
    		while ((curr = indexReader.readLine()) != null) { //creates kanji ID hashmap
//				System.out.println(curr);
//				System.out.println(!curr.substring(1, 2).equals("#"));
    			if (!curr.substring(0, 1).equals("#")) {
//    				System.out.println(curr.substring(0, 6));
    				
    				String[] s = curr.split("\t");
//    				for (int i = 0; i < s.length; i++)
//    					System.out.println(s[i]);
    				
    				int id = Integer.parseInt(s[0]);
//    				System.out.println(id);

    				int kanjiID = Character.codePointAt(s[1], 0); //ids
    				kanji.put(id, kanjiID);
    				graph.addVertex(id);
    			}
    		}
    		
    		indexReader.close();
    		
    		System.out.println("Original:");
    		for (int i : graph.vertices()) {
    			System.out.print(String.valueOf(Character.toChars(kanji.get(i)))); //prints Kanji characters
    		}
//			PrintStream out = new PrintStream(Stream.out, true, "UTF-8");

    		
    		//Sort
    		indexReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data-components.txt")), "UTF8"));
    		String current;
    		
    		while ((current = indexReader.readLine()) != null) { //creates kanji ID hashmap
    			if (!current.substring(0, 1).equals("#")) {
    				String[] s = current.split("\t");
    				
//    				int id = Integer.parseInt(s[0]);
//    				int kanjiID = Character.codePointAt(s[1], 0); //ids
    				
    				int v = Integer.parseInt(s[0]);
    				int w = Integer.parseInt(s[1]);
    				graph.addEdge(v, w);
    			}
    		}
    		indexReader.close();
    		IntuitiveTopological topoSort = new IntuitiveTopological(graph);
    		System.out.println("\nSorted:");
    		for (int i : topoSort.order())
    			System.out.print(String.valueOf(Character.toChars(kanji.get(i)))); //prints Kanji characters
    		
    		
    	} catch (IOException e) {
    		System.out.println("Cannot load file: " + e);
    	}
    }
}