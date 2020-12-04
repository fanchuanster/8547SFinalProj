package challenge;

import searchtrees.SplayTree;

import java.io.File;
import java.util.Hashtable;


public class WebDict {
	
	private Hashtable<String, Integer> wordsFrequencies = new Hashtable<String, Integer>();
	
	
	/**
	 * Add word frequencies to the web dictionary.
	 * @param newWordsFrequencies new word frequencies to add
	 */
	private void add(Hashtable<String, Integer> newWordsFrequencies) {
		newWordsFrequencies.forEach((key, val) -> {
			Integer newVal = wordsFrequencies.containsKey(key) ? wordsFrequencies.get(key) + val : val;
			wordsFrequencies.put(key, newVal);
		});
	}
	
	/**
	 * Get the words frequencies as a hash table.
	 * @return hash table containing the words frequencies.
	 */
	public Hashtable<String, Integer> getWordsFrequenciesAsHashtable() {
		return wordsFrequencies;
	}
	
	/**
	 * Get the words frequencies as a splay tree.
	 * @param ascending indicates whether to create the splay tree in ascending order.
	 * @return the created splay tree of the word frequencies.
	 */
	public SplayTree<IntKeyObject> getWordsFrequenciesAsSplayTree(boolean ascending) {
		SplayTree<IntKeyObject> sptree = new SplayTree<IntKeyObject>();
		wordsFrequencies.forEach((word, frequency) -> {
			sptree.insert(new IntKeyObject(frequency, word, ascending));
		});
		
		return sptree;
	}
	
	/**
	 * Build words frequencies from pages in given directory.
	 * @param the directory where the web pages are.
	 */
	public void buildDict(String pagesDir) {
		
		File dir = new File(pagesDir);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
		  for (File file : directoryListing) {
			  Hashtable<String, Integer> wordsFrequencies = WordsCounter.CountWords(file.getAbsolutePath());
			  add(wordsFrequencies);
		  }
		}
	}

	/**
	 * this is the main entry of Challenge.4 and Challenge.5
	 * @param args
	 */
	public static void main(String[] args) {
		
		final int TOP_LEAST_N = 10;
		final int TOP_MOST_N = 10;

		/*
		 * #4
		 */
		WebDict webDict = new WebDict();
		webDict.buildDict(WebSearchEngine.INPUT_PAGES_DIR);
		SplayTree<IntKeyObject> spTree = webDict.getWordsFrequenciesAsSplayTree(true);
		
		/*
		 * #5
		 */
		IntKeyObject[] wordsMostAndLeast = new IntKeyObject[TOP_MOST_N + TOP_LEAST_N];
		for (int i=0; i<TOP_MOST_N; i++) {
			if (spTree.isEmpty()) break;
			IntKeyObject wordMost = spTree.findMax();
			wordsMostAndLeast[i] = wordMost;
			spTree.remove(wordMost);
		}
		for (int i=0; i<TOP_LEAST_N; i++) {
			if (spTree.isEmpty()) break;
			IntKeyObject wordLeast = spTree.findMin();
			wordsMostAndLeast[TOP_MOST_N+TOP_LEAST_N-i-1] = wordLeast;
			spTree.remove(wordLeast);
		}
		System.out.println("Freq\tWord");
		for (int i=0; i<TOP_MOST_N + TOP_LEAST_N; i++) {
			if (wordsMostAndLeast[i] != null) {
				System.out.println(String.format("%d\t%s", wordsMostAndLeast[i].getKey(), wordsMostAndLeast[i].getContent()));
			}
		}
		System.out.println("The algorithm for Challenge.5 is efficient because it leverage SplayTree's findMax and findMin methods with a subsequent remove."
						  +"\nThe overall time complexity for each most/least element is O(log n)");
	}

}
