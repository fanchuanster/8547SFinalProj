package finalproj;

import java.net.InetSocketAddress;
import java.net.Proxy;

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
	 
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        WebCrawler crawler = new WebCrawler("https://www.newsmax.com/", 3, 50,
        		new Proxy(Proxy.Type.HTTP, new InetSocketAddress("web-proxy.il.softwaregrp.net", 8080)));
//        crawler.trustEveryone();
        
        int count = crawler.downloadPages();
        
        System.out.println(String.format("%d pages downloaded,find them in %s", count, crawler.getPagesDir()));
    }
}
