package webcrawler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.sourceforge.jrobotx.RobotExclusion;

public class WebCrawler {
	private LinkedList<URL> websites;
	private LinkedHashSet<URL> knownURLs;
	private LinkedHashSet<URL> uniqueURLs;
	private int count = 0;
	public  final int searchLimit = 100;
	private URL seedURL;
	
	public void initiliaseWebCrawler(URL seedURL)
	{
		setWebsites(new LinkedList<URL>());
		setKnownURLs(new LinkedHashSet<URL>());
		setUniqueURLs(new LinkedHashSet<URL>());
		setSeedURL(seedURL);
	}
	

	public URL getSeedURL() {
		return seedURL;
	}

	public void setSeedURL(URL seedURL) {
		this.seedURL = seedURL;
	}

	public LinkedList<URL> getWebsites() {
		return websites;
	}

	public void setWebsites(LinkedList<URL> websites) {
		this.websites = websites;
	}

	public LinkedHashSet<URL> getKnownURLs() {
		return knownURLs;
	}

	public void setKnownURLs(LinkedHashSet<URL> crwaledURLs) {
		this.knownURLs = crwaledURLs;
	}

	public LinkedHashSet<URL> getUniqueURLs() {
		return uniqueURLs;
	}

	public void setUniqueURLs(LinkedHashSet<URL> uniqueURLs) {
		this.uniqueURLs = uniqueURLs;
	}

	public int getCount() {
		return this.count;
	}

	public  void setCount(int count) {
		this.count = count;
	}
	
	public boolean allowedToCrawl(URL url)
	{
		String userAgentString = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)";
		RobotExclusion adhereRobot = new RobotExclusion();
		return adhereRobot.allows(url, userAgentString);
	}
	
	public boolean isKnownURL(URL url)
	{
		return getKnownURLs().contains(url);
	}
	
	public boolean isMaximumSearchLimit()
	{
		return searchLimit==getCount();
	}
	
	public boolean isWithinDomain(URL url)
	{
		return url.toString().startsWith(getSeedURL().toString());
	}
	
	public boolean parseAndRecordURL(URL url)
	{
		try 
		{
			Connection connection = Jsoup.connect(url.toString()).userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			Document doc = connection.get();
			Elements links = doc.select("a[href]");
			for (Element link : links) {				
				String linkRef = link.attr("abs:href");	
				if(isWithinDomain(new URL(linkRef)) && (!hasHashTagOrQuestion(linkRef)))
				{
					getWebsites().add(new URL(linkRef));					
				}		
			}
			if(conditionForRecord(url.toString()))
			{
				getUniqueURLs().add(new URL(url.toString()));
				return true;
			}
			else
				return false;
		
			
		
		} 
		catch (IOException e)
		{
			//e.printStackTrace();
			return false;
		}
	}

	private boolean conditionForRecord(String linkRef) throws IOException {
		
		return (isHtmlOrPdf(linkRef) && isUniqueURLs(new URL(linkRef)) && isWithinDomain(new URL(linkRef)));
	}

	private boolean hasHashTagOrQuestion(String linkRef) {
		
	 return (linkRef.contains("#")  || linkRef.contains("?") );
	}


	private boolean isUniqueURLs(URL url) {
		if(getKnownURLs().contains(url)
                || getUniqueURLs().contains(url))
			return false;
		else
			return true;
	}

	private boolean isHtmlOrPdf(String linkRef) throws IOException {
		return (linkRef.endsWith("pdf") ||isHtml(linkRef));
			
	}
	
	private boolean isHtml(String linkRef) {
		URL url = null;
		try {
			url = new URL(linkRef);
		} catch (MalformedURLException e) {

		}
		HttpURLConnection urlc = null;
		try {
			urlc = (HttpURLConnection) url.openConnection();
		} catch (Exception e) {
			return false;
		}

		urlc.setAllowUserInteraction(false);
		urlc.setDoInput(true);
		urlc.setDoOutput(false);
		urlc.setUseCaches(true);

		try {
			urlc.setRequestMethod("HEAD");
		} catch (ProtocolException e) {

		}
		try {
			urlc.connect();
		} catch (IOException e) {
		}
		String mime = urlc.getContentType();
		if (mime != null) {
			if (mime.contains("text/html"))
				return true;
			else
				return false;
		} else
			return false;
	  
	}

	public void incrementCount()
	{		
		setCount(getCount() + 1);
	}
	
	public boolean isPDF(URL url) {
		return url.toString().endsWith("pdf");
	}

}
