package webcrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MyWebCrawler extends Thread 
{
	private WebCrawler webCrawler;
	private FileWriterClass myFileWriter;
	

	public WebCrawler getWebCrawler()
	{
		return webCrawler;
	}

	public void setWebCrawler(WebCrawler webCrawler)
	{
		this.webCrawler = webCrawler;
	}

	public void setMyFileWriter(String fileName)
	{
		myFileWriter = new FileWriterClass();
		myFileWriter.setFileWriter(fileName);
	}
	
	public FileWriterClass getMyFileWriter()
	{
		return this.myFileWriter;
	}
	
	public void crawl(String url)
	{
		setWebCrawler(new WebCrawler());
		setMyFileWriter("crwaleroutput.txt");
		try 
		{
			getWebCrawler().initiliaseWebCrawler(new URL(url));
			getWebCrawler().getWebsites().add(new URL(url));
			this.start();
		} 
		catch (MalformedURLException e)
		{		
			//e.printStackTrace();
		}
		
		
	}
	
	public void run()
	{
		
		while(!(getWebCrawler().getWebsites().isEmpty()) && getWebCrawler().getCount()<getWebCrawler().searchLimit)
		{
			URL url = getWebCrawler().getWebsites().pollFirst();
			//System.out.println(getWebCrawler().getCount()+" : "+url.toString());
			if(getWebCrawler().isKnownURL(url))
				continue;
			
			else
			{
				if(getWebCrawler().allowedToCrawl(url))
				{
					crawlURL(url);
				}

				try 
				{
					Thread.sleep(5000);
				} 
				catch (InterruptedException e)
				{		
					//e.printStackTrace();
				}	
			}
		}
	}

	
	private void crawlURL(URL url)
	{
		if(getWebCrawler().isPDF(url))
		{
			getWebCrawler().incrementCount();
			getWebCrawler().getKnownURLs().add(url);
			try 
			{
				getMyFileWriter().getOutBuff().write((getMyFileWriter().getCurrentTime()+" : "+url.toString()));
				getMyFileWriter().getOutBuff().newLine();
				getMyFileWriter().getOutBuff().flush();
			}
			catch (IOException e) 
			{
				//e.printStackTrace();
			}
			return;
		}
		else
		{
			//getWebCrawler().getKnownURLs().add(url);
			if(getWebCrawler().parseAndRecordURL(url))
			{
				getWebCrawler().incrementCount();
				getWebCrawler().getKnownURLs().add(url);
				try 
				{
					getMyFileWriter().getOutBuff().write(getMyFileWriter().getCurrentTime()+" : "+url.toString());
					getMyFileWriter().getOutBuff().newLine();
					getMyFileWriter().getOutBuff().flush();
				}
				catch (IOException e) 
				{
					//e.printStackTrace();
				}
			}
			
			return;
		}
		
		
		
		
	}
}
