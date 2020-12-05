/**
 * 
 */
package assignment3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import graphs.DepthFirstOrder;
import graphs.Digraph;
import graphs.DijkstraSP;
import graphs.DirectedEdge;
import graphs.EdgeWeightedDigraph;
import graphs.In;
import graphs.StdOut;

/**
 * @author donwen
 *
 */
public class TestAllPairsSP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String dgFileName = "mediumEWG.txt";
		File file = new File(TestDfS.INPUT_DATA_DIR + dgFileName);
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(new In(file));
        
		double[][] shortestDistances = new double[G.V()][G.V()];
		
		System.out.println(String.format("Task #2 - Find shortest path for all pairs in %s", dgFileName));
		
		long start = System.currentTimeMillis();
		
		for (int s=0; s<G.V(); s++) {
			// compute shortest paths with Dijkstra algorithm.
	        DijkstraSP sp = new DijkstraSP(G, s);

	        for (int t = 0; t < G.V(); t++) {
	            if (sp.hasPathTo(t)) {
	            	shortestDistances[s][t] = sp.distTo(t);
	            }
	            else {
	            	shortestDistances[s][t] = -1;
	            }
	        }
		}
		long total = System.currentTimeMillis() - start;
		System.out.println(String.format("Total time spent: %d ms.", total));
		
		int outputMatrixSize = 10;
		String outfile = "sp_all_pairs.txt";
		BufferedWriter out = null;
		
		System.out.println(String.format("Here is a snippet of shortest paths matrix(%d X %d), see full results in %s", outputMatrixSize, outputMatrixSize, outfile));
		
		try {
			out = new BufferedWriter(new FileWriter(outfile));
			for (int i=0; i<G.V(); i++) {
				for (int j=0; j<G.V(); j++) {
					String spString = "N   ";
					if (shortestDistances[i][j] >= 0) {
						spString = String.format("%.2f", shortestDistances[i][j]);
					}
					out.write(spString + "\t");
					
					if (i < outputMatrixSize && j < outputMatrixSize) {
						System.out.print(spString + "\t");
					}
				}
				out.write("\n");
				if (i < outputMatrixSize) {
					System.out.println();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    if(out != null) {
		        try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
		
		
	}

}
