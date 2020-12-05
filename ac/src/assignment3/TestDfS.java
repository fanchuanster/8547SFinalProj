/**
 * 
 */
package assignment3;

import graphs.*;

/**
 * @author donwen
 *
 */
public class TestDfS {
	
	public static final String INPUT_DATA_DIR = "resources\\graphs\\";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String dgFileName = "mediumDG.txt";
		
		long start = System.currentTimeMillis();
		Digraph digraph = new Digraph(new In(INPUT_DATA_DIR + dgFileName));
		long createDGTime = System.currentTimeMillis() - start;
		start = System.currentTimeMillis();
		DepthFirstOrder dfs = new DepthFirstOrder(digraph);
		
        System.out.println(String.format("Task #1 - Run DFS on %s. Time spent: \n\tCreating DG from %s - %d ms\n\tDFS - %d ms", dgFileName, dgFileName, createDGTime, System.currentTimeMillis() - start));
        
        System.out.print("Pre-order:  ");
        for (int v : dfs.pre()) {
        	System.out.print(v + " ");
        }
        System.out.println();
        
        System.out.print("Post-order: ");
        start = System.currentTimeMillis();
        for (int v : dfs.post()) {
        	System.out.print(v + " ");
        }
        StdOut.println();
	}

}
