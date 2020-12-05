package assignment3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import heaps.BinaryHeap;
import memoryManagement.*;

public class Task5 {
	
	public static void sortFile(String inputFile, String outputFile) throws IOException {
		System.out.println("Sorting " + inputFile + " as " + outputFile + ".");
		BinaryHeap<String> h = new BinaryHeap<String>();
		try {
			In in = new In(inputFile);
            while (!in.isEmpty()) {
                String s = in.readLine();
                h.insert(s);
            }
        }
        catch (Exception e) { System.out.println(e); }
		
		FileWriter fw = new FileWriter(outputFile);
		while ( !h.isEmpty() ) {
			String min = h.deleteMin();
			fw.write(min + "\n");
		}
		fw.close();
	}
	
	public static void multiWay(String[] files, String outputFile) throws IOException {
		System.out.println("Merge sorting following files:");
		for (String f:files) {
			System.out.println(f);
		}
		int N = files.length; 
        In[] streams = new In[N]; 
        for (int i = 0; i < N; i++) 
            streams[i] = new In(files[i]);
        
        IndexMinPQ<String> pq = new IndexMinPQ<String>(N); 
        for (int i = 0; i < N; i++) 
            if (!streams[i].isEmpty()) 
                pq.insert(i, streams[i].readString()); 
        
        FileWriter fw = new FileWriter(outputFile);
        while (!pq.isEmpty()) {
        	fw.write(pq.minKey() + "\n");
            int i = pq.delMin(); 
            if (!streams[i].isEmpty()) 
                pq.insert(i, streams[i].readString()); 
        }
        fw.close();
        System.out.println("Merge Sorting done into '" + outputFile + "'");
	}

	public static void main(String[] args) throws IOException {
		final String sourceDateFile = "resources/ChIP-seq reads/ChIP-seq-reads-1M.dat";
		final String OutputDir = "output/";
		final String sortedFile = OutputDir + "Chip-seq-reads-1M-sorted.dat";
		
		String[] sublists = { "A.dat", "B.dat", "C.dat", "D.dat" };
		String[] sortedFiles = { "AS.dat", "BS.dat", "CS.dat", "DS.dat" };
		
		for (int i=0; i<sublists.length; i++) {
			sublists[i] = OutputDir + sublists[i];
			sortedFiles[i] = OutputDir + sortedFiles[i];
		}
		
		long start = System.currentTimeMillis();
		
		System.out.println("Partitioning '" + sourceDateFile + "' into following files:");
		for (String f:sublists) {
			System.out.println(f);
		}
		HashMap<String, FileWriter> outputs = new HashMap<String, FileWriter>();
		for (String f:sublists) {
			outputs.put(f, new FileWriter(f));
		}

		try {
			int mod = sublists.length;
			int c = 0;
			In in = new In(sourceDateFile);
			FileWriter fw;
            while (!in.isEmpty()) {
                String s = in.readLine().trim();
                fw = outputs.get(sublists[c++ % mod]);
                fw.write(s + "\n");
            }
        }
        catch (Exception e) { System.out.println(e); }
		
		for (String f:sublists) {
			outputs.get(f).close();
		}
		
		for (int i=0; i<sublists.length; i++) {
			sortFile(sublists[i], sortedFiles[i]);
		}
		
		multiWay(sortedFiles, sortedFile);
		
		System.out.println("Total CPU time is " + (System.currentTimeMillis()-start) + " ms");
	}
}
