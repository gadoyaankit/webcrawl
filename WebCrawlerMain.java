package webcrawler;

public class WebCrawlerMain {

	public static void main(String[] args) {
		MyWebCrawler myWebCrawler = new MyWebCrawler();		
		FileWriterClass fw = new FileWriterClass();		
		String seedURL = "http://www.ccs.neu.edu/";
		myWebCrawler.crawl(seedURL);
		
	}

}
