package finalproj;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import challenge.IntKeyObject;
import sorting.*;

import common.Util;
import heaps.BinaryHeap;

/*
 * finding patterns using regular expressions, translation of HTML to text,
 * ranking web pages using sorting, heaps or other data structures,
 * finding keywords using string matching, use of inverted index, analyzing frequencies using hash tables or search trees,
 * using large dictionaries/datasets, sorting techniques, search trees, spellchecking keywords or HTML files, and many others.  
 */


public class WebSearchEngine {
	
	final int MaxPages = 5;
	final int Depth = 3;
	
	/**
	 * Search given keyword in the file
	 * @param pattern the pattern to search for
	 * @param file the file to search
	 * @return occurrences number of the keyword in the file.
	 */
	int searchFile(Pattern pattern, File file) {
		
		String content = Util.readFile(file);
	    Matcher matcher = pattern.matcher(content);
	    int occurrences = 0;
	    while (matcher.find()) {
	    	occurrences++;
	    }
	    return occurrences;
	}
	
	List<SearchResult> getTopResults (List<SearchResult> results, int topN) {
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
	 * search for keyword in page and descendant pages of a given original URL.
	 * @param keyword the keyword to search for.
	 * @param url the original URL to start searching.
	 * @param topN indicates how many top ranked results for the keyword to return
	 * @return return top ranked results for the keyword
	 */

	public List<SearchResult> search(String keyword, String url, int topN) {
		WebCrawler crawler = new WebCrawler(url, Depth, MaxPages,
        		new Proxy(Proxy.Type.HTTP, new InetSocketAddress("web-proxy.il.softwaregrp.net", 8080)));
		int count = crawler.downloadPages();
		
//		System.out.println(String.format("%d pages downloaded spreading from %s", count, url));
		
		Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
		
		File pagesDir = new File(crawler.getPagesDir());
		List<SearchResult> results = new ArrayList<SearchResult>();
		for (final File file : pagesDir.listFiles()) {
			int occurrences = searchFile(pattern, file);
			String pageUrl = crawler.filenameToUrl(file.getName());
			assert(pageUrl != null) : "not exists " + file.getName();
			SearchResult res = new SearchResult(keyword, occurrences, pageUrl);
			results.add(res);
		}
		
		return getTopResults(results, topN);
	}
	
	public static void main(String[] args) {
		
//    	Scanner sc = new Scanner(System.in);
//        System.out.println( "Hello! Please input a link to search" );
//        String url = sc.nextLine();
        String url = "https://www.newsmax.com/";
        String keyword = "newsmax";
        WebSearchEngine engine = new WebSearchEngine();
        List<SearchResult> tops = engine.search(keyword, url, 10);

        System.out.println(String.format("\n===================search result of '%s'====================", keyword));
        tops.stream().map(s -> String.format("%d\t%s\n", s.occurrences, s.pageUrl)).forEach(System.out::print);
        
//        System.out.println( "Hello! Please input a string to search for" );
//        String word = sc.nextLine();
	}
}
