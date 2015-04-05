package me.zombieranke.ADS;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.csvreader.CsvReader;

/**The implementation of prize data of a share
 * 
 * @author Christoph Majcen and Arthur Bouz
 *
 */
public class Share implements Serializable
{
	/**Automatically generated id to identify class at load*/
	private static final long serialVersionUID = 6619004848220809275L;
	
	/**Representing dates from which we have price data*/
	private Date date[];
	
	/**Opening price data for the share. Corresponds to a date with index of array*/
	private double open[];
	
	/**Highest price data for the share. Corresponds to a date with index of array*/
	private double high[];
	
	/**Lowest price data for the share. Corresponds to a date with index of array*/
	private double low[];
	
	/**Closing price data for the share. Corresponds to a date with index of array*/
	private double close[];
	
	/**Volume of the share. Corresponds to a date with index of array*/
	private long volume[];
	
	/**Adj Close? price data for the share. Corresponds to a date with index of array*/
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
	
	/**Import price data from csv
	 * 
	 * @param src The path to the soure file
	 * @throws IOException
	 * @throws ParseException
	 */
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
	
	/**Plot share*/
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
				loopCount = i - 1;
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
		int stepX = 10;
		double matrixMin = min - 3*stepY;
		int matrixWidth = 30*stepX+4;
		int matrixHeight = 40;
		
		
		char[][] matrix = new char[matrixHeight][matrixWidth];
		for(i=0;i<matrixHeight;i++)
		{
			for(j=0;j<matrixWidth;j++)
			{
				matrix[i][j] = ' ';
			}
		}
		
		
		Date[] turnedAroundDate = new Date[loopCount];
		double[] turnedAroundClose = new double[loopCount];
	
		for(i=0;i<loopCount;i++)
		{
			turnedAroundDate[i] = date[loopCount - i];
			turnedAroundClose[i] = close[loopCount - i];
		}
		
		Date lastDate = turnedAroundDate[0];

		
		Calendar lastTime = new GregorianCalendar();
		lastTime.setTime(lastDate);
		
		Calendar temp = new GregorianCalendar();
		int daysSkipped = 0;
		int daysSkippedTotal = 0;
		
		long tempForTimeInMillis = 0;
		int x = 0;
		int y = 0;
		double deltaX = 0;
		double deltaY = 0;
		
		
		for(i=0;i<loopCount-1;i++)
		{
			temp.setTime(turnedAroundDate[i]);
			
			if(i>0)
			{
				lastTime.setTime(turnedAroundDate[i-1]);
				tempForTimeInMillis = temp.getTimeInMillis() - lastTime.getTimeInMillis();
				deltaX = (double)(tempForTimeInMillis/86400000);
				deltaY = turnedAroundClose[i] - turnedAroundClose[i-1];
				
				for(j=0;j<stepX;j++)
				{
					if(!(daysSkipped > 0) && j==0)
					{
						j = 2;
					}
					x = 2 + stepX*i + stepX*daysSkippedTotal + j - stepX;
					y = matrixHeight - (int) Math.round( ( ( (deltaY/(deltaX*stepX))*j + (deltaY/deltaX)*(daysSkipped)) + turnedAroundClose[i-1] - matrixMin)/stepY);

					matrix[y][x] = '.';
				}
			}
			
			
			if(temp.getTimeInMillis() <= lastTime.getTimeInMillis() + 86400000 * (1+daysSkipped))
			{
				x = 2 + stepX*i + stepX*daysSkippedTotal;
				y = matrixHeight - (int) Math.round((turnedAroundClose[i] - matrixMin)/stepY);
				matrix[y][x-1] = '[';
				matrix[y][x] = 'X';
				matrix[y][x+1] = ']';
				
				lastTime.setTime(turnedAroundDate[i]);
				daysSkipped = 0;
			}
			else
			{
				daysSkipped++;
				daysSkippedTotal++;
				i--;
			}
		}
		
		for(i=0;i<matrixHeight;i++)
		{
			for(j=0;j<matrixWidth;j++)
			{
				System.out.print(matrix[i][j]);
			}
			System.out.print("\n");
		}
		
		
		
		
	}
	
	/**Print all price data*/
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
	
	/**Print most recent price data*/
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
