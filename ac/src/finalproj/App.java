package finalproj;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class App {
	
	static String initialUrl;
	static List<String> keywords = new ArrayList<String>();
	static Scanner sc = new Scanner(System.in);
	
	public static boolean promptInput() {
		
		initialUrl = null;
		keywords.clear();
		
		
		boolean ret = false;
		boolean end = false;
		while (!end) {
			System.out.println("Choose action - 1 quit; 2 do search");
						
			int action = sc.nextInt();
			sc.nextLine();
			
			switch (action) {
			case 1:
				end = true;
				ret = false;
				break;
			case 2:
				System.out.println("input a URL to start with:\n");
				initialUrl = sc.nextLine();
				System.out.println("input keywords to search for, dilimited by space");
				keywords = new ArrayList<String>();
				String inputLine = sc.nextLine();
				StringTokenizer tokenizer = new StringTokenizer(inputLine);
		        while (tokenizer.hasMoreTokens()) {
		        	keywords.add(tokenizer.nextToken());
		        }
		        end = true;
		        ret = true;
				break;
			default:
				break;
			}
		}
		
		return ret;
	}

	public static void main(String[] args) {
//    	Scanner sc = new Scanner(System.in);
//      System.out.println( "Hello! Please input a link to search" );
//      String url = sc.nextLine();
//		
//		Hashtable<String, String[]> tosearches = new Hashtable<String, String[]>();
//		tosearches.put("https://www.newsmax.com/", new String[]{"newsmax", "will"});
//		tosearches.put("https://en.wikipedia.org/wiki/New_Tang_Dynasty_Television", new String[]{"television", "group"});
//		tosearches.put("https://www.newsmax.com/", new String[]{"win", "sidney"});
		
        WebSearchEngine engine = new WebSearchEngine(3,5);
        
        while (promptInput()) {
        	Hashtable<String, List<String>> tosearches = new Hashtable<String, List<String>>();
    		tosearches.put(initialUrl, keywords);
        	Set<String> urls = tosearches.keySet();
            for (final String url:urls) {
            	List<String> keywords = tosearches.get(url);
            	for (final String keyword:keywords) {
                	List<SearchResult> tops = engine.searchPattern(keyword, url, 3);

                    System.out.println(String.format("\n===================Search Result of '%s'====================", keyword));
                    tops.stream().map(s -> String.format("%d times\t%s\n", s.occurrences, s.pageUrl)).forEach(System.out::print);
            	}
            }
        }
        sc.close();
        System.out.println("ended.");
    }

}
