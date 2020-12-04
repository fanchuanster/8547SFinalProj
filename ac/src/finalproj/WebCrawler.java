package finalproj;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashSet;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.net.MalformedURLException;
import java.net.URL;

public class WebCrawler {

	String initialUrl;
	int depth;
	int maximum;
	Proxy proxy;
	int pagesProcessed;
	
	HashSet<String> links;
	HashSet<String> invalidLinks;
	final String outputDir = "output/pages/";
	
	WebCrawler(String url, int depth, int maximum, Proxy proxy) {
		this.initialUrl = url;
		this.proxy = proxy;
		this.depth = depth;
		this.maximum = maximum;
		
		this.pagesProcessed = 0;
		
		this.links = new HashSet<String>();
		this.invalidLinks = new HashSet<String>();
		
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
	
	public void trustEveryone() {
	    try {
	        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
	            @Override
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        });
	        SSLContext context = SSLContext.getInstance("TLS");
	        context.init(null, new X509TrustManager[] { new X509TrustManager() {
	            @Override
	            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }
	            @Override
	            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }
	            @Override
	            public X509Certificate[] getAcceptedIssuers() {
	                return new X509Certificate[0];
	            }
	        } }, new SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	    } catch (Exception e) {
	         e.printStackTrace();
	    }
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
		
		try {
            Document document = Jsoup.connect(url)
					  .proxy(this.proxy)
					  .get();
            links.add(url);
            
            String filename = String.format("%s.%d.txt", document.title().replaceAll("[\s,|]+", ""), System.nanoTime());
            FileWriter fw = new FileWriter(outputDir + filename);
            fw.write(document.body().text());
            fw.close();
            System.out.println( "downloaded " + filename + " from " + url);
            
            this.pagesProcessed++;
            depth++;
            
            if (this.depth != 0 && depth > this.depth) {
            	return;
            }
            if (this.maximum != 0 && this.pagesProcessed > this.maximum) {
            	return;
            }
            	
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
		
	public int downloadPages() {
		this.processPage(this.initialUrl, 0);
		return this.pagesProcessed;
	}
	
}
