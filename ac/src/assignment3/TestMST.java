/**
 * 
 */
package assignment3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import graphs.*;

/**
 * @author donwen
 *
 */
public class TestMST {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String dgFileName = "mediumEWG.txt";
		In in = new In(TestDfS.INPUT_DATA_DIR + dgFileName);
		
		long start = System.currentTimeMillis();
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        long timeLoadingEWG = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        KruskalMST mst = new KruskalMST(G);
        System.out.println(String.format("Finds MST in %s, time spent:\n\tloading EWG from %s: %d ms\n\tfinding MST: %d ms", dgFileName, dgFileName, timeLoadingEWG, System.currentTimeMillis() - start));
//        for (Edge e : mst.edges()) {
//            StdOut.println(e);
//        }
//        StdOut.printf("%.5f\n", mst.weight());
	}

}
