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
	
	public static int promptInput() {
		
		initialUrl = null;
		keywords.clear();
		
		int action = 0;
		boolean end = false;
		while (!end) {
			System.out.println("Choose action - 0 quit; 1 do search; 2 do regex search");
						
			action = sc.nextInt();
			sc.nextLine();
			
			switch (action) {
			case 0:
				end = true;
				break;
			case 1:
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
				break;
			default:
				break;
			}
		}
		
		return action;
	}

	public static void main(String[] args) {
//		tosearches.put("https://www.newsmax.com/", new String[]{"newsmax", "will"});
//		tosearches.put("https://en.wikipedia.org/wiki/New_Tang_Dynasty_Television", new String[]{"television", "group"});
//		tosearches.put("https://www.newsmax.com/", new String[]{"win", "sidney"});
		final int SearchDepth = 3;
		final int SearchMaxPages = 5;
        WebSearchEngine engine = new WebSearchEngine(SearchDepth,SearchMaxPages);
        
        int action = 0;
        while ((action = promptInput()) != 0) {
        	Hashtable<String, List<String>> tosearches = new Hashtable<String, List<String>>();
    		tosearches.put(initialUrl, keywords);
        	Set<String> urls = tosearches.keySet();
            for (final String url:urls) {
            	List<String> keywords = tosearches.get(url);
            	for (final String keyword:keywords) {
            		List<SearchResult> tops;
            		if (action == 1) {
            			tops = engine.search(keyword, url, 3);
            		} else {
            			tops = engine.searchPattern(keyword, url, 3);
            		}                	

                    System.out.println(String.format("\n===================Search Result of '%s'====================", keyword));
                    tops.stream().map(s -> String.format("%d times\t%s\n", s.occurrences, s.pageUrl)).forEach(System.out::print);
            	}
            }
        }
        sc.close();
        System.out.println("ended.");
    }

}
