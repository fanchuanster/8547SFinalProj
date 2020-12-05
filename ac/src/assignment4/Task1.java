package assignment4;

import java.io.File;
import java.io.IOException;

import textprocessing.BoyerMoore;
import textprocessing.BruteForceMatch;
import textprocessing.KMP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Task1 {
	

	static String readFile(String pathname) throws IOException {

	    File file = new File(pathname);
	    StringBuilder fileContents = new StringBuilder((int)file.length());        

	    try (Scanner scanner = new Scanner(file)) {
	        while(scanner.hasNextLine()) {
	            fileContents.append(scanner.nextLine() + System.lineSeparator());
	        }
	        return fileContents.toString();
	    }
	}
	
	/**
	 * search pattern in text with given algorithm.
	 * @param pat pattern to search for
	 * @param text text string to search
	 * @param algorithm algorithm type to do the search, 1 - BoyerMoore; 2 - KMP; 3 - BruteForceMatch.
	 * @return indices of the pattern found in the text string.
	 */
	static int[] searchPat(String pat, String text, int algorithm) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int N = text.length();

		BoyerMoore boyermoore1 = new BoyerMoore(pat);
		KMP kmp = new KMP(pat);
		
		int start = 0;
		while (start < N) {
			int index = N;
			if (algorithm == 1) {
				index = boyermoore1.search(text.substring(start));
			} else if (algorithm == 2) {
				index = kmp.search(text.substring(start));
			} else {
				index = BruteForceMatch.search1(pat, text.substring(start));
			}
			if (start + index < N) {
				indices.add(index+start);
			}
			
			start += index + 1;
		}
		return indices.stream().mapToInt(i -> i).toArray();
	}
	
	
	
	public static void main(String[] args) throws IOException {
		final int TIMES = 100;
		final String sourceDataFile = "resources/Hard disk.txt";
		String[] patterns = {"hard", "disk", "hard disk", "hard drive", "hard dist", "xltpru"};

		String fileString = readFile(sourceDataFile);
		
		System.out.println("\nIndices found by BoyerMoore:");
		for (String pat:patterns) {
			int[] indices = searchPat(pat, fileString, 1);
			System.out.println(pat + ": " + Arrays.toString(indices));
		}
		
		System.out.println("\nIndices found by KMP:");
		for (String pat:patterns) {
			int[] indices = searchPat(pat, fileString, 2);
			System.out.println(pat + ": " + Arrays.toString(indices));
		}
		
		System.out.println("\nIndices found by BruteForceMatch:");
		for (String pat:patterns) {
			int[] indices = searchPat(pat, fileString, 3);
			System.out.println(pat + ": " + Arrays.toString(indices));
		}
		
		
		/*
		 * performance test by running for times;
		 */
		System.out.println("\nSearching patterns for " + TIMES + " times with each algorithms, average CPU time:");
		long start = System.nanoTime();
		for (int i=0; i<TIMES; i++) {
			for (String pat:patterns) {
				searchPat(pat, fileString, 1);
			}
		}
		long end = System.nanoTime();
		System.out.println("BoyerMoore: " + (end -start)/TIMES + " ns");
		
		start = System.nanoTime();
		for (int i=0; i<TIMES; i++) {
			for (String pat:patterns) {
				searchPat(pat, fileString, 2);
			}
		}
		end = System.nanoTime();
		System.out.println("KMP: " + (end -start)/TIMES + " ns");
		
		start = System.nanoTime();
		for (int i=0; i<TIMES; i++) {
			for (String pat:patterns) {
				searchPat(pat, fileString, 3);
			}
		}
		end = System.nanoTime();
		System.out.println("BruteForceMatch: " + (end -start)/TIMES + " ns");
	}

}
