package assignment3;

import java.io.FileWriter;
import java.io.IOException;

import memoryManagement.BTree;
import memoryManagement.In;
import memoryManagement.BTree.Entry;

public class Task6 {
	
	public static void InorderVisitBTreeNode(BTree.Node h, int ht, FileWriter fw) throws IOException {
		Entry[] children = h.children;

        if (ht == 0) {
            for (int j = 0; j < h.m; j++) {
            	fw.write(children[j].key + "\n");
            }
        }
        else {
            for (int j = 0; j < h.m; j++) {
                InorderVisitBTreeNode(children[j].next, ht-1, fw);
            }
        }
	}

	public static void main(String[] args) throws IOException {
		final String sourceDateFile = "resources/ChIP-seq reads/ChIP-seq-reads-1M.dat";
		final String OutputDir = "output/";
		final String sortedFile = OutputDir + "B-tree.dat";
		
		System.out.println("Loading into BTee - '" + sourceDateFile + "'.");
		long start = System.currentTimeMillis();
		BTree<String, Integer> st = new BTree<String, Integer>();
		int counter = 0;
		try {
			In in = new In(sourceDateFile);
			
            while (!in.isEmpty()) {
                String s = in.readLine().trim();
                st.put(s, counter++);
            }
        }
        catch (Exception e) { System.out.println(e); }
		
		System.out.println("Write the keys (in al " + counter + ") in in-order to '" + sortedFile + "'.");
		
		FileWriter fw = new FileWriter(sortedFile);
		
		InorderVisitBTreeNode(st.root, st.height(), fw);
		
		fw.close();
		System.out.println("Done. Total CPU time is " + (System.currentTimeMillis() - start) + " ms");
	}

}
