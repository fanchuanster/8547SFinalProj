package finalproj;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class App {

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
                tops.stream().map(s -> String.format("%d times\t%s\n", s.occurrences, s.pageUrl)).forEach(System.out::print);
        	}
        }
        
//        System.out.println( "Hello! Please input a string to search for" );
//        String word = sc.nextLine();	
    }

}
