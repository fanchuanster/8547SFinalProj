package assignment3;

import java.util.stream.StreamSupport;

import graphs.CC;
import graphs.Graph;
import graphs.Queue;
import graphs.SymbolGraph;

public class TestConnectedComponents {

	public static void main(String[] args) {
		String dgFileName = "movies.txt";
		SymbolGraph sg = new SymbolGraph(TestDfS.INPUT_DATA_DIR + dgFileName, "/");
        Graph G = sg.G();
        
        long start = System.currentTimeMillis();
        CC cc = new CC(G);
        int componentsNumber = cc.count();
        long end = System.currentTimeMillis();

        System.out.println(componentsNumber + " connected components found in " + dgFileName);

        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[componentsNumber];
        for (int i = 0; i < componentsNumber; i++) {
            components[i] = new Queue<Integer>();
        }
        for (int v = 0; v < G.V(); v++) {
            components[cc.id(v)].enqueue(v);
        }

        // print connected components.
//        for (int i = 0; i < componentsNumber; i++) {
//            for (int v : components[i]) {
//            	System.out.print(v + " ");
//            }
//            System.out.println();
//        }
        
        System.out.println(String.format("Task #3 - It took %d ms to use DFS identifying the components and %d connected components identified in %s.", end-start, componentsNumber, dgFileName));
        
        System.out.println("Task #4 - ");
        String[] actors = {"DiCaprio, Leonardo", "Roberts, Julia (I)", "Grant, Hugh (I)"};
        for (String actor:actors) {
        	if (sg.contains(actor)) {
        		System.out.println("Movies starred by " + actor + ":");
        		int s = sg.index(actor);                
                for (int v : G.adj(s)) {
                	System.out.println("\t" + sg.name(v));
                }
            }
            else {
            	System.out.println("did not find movies starred by '" + actor + "'\n");
            }
        }
        System.out.println("Movies starred by both Roberts, Julia (I) and Grant, Hugh (I):");
		int sRobertsJulia = sg.index("Roberts, Julia (I)");
		int sGrantHugh = sg.index("Grant, Hugh (I)");
        for (int v : G.adj(sRobertsJulia)) {
        	if (StreamSupport.stream(G.adj(sGrantHugh).spliterator(), false).anyMatch(n -> n == v)) {
        		System.out.println("\t" + sg.name(v));
        	}        	
        }
	}
}
