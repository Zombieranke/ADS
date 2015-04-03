package me.zombieranke.ADS;

import java.io.FileNotFoundException;
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
	private String name;
	private String kuerzel;
	private String wkn;
	
	private Date date[];
	private double open[];
	private double high[];
	private double low[];
	private double close[];
	private long volume[];
	private double adj_close[];
	
	public Share(String name, String kuerzel, String wkn)
	{
		this.name = name;
		this.kuerzel = kuerzel;
		this.wkn = wkn;
		
		date = new Date[30];
		open = new double[30];
		high = new double[30];
		low = new double[30];
		close = new double[30];
		volume = new long[30];
		adj_close = new double[30];
	}
	
	void importCsv(String src) throws IOException, ParseException
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
		}
		while(reader.readRecord() && differenceInMillis < 2.6e9);

	}
	
	
	
}
