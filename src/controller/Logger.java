package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

public class Logger {
	private File logFile;
	private PrintStream stream;
	public Logger() 
	{
		logFile = new File("log/delivery.log");
		try
		{
			logFile.createNewFile();
			stream = new PrintStream(logFile);
			Date now = new Date();
			stream.println(now.toString()+" : Application launched\r\n");
			stream.flush();
		}
		catch(IOException e)
		{
		}
	}
	
	public void write(String message)
	{
		Date now = new Date();
		stream.println(now.toString() + " : "+message);
		stream.flush();
	}
}
