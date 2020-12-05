package finalproj;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Proxy;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

public class WebCrawler {

	String outputDir = "output/pages/";
	
	String initialUrl;
	int depth;
	int maximum;
	Proxy proxy;
	
	HashSet<String> links;
	HashSet<String> invalidLinks;
	Hashtable<String, Hashtable<String, String>> filenameToUrls;
	Hashtable<String, String> filenameToUrlTmp;
	
	WebCrawler(int depth, int maximum, Proxy proxy) {
		
		this.proxy = proxy;
		this.depth = depth;
		this.maximum = maximum;
		
		
		this.links = new HashSet<String>();
		this.invalidLinks = new HashSet<String>();
		this.filenameToUrls = new Hashtable<String, Hashtable<String, String>>();
		this.filenameToUrlTmp = new Hashtable<String, String>();
		
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
	
	private boolean internalUrl(String url) {
		URL u = this.parseUrl(this.initialUrl);
		URL inUrl = this.parseUrl(url);
		if (inUrl == null) {
			return false;
		}
		return u.getHost().equals(inUrl.getHost());
	}
	private void processPage(String url, int depth) {
		if (this.links.contains(url) || this.invalidLinks.contains(url)) {
			return;
		}
		
		if (this.depth != 0 && depth >= this.depth) {
        	return;
        }
        if (this.maximum != 0 && this.filenameToUrlTmp.size() >= this.maximum) {
        	return;
        }
        		
		try {
            Document document = Jsoup.connect(url)
					  .proxy(this.proxy)
					  .get();
            links.add(url);
            
            String filename = String.format("%s.%d.txt", document.title().replaceAll("[\s,|]+", ""), System.nanoTime());
            FileWriter fw = new FileWriter(outputDir + filename);
            fw.write(document.body().text());
            fw.close();
            System.out.println((this.filenameToUrlTmp.size()+1)+"/"+(depth+1) + " downloaded " + filename + " from " + url);
            assert !this.filenameToUrlTmp.containsKey(filename) : filename + " already exists";
            this.filenameToUrlTmp.put(filename, url);
            
            depth++;            	
            Elements pageLinks = document.select("a[href]");
            for (Element link : pageLinks) {
            	String childUrl = link.attr("abs:href");
            	if (this.internalUrl(childUrl)) {
            		processPage(link.attr("abs:href"), depth);
            	}
            }
        } catch (IOException e) {
            System.err.println("For '" + url + "': " + e.getMessage());
            this.invalidLinks.add(url);
        }
	}
	
	public String getPagesDir() {
		return this.outputDir;
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
		
	public int downloadPages(String url) {
		this.initialUrl = url;
		this.processPage(url, 0);
		
		Set<String> keysets = this.filenameToUrlTmp.keySet();
		for (final String key:keysets) {
			this.filenameToUrl.put(key, this.filenameToUrlTmp.get(key));
		}
		
		int n = this.filenameToUrlTmp.size();
		this.filenameToUrlTmp.clear();
		return n;
	}
	
}
