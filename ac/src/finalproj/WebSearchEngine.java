package finalproj;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import challenge.IntKeyObject;
import sorting.*;
import textprocessing.TST;
import common.Util;
import heaps.BinaryHeap;

/*
 * finding patterns using regular expressions, translation of HTML to text,
 * ranking web pages using sorting, heaps or other data structures,
 * finding keywords using string matching, use of inverted index, analyzing frequencies using hash tables or search trees,
 * using large dictionaries/datasets, sorting techniques, search trees, spellchecking keywords or HTML files, and many others.  
 */


public class WebSearchEngine {
	
	int MaxPages = 5;
	int Depth = 3;
	
	Hashtable<String, TST<Integer>> fileWordsOccurrences;
	Hashtable<String, Hashtable<String, TST<Integer>>> dictBuiltFromUrls;
	WebCrawler crawler;
	
	public WebSearchEngine(int depth, int maxPages) {
		this.Depth = depth;
		this.MaxPages = maxPages;
		
		this.dictBuiltFromUrls = new Hashtable<String, Hashtable<String, TST<Integer>>>();
		this.crawler = new WebCrawler(Depth, MaxPages,
        		new Proxy(Proxy.Type.HTTP, new InetSocketAddress("web-proxy.il.softwaregrp.net", 8080)));
	}
	
	/**
	 * Count words frequencies in a given file, it answers Challenge.2
	 * @param filePath the file to count words in
	 * @return a hash table with each word as the key and the corresponding frequency as value.
	 */
	private TST<Integer> CountWords(File file) {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (IOException e) {
			System.err.println("Failed to open file " + file.getAbsolutePath());
			return null;
		}
		
		StreamTokenizer tokenizer = new StreamTokenizer(fr);
		tokenizer.lowerCaseMode(true);
		
		tokenizer.ordinaryChars(0, (int)'A' - 1);
		tokenizer.ordinaryChars((int)'Z' + 1, (int)'a' - 1);
		tokenizer.ordinaryChars((int)'z' + 1, 256);
		tokenizer.wordChars((int)'0', (int)'9');
		TST<Integer> tst = new TST<Integer>();
		try {
			int token = tokenizer.nextToken();
			while (token != StreamTokenizer.TT_EOF) {
				switch (token) {
				case StreamTokenizer.TT_WORD:
					String word = tokenizer.sval;
					int count = tst.contains(word) ? tst.get(word) : 0; 
					tst.put(word, count + 1);
					break;
				default:
					break;
				}
				token = tokenizer.nextToken();
			}
		} catch (IOException e) {
			System.err.println("Failed to nextToken " + file.getAbsolutePath());
			e.printStackTrace();
		}
		return tst;
	}


	private Hashtable<String, TST<Integer>> getDict(String url) {
		if (this.dictBuiltFromUrls.containsKey(url)) {
			return this.dictBuiltFromUrls.get(url);
		}
		
		Enumeration<String> pagesFileNames = crawler.downloadPages(url);
				
		Hashtable<String, TST<Integer>> fileWordsOccurrences = new Hashtable<String, TST<Integer>>();
		while (pagesFileNames.hasMoreElements()) {
			String filename = pagesFileNames.nextElement();
			File pageFile = new File(filename);
			TST<Integer> tst = CountWords(pageFile);
			fileWordsOccurrences.put(filename, tst);
		}
		this.dictBuiltFromUrls.put(url, fileWordsOccurrences);
		
		return fileWordsOccurrences;
	}
	
	public List<SearchResult> search(String keyword, String initialUrl, int topN) {
		
		List<SearchResult> results = new ArrayList<SearchResult>();
		
		Hashtable<String, TST<Integer>> dict = getDict(initialUrl);
		assert(dict != null);
		
		Set<String> filenames = dict.keySet();
		 
		for(String filename : filenames) {
			TST<Integer> tst = dict.get(filename);
			if (tst.contains(keyword)) {
				int occurrences = tst.get(keyword);
				results.add(new SearchResult(keyword, occurrences, crawler.filenameToUrl(initialUrl, filename)));
			}
        }
		
		return getTopResults(results, topN);
	}
	
	/**
	 * Search given keyword in the file
	 * @param pattern the pattern to search for
	 * @param file the file to search
	 * @return occurrences number of the keyword in the file.
	 */

	private int searchFile(Pattern pattern, File file) {
		
		String content = Util.readFile(file);
	    Matcher matcher = pattern.matcher(content);
	    int occurrences = 0;
	    while (matcher.find()) {
	    	occurrences++;
	    }
	    return occurrences;
	}
	
	private List<SearchResult> getTopResults (List<SearchResult> results, int topN) {
		BinaryHeap<SearchResult> heap = new BinaryHeap<SearchResult>();
		
		for (SearchResult result:results) {
			heap.insert(result);
		}
		
		int counter = 0;
		SearchResult sr = null;
		ArrayList<SearchResult> tops = new ArrayList<SearchResult>();
		while ((sr = heap.deleteMin()) != null && counter++ <= topN) {
			tops.add(sr);
		}
		return tops;
	}
	
	/**
	 * regex search page and descendant pages of a given original URL.
	 * it's a one-time search from scratch without building dictionary.
	 * @param keywordPattern the keyword pattern to search for.
	 * @param initialUrl the original URL to start searching.
	 * @param topN indicates how many top ranked results for the keyword to return
	 * @return return top ranked results for the keyword
	 */

	public List<SearchResult> searchPattern(String keywordPattern, String initialUrl, int topN) {
		Enumeration<String> filenames = crawler.downloadPages(initialUrl);
		
		Pattern pattern = Pattern.compile(keywordPattern, Pattern.CASE_INSENSITIVE);
		
		List<SearchResult> results = new ArrayList<SearchResult>();
		
		while (filenames.hasMoreElements()) {
			String filename = filenames.nextElement();
			int occurrences = searchFile(pattern, new File(filename));
			String pageUrl = crawler.filenameToUrl(initialUrl, filename);
			assert(pageUrl != null) : "not exists " + filename;
			SearchResult res = new SearchResult(keywordPattern, occurrences, pageUrl);
			results.add(res);
        }
		
		return getTopResults(results, topN);
	}
	
	public static void main(String[] args) {
		
//    	Scanner sc = new Scanner(System.in);
//      System.out.println( "Hello! Please input a link to search" );
//      String url = sc.nextLine();
		
		Hashtable<String, String[]> tosearches = new Hashtable<String, String[]>();
		tosearches.put("https://www.newsmax.com/", new String[]{"newsmax", "will"});
		tosearches.put("https://en.wikipedia.org/wiki/New_Tang_Dynasty_Television", new String[]{"television", "group"});
		tosearches.put("https://www.newsmax.com/", new String[]{"win", "sidney"});
		
        WebSearchEngine engine = new WebSearchEngine(3,5);
        
        Set<String> urls = tosearches.keySet();
        for (final String url:urls) {
        	String[] keywords = tosearches.get(url);
        	for (final String keyword:keywords) {
            	List<SearchResult> tops = engine.searchPattern(keyword, url, 3);

                System.out.println(String.format("\n===================Search Result of '%s'====================", keyword));
                tops.stream().map(s -> String.format("%d\t%s\n", s.occurrences, s.pageUrl)).forEach(System.out::print);
        	}
        }
        
//        System.out.println( "Hello! Please input a string to search for" );
//        String word = sc.nextLine();
	}
}
