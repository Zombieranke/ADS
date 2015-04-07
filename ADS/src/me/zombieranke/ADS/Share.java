package me.zombieranke.ADS;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
	 */
	public void importCsv(String src)
	{
		CsvReader reader;
		
		try
		{
			reader = new CsvReader(src);
		}
		catch(IOException ioe)
		{
			if(ioe instanceof FileNotFoundException)
			{
				System.out.println("Could not find file! Please check specified path.");
			}
			else
			{
				ioe.printStackTrace();
			}
			return;
		}
		try
		{
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
		catch(ParseException pe)
		{
			pe.printStackTrace();
		} 
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
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
				loopCount = i; //How many entries do all the arrays have? loopCount -2
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
		
		
		double stepY = (max - min)/36;  //The difference of values between [y][x] and [y+1][x]
		int stepX = 10;		//Number of Columns until the next day is recorded in the matrix
		double matrixMin = min - 2*stepY;  //Centering along the y-axis
		int matrixWidth = 32*stepX + 6;	//The last 30 days and 4 Columns for space to the left border
		int matrixHeight = 40;
		
		
		char[][] matrix = new char[matrixHeight][matrixWidth]; //initializing the matrix with space characters
		for(i=0;i<matrixHeight;i++)
		{
			for(j=0;j<matrixWidth;j++)
			{
				matrix[i][j] = ' ';
			}
		}
		
		
		Date[] turnedAroundDate = new Date[loopCount];	//turning around the arrays as zero would be latest date but it shall appear to the most right
		double[] turnedAroundClose = new double[loopCount];	//of course i have to turn around the close-array as well
	
		for(i=0;i<loopCount;i++)
		{
			turnedAroundDate[i] = date[loopCount - i - 1];
			turnedAroundClose[i] = close[loopCount - i - 1];
		}
		
		Date lastDate = turnedAroundDate[0];

		
		Calendar lastTime = new GregorianCalendar();	//Calendars are needed for the getTimeInMillis()-function as it is needed to calculate the difference between to dates
		lastTime.setTime(lastDate);
		
		Calendar temp = new GregorianCalendar();
		int daysSkipped = 0;
		int daysSkippedTotal = 0;
		
		long tempForTimeInMillis = 0;
		int x = 0;		// where exactly should the calculated entry should be located
		int y = 0;		// this one is rounded in the loop
		double deltaX = 0;	//time difference between the current and the last date
		double deltaY = 0;	//value difference between the current value and the last value
		
		
		for(i=0;i<loopCount;i++)
		{
			temp.setTime(turnedAroundDate[i]);
			
			if(i>0)	//not possible for the first loop as otherwise turnedAroundDate[i-1] would throw an error
			{
				lastTime.setTime(turnedAroundDate[i-1]);
				tempForTimeInMillis = temp.getTimeInMillis() - lastTime.getTimeInMillis(); //difference of the dates in milliseconds
				deltaX = (double)(tempForTimeInMillis/86400000);	//difference of the dates in days
				deltaY = turnedAroundClose[i] - turnedAroundClose[i-1]; //difference of the values
				
				for(j=0;j<stepX;j++)	//This loop shall put dots '.' between the course values
				{
					if(daysSkipped == 0 && j==0) //if the last day was not skipped the loop starts from j=2 instead of zero as we do not want to overwrite the [X] in the matrix
					{
						j = 2;
						if(!(j<stepX))
						{
							break;
						}
					}
					x = 4 + stepX*i + stepX*daysSkippedTotal + j - stepX;	// 4 for border gap, stepX*i + stepX*daysSkippedTotal for the amount of days, + j - stepX for accessing the columns between two days in the matrix
					y = matrixHeight - (int) Math.round( ( ( (deltaY/deltaX)*daysSkipped) + (deltaY/(deltaX*stepX))*j  + turnedAroundClose[i-1] - matrixMin)/stepY);
					//Ok... (deltaY/deltaX)*daysSkipped) saves the position if the dates are not directly subsequent - more or less they increase the 'd' in y = k*x + d
					//  (deltaY/(deltaX*stepX))*j is the same as the 'k*x'  in y = k*x + d
					// + turnedAroundClose[i-1] is the base where I have to start with my line
					// - matrixMin and /stepY to bring the whole expression in a form for the matrix as it does not start from zero but for example from 132.42
					// matrixHeight - (expression so far explained) for mirroring as the lowest values would be shown on the top of matrix which would make no sense
					// I hope this explanation was helpful
					
					
					matrix[y][x] = '.';
				}
			}
			
			
			if(temp.getTimeInMillis() <= lastTime.getTimeInMillis() + 86400000 * (1+daysSkipped)) //checks whether the dates are subsequent
			{																					  // if they are not daysSkipped is increased so the span is increased by one whole day
				x = 4 + stepX*i + stepX*daysSkippedTotal;  //placing on the right columns 
				y = matrixHeight - (int) Math.round((turnedAroundClose[i] - matrixMin)/stepY); // easy if you understood the formula above

				matrix[y][x] = 'X';
				if(stepX>=3)
				{
					matrix[y][x-1] = '[';
					matrix[y][x+1] = ']';
				}
				
				//lastTime.setTime(turnedAroundDate[i]); 
				daysSkipped = 0; //resetting the daysSkipped
			}
			else
			{
				daysSkipped++;
				daysSkippedTotal++; //every single skipped day must be saved in order to get the positions on the x-axis right
				i--;	//the i must be lowered as we want to picture the dates over 30 days and otherwise the loop would be too short
			}
		}
		
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		DateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.ENGLISH);
		
		for(i=0;i<matrixHeight;i++)
		{
			//System.out.print(decimalFormat.format(matrixMin+(40-i)*stepY));
			
			for(j=0;j<matrixWidth;j++)
			{
				System.out.print(matrix[i][j]);
			}
			System.out.print("\n");
		}
		
		
	/*	Date tempDate = turnedAroundDate[0];
		
		Calendar tempConstant = new GregorianCalendar();
		
		tempConstant.setTime(tempDate);
		System.out.print("  ");
		for(i=1;i<30;i++)
		{
			temp.setTimeInMillis(tempConstant.getTimeInMillis()+(long)i* (long)86400000);
			tempDate = temp.getTime();
			System.out.print(dateFormat.format(tempDate)+" ");
		}
		*/
		
		
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
