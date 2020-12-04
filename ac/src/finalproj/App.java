package finalproj;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
	/*
	 * finding patterns using regular expressions, translation of HTML to text,
	 * ranking web pages using sorting, heaps or other data structures,
	 * finding keywords using string matching, use of inverted index, analyzing frequencies using hash tables or search trees,
	 * using large dictionaries/datasets, sorting techniques, search trees, spellchecking keywords or HTML files, and many others.  
	 */
	
	static int searchFile() {
		return 0;
	}
	 
    public static void main( String[] args )
    {
    	final String outputDir = "output/pages/";
    	
    	Scanner sc = new Scanner(System.in);
        System.out.println( "Hello! Please input a link to search" );
        String url = sc.nextLine();
        url = "https://www.newsmax.com/";
        
        WebCrawler crawler = new WebCrawler(url, 3, 50, outputDir,
        		new Proxy(Proxy.Type.HTTP, new InetSocketAddress("web-proxy.il.softwaregrp.net", 8080)));
//        crawler.trustEveryone();
        
        int count = crawler.downloadPages();
        
        System.out.println(String.format("%d pages downloaded,find them in %s", count, crawler.getPagesDir()));
        
        System.out.println( "Hello! Please input a string to search for" );
        String word = sc.nextLine();
    }
}
