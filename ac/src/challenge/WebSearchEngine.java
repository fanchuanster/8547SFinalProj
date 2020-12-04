/**
 * 
 */
package challenge;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import heaps.BinaryHeap;
import java.io.File;
import java.util.Scanner;
import java.util.StringTokenizer;
/**
 * Given a set of keywords as input, it ranks the web pages based on their score (score = sum of matches per keyword, or keyword * frequency). 
 * The program should list the top 10 pages with the best matches
 * @author donwen
 *
 */
public class WebSearchEngine {
	public static final String INPUT_PAGES_DIR = "resources\\W3C Web Pages\\Text\\";
	
	public static int GetFileMatchScore(String filePath, String[] keywords) {
		Hashtable<String, Integer> hashtable = WordsCounter.CountWords(filePath);
		int score = 0;
		for (String keyword:keywords) {
			if (hashtable.containsKey(keyword)) {
				score += hashtable.get(keyword);
			}
		}
		return score;
	}

	/**
	 * It answers Challenge.3
	 * @param args
	 */
	public static void main(String[] args) {
		
        System.out.println("Enter the keywords to search delimited by space:");

        Scanner sc = new Scanner(System.in);
        List<String> keywordsList = new ArrayList<String>();
        String inputLine = sc.nextLine();
        sc.close();
        StringTokenizer tokenizer = new StringTokenizer(inputLine);
        while (tokenizer.hasMoreTokens()) {
        	keywordsList.add(tokenizer.nextToken());
        }
        String[] keywords = new String[keywordsList.size()];
        keywordsList.toArray(keywords);
        
        System.out.println("Searching keywords:");
        for (String k:keywords) {
        	System.out.println(k);
        }        
		
		
		final boolean ASCENDING = false;
		final int LIST_TOP_N_SCORE = 10;

		BinaryHeap<IntKeyObject> heap = new BinaryHeap<IntKeyObject>();
		
		File dir = new File(INPUT_PAGES_DIR);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
		  for (File file : directoryListing) {
		  	int score = GetFileMatchScore(file.getAbsolutePath(), keywords);
		  	heap.insert(new IntKeyObject(score, file.getName(), ASCENDING));
		  }
		}
		
		System.out.println(String.format("-----------Top %d returned pages-----------:", LIST_TOP_N_SCORE));
		System.out.println("Score\tFile Name");
		
		IntKeyObject fs = null;
		int i = 0;
		while ((fs = heap.deleteMin()) != null && i++ < LIST_TOP_N_SCORE) {
			System.out.println(String.format("%d\t%s", fs.getKey(), fs.getContent()));
		}
	}
}
