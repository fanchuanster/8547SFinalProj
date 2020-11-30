package ac;

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

public class WebCrawler {

	String initialUrl;
	Proxy proxy;
	int depth;
	HashSet<String> links;
	HashSet<String> invalidLinks;
	final String outputDir = "output/pages/";
	
	WebCrawler(String url, Proxy proxy) {
		this.initialUrl = url;
		this.proxy = proxy;
		
		this.links = new HashSet<String>();
		this.invalidLinks = new HashSet<String>();
		
		File directory = new File(this.outputDir);
	    if (! directory.exists()){
	        directory.mkdirs();
	    }
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
	
	private int processPage(String url, int depth) {
		if (this.links.contains(url) || this.invalidLinks.contains(url)) {
			return 0;
		}
		
		int pageProcessed = 0;
		try {
            Document document = Jsoup.connect(url)
					  .proxy(this.proxy)
					  .get();
            links.add(url);
            
            String filename = String.format("%s.%d.txt", document.title().replaceAll("\s", ""), System.nanoTime());
            FileWriter fw = new FileWriter(outputDir + filename);
            fw.write(document.body().text());
            fw.close();
            System.out.println( "downloaded " + filename );
            
            pageProcessed++;
            
            Elements pageLinks = document.select("a[href]");

            depth++;
            for (Element link : pageLinks) {
            	pageProcessed += processPage(link.attr("abs:href"), depth);
            }
        } catch (IOException e) {
            System.err.println("For '" + url + "': " + e.getMessage());
            this.invalidLinks.add(url);
        }
		return pageProcessed;
	}
	
	public String getPagesDir() {
		return this.outputDir;
	}
		
	public int downloadPages() {
		return this.processPage(this.initialUrl, 0);
	}
	
}
