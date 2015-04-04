package me.zombieranke.ADS;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.csvreader.CsvReader;

public class Share
{
	private Date date[];
	private double open[];
	private double high[];
	private double low[];
	private double close[];
	private long volume[];
	private double adj_close[];
	
	/**
	 * Default Constructor for a Share
	 * 
	 * @param name The full name of the share
	 * @param shortName The short name of the share
	 * @param wkn The Wertpapierkennummer of the share
	 */
	public Share()
	{
		date = new Date[30];
		open = new double[30];
		high = new double[30];
		low = new double[30];
		close = new double[30];
		volume = new long[30];
		adj_close = new double[30];
	}
	
	public void importCsv(String src) throws IOException, ParseException
	{
		CsvReader reader = new CsvReader(src);
		reader.readHeaders();
		
		Calendar c = new GregorianCalendar();
		Calendar first = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		
		reader.readRecord();
		
		int i = 0;
		long differenceInMillis = 0;
		
		do
		{
			date[i] = df.parse(reader.get("Date"));
			open[i] = Double.parseDouble(reader.get("Open"));
			high[i] = Double.parseDouble(reader.get("High"));
			low[i] = Double.parseDouble(reader.get("Low"));
			close[i] = Double.parseDouble(reader.get("Close"));
			volume[i] = Long.parseLong(reader.get("Volume"));
			adj_close[i] = Double.parseDouble(reader.get("Adj Close"));
			c.setTime(date[i]);
			first.setTime(date[0]);
			differenceInMillis = first.getTimeInMillis() - c.getTimeInMillis();
			i++;
		}
		while(reader.readRecord() && differenceInMillis < 2.6e9);
	}
	
	public void plot()
	{
		int i = 0;
		int j = 0;
		double min = close[0]; // must not be 0 as it would be then smaller than every array entry
		double max = close[0]; // would not be necessary here, but for consistency
		int loopCount = 0;
		
		
		for(i=0;i<30;i++)
		{
			if(date[i] ==  null)
			{
				loopCount = i;
				break;
			}
			else
			{
				if(close[i]>max)
				{
					max = close[i];
				}
				else if(close[i] < min)
				{
					min = close[i];
				}
			}
		}
		
		
		double stepY = (max - min)/34;
		int stepX = 3;
		double matrixMin = min - 3*stepY;
		
		char[][] matrix = new char[40][94];
		for(i=0;i<40;i++)
		{
			for(j=0;j<94;j++)
			{
				matrix[i][j] = ' ';
			}
		}
		
		
		Date[] turnedAround = new Date[loopCount];
	
		for(i=0;i<loopCount;i++)
		{
			turnedAround[i] = date[loopCount - i];
		}
		
		Date lastDate = turnedAround[0];
	
		Calendar lastTime = new GregorianCalendar();
		lastTime.setTime(lastDate);
		
		Calendar temp = new GregorianCalendar();
		int daysSkipped = 0;
		int daysSkippedTotal = 0;
		
		int x = 0;
		int y = 0;
		
		for(i=0;i<loopCount;i++)
		{
			temp.setTime(turnedAround[i]);
			
			if(temp.getTimeInMillis() <= lastTime.getTimeInMillis() + 86400000 * (1+daysSkipped))
			{
				x = 2 + stepX*i + stepX*daysSkippedTotal;
				y = (int) Math.round((close[i] - matrixMin)/stepY);
				matrix[y][x] = 'X';
				
				lastTime = temp;
				daysSkipped = 0;
			}
			else
			{
				daysSkipped++;
				daysSkippedTotal++;
				i--;
			}
		}
		
	}
	
	public void print()
	{
		int i;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		
		for(i = 0;i<date.length;i++)
		{
			if(date[i] ==  null)
			{
				if(i==0)
				{
					System.out.println("Share is empty! Please import data.");
				}
				break;
			}
			System.out.println("Date: " + df.format(date[i]) + ", Open: " + open[i] + ", High: " + high[i] + ", Low: " + low[i] + ", Close: " + close[i] + ", Volume: " + volume[i] + ", Adj Close: " + adj_close[i]);
		}
	}
	
	public void printLatest()
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
		
		if(date[0] !=  null)
		{
			System.out.println("Date: " + df.format(date[0]) + ", Open: " + open[0] + ", High: " + high[0] + ", Low: " + low[0] + ", Close: " + close[0] + ", Volume: " + volume[0] + ", Adj Close: " + adj_close[0]);

		}
		else
		{
			System.out.println("Share is empty! Please import data.");
		}
	}
}
