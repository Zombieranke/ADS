package me.zombieranke.ADS;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;



public class ADS {

	public static void main(String[] args) throws ParseException
	{
		String target = "2015-05-03";
		Calendar c = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date result = df.parse(target);
		c.setTime(result);
		System.out.println(c.get(Calendar.DAY_OF_MONTH) + ", " + c.get(Calendar.MONTH)+1);
	}

}
