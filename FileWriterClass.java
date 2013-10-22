package webcrawler;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class FileWriterClass {
	
    private  FileWriter fileWriter;
    private  BufferedWriter outBuff;
    DateFormat dateFormat;
    
    FileWriterClass()
    {
    	this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }
	
	
	public void setFileWriter(String fileName)
	{
		try 
		{
			this.fileWriter= new FileWriter(fileName);
			setOutBuff(getFileWriter());
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getCurrentTime()
	{
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	
	public FileWriter getFileWriter()
	{
		return this.fileWriter;
	}
	
	
	public BufferedWriter getOutBuff()
	{
		return this.outBuff;
	}
	
	public void setOutBuff(FileWriter fw)
	{
		this.outBuff = new BufferedWriter(fw);
	}
}
