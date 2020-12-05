package finalproj;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Proxy;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

public class WebCrawler {

	final String outputDir = "output/pages/";
	
	int maxDepth;
	int maximum;
	Proxy proxy;
	
	HashSet<String> links;
	HashSet<String> invalidLinks;
	Hashtable<String, Hashtable<String, String>> filenameToUrls;
	
	WebCrawler(int depth, int maximum, Proxy proxy) {
		
		this.proxy = proxy;
		this.maxDepth = depth;
		this.maximum = maximum;
		
		
		this.links = new HashSet<String>();
		this.invalidLinks = new HashSet<String>();
		this.filenameToUrls = new Hashtable<String, Hashtable<String, String>>();
		
		/*
		 * prepare an empty output folder for downloading.
		 */
		File directory = new File(this.outputDir);
	    if (! directory.exists()){
	        directory.mkdirs();
	    }
	}
	
	private URL parseUrl(String url) {
        try {
            URL u = new URL(url);
            return u;
        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        }
        return null;
	}
	
	private boolean internalUrl(String initialUrl, String url) {
		URL u = this.parseUrl(initialUrl);
		URL inUrl = this.parseUrl(url);
		if (inUrl == null) {
			return false;
		}
		return u.getHost().equals(inUrl.getHost());
	}
	private void processPage(String initialUrl, String url, int depth, Hashtable<String, String> filenameToUrlTmp) {
		if (this.links.contains(url) || this.invalidLinks.contains(url)) {
			return;
		}
		
		if (this.maxDepth != 0 && depth >= this.maxDepth) {
        	return;
        }
        if (this.maximum != 0 && filenameToUrlTmp.size() >= this.maximum) {
        	return;
        }
        		
		try {
            Document document = Jsoup.connect(url)
					  .proxy(this.proxy)
					  .get();
            links.add(url);
            
            String filename = outputDir + String.format("%s.%d.txt", document.title().replaceAll("[\s,|]+", ""), System.nanoTime());
            FileWriter fw = new FileWriter(filename);
            fw.write(document.body().text());
            fw.close();
            System.out.println((filenameToUrlTmp.size()+1)+"/"+(depth+1) + " downloaded " + filename + " from " + url);
            assert !filenameToUrlTmp.containsKey(filename) : filename + " already exists";
            filenameToUrlTmp.put(filename, url);
            
            depth++;            	
            Elements pageLinks = document.select("a[href]");
            for (Element link : pageLinks) {
            	String childUrl = link.attr("abs:href");
            	if (this.internalUrl(initialUrl, childUrl)) {
            		processPage(initialUrl, link.attr("abs:href"), depth, filenameToUrlTmp);
            	}
            }
        } catch (IOException e) {
            System.err.println("For '" + url + "': " + e.getMessage());
            this.invalidLinks.add(url);
        }
	}
	
	public String filenameToUrl(String initialUrl, String filename) {
		if (this.filenameToUrls.containsKey(initialUrl)) {
			Hashtable<String, String> filenameToUrl = this.filenameToUrls.get(initialUrl);
			if (filenameToUrl.containsKey(filename)) {
				return filenameToUrl.get(filename);
			}
		}
		return null;
	}
		
	public Enumeration<String> downloadPages(String initialUrl) {
		if (this.filenameToUrls.containsKey(initialUrl)) {
			return this.filenameToUrls.get(initialUrl).keys();
		}
		Hashtable<String, String> filenameToUrlTmp = new Hashtable<String, String>();
		
		this.processPage(initialUrl, initialUrl, 0, filenameToUrlTmp);	
		this.filenameToUrls.put(initialUrl, filenameToUrlTmp);
		
		return filenameToUrlTmp.keys();
	}
	
}
