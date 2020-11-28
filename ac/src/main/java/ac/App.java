package ac;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
//        WebCrawler crawler = new WebCrawler("https://www.newsmax.com/");
        WebCrawler crawler = new WebCrawler("http://www.mkyong.com");
        crawler.trustEveryone();
        
        int count = crawler.downloadPages();
        
        System.out.println(String.format("%d pages downloaded,find them in %s", count, crawler.getPagesDir()));
    }
}
