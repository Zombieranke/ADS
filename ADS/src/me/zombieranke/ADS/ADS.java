package me.zombieranke.ADS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;

public class ADS {
	
	static MyHashtable nameTable;
	static MyHashtable shortNameTable;
	
	private enum Mode{NAME,SHORT_NAME}; 

	public static void main(String[] args) throws IOException, ParseException
	{
		Scanner sc = new Scanner(System.in);
		while(menu(sc));
		sc.close();
	}
	
	private static boolean menu(Scanner sc) throws IOException, ParseException
	{
		printMenu();
		String option = sc.nextLine();
		switch(option.toLowerCase(Locale.ROOT))
		{
		case "1": case "add":
			add(sc);
			return true;
			
		case "2": case "del":
			del(sc);
			return true;
			
		case "3": case "import":
			importData(sc);
			return true;
			
		case "4": case "search":
			search(sc);
			return true;
			
		case "5": case "plot":
			plot(sc);
			return true;
			
		case "6": case "save":
			save(sc);
			return true;
			
		case "7": case "load":
			load(sc);
			return true;
			
		case "8": case "exit":
			return false;
		
		default:
			return true;
		}
	}

	private static void printMenu()
	{
		System.out.println("1.ADD");
		System.out.println("2.DEL");
		System.out.println("3.IMPORT");
		System.out.println("4.SEARCH");
		System.out.println("5.PLOT");
		System.out.println("6.SAVE");
		System.out.println("7.LOAD");
		System.out.println("8.EXIT");
		
	}
	
	private static void add(Scanner sc)
	{
		System.out.println("Enter the full name of the share: ");
		String name = sc.nextLine();
		System.out.println("Enter the short name: ");
		String shortName = sc.nextLine();
		System.out.println("Enter WKN: ");
		String wkn = sc.nextLine();

		if(nameTable == null)
		{
			nameTable = new MyHashtable();
			
		}

		if(shortNameTable == null)
		{
			shortNameTable = new MyHashtable();
		}
		
		Entry toAdd = new Entry(name,shortName,wkn);
		
		nameTable.addEntry(toAdd,name);
		shortNameTable.addEntry(toAdd,shortName);
	}
	
	private static void del(Scanner sc)
	{
		Mode mode = evaluateMode(sc);
		System.out.println("What share would you like to delete?");
		String toDelete = sc.nextLine();
		if(mode == Mode.NAME)
		{
			nameTable.deleteEntry(toDelete);
		}
		else if(mode == Mode.SHORT_NAME)
		{
			shortNameTable.deleteEntry(toDelete);
		}
	}
	
	private static void importData(Scanner sc) throws IOException, ParseException
	{
		Mode mode = evaluateMode(sc);
		System.out.println("What share would you like to import?");
		String toImport = sc.nextLine();
		System.out.println("Please specify the exact Path to the csv file you want to import: ");
		String path = sc.nextLine();
		Share share = new Share();
		share.importCsv(path);
		if(mode == Mode.NAME)
		{
			nameTable.importShareData(share,toImport);
		}
		else if(mode == Mode.SHORT_NAME)
		{
			shortNameTable.importShareData(share, toImport);
		}
	}
	
	private static void search(Scanner sc)
	{
		Mode mode = evaluateMode(sc);
		System.out.println("Do want you want a full report(1) or only the most recent(2)?");
		String reportOption = sc.nextLine();
		System.out.println("What share would you like to search for?");
		String toSearch = sc.nextLine();
		if(mode == Mode.NAME)
		{
			if(reportOption.equalsIgnoreCase("full") || reportOption.equalsIgnoreCase("full report") || reportOption.equalsIgnoreCase("fullreport") || reportOption.equalsIgnoreCase("1"))
			{
				nameTable.printEntry(toSearch);
			}
			else if(reportOption.equalsIgnoreCase("recent") || reportOption.equalsIgnoreCase("recent report") || reportOption.equalsIgnoreCase("recentreport") || reportOption.equalsIgnoreCase("2"))
			{
				nameTable.printLatestEntry(toSearch);
			}
			else
			{
				System.out.println("Not a valid report option");
			}
		}
		else if(mode == Mode.SHORT_NAME)
		{
			if(reportOption.equalsIgnoreCase("full") || reportOption.equalsIgnoreCase("full report") || reportOption.equalsIgnoreCase("fullreport") || reportOption.equalsIgnoreCase("1"))
			{
				shortNameTable.printEntry(toSearch);
			}
			else if(reportOption.equalsIgnoreCase("recent") || reportOption.equalsIgnoreCase("recent report") || reportOption.equalsIgnoreCase("recentreport") || reportOption.equalsIgnoreCase("2"))
			{
				shortNameTable.printLatestEntry(toSearch);
			}
			else
			{
				System.out.println("Not a valid report option");
			}
		}
	}
	
	private static void plot(Scanner sc)
	{
		Mode mode = evaluateMode(sc);
		System.out.println("What share would you like to plot?");
		String toPlot = sc.nextLine();
		if(mode == Mode.NAME)
		{
			nameTable.plotEntry(toPlot);
		}
		else if(mode == Mode.SHORT_NAME)
		{
			shortNameTable.plotEntry(toPlot);
		}
	}
	
	private static void save(Scanner sc)
	{
		nameTable.save("nameTable");
	}
	
	private static void load(Scanner sc)
	{
		nameTable = MyHashtable.load("nameTable");
		shortNameTable = nameTable.createShortNameHashtable();
	}
	
	private static Mode evaluateMode(Scanner sc)
	{
		while(true)
		{
			System.out.println("Do want you want to search according to name(1) or to short name(2)?");
			String option = sc.nextLine();
			if(option.equalsIgnoreCase("name") || option.equalsIgnoreCase("1"))
			{
				return Mode.NAME;
			}
			else if(option.equalsIgnoreCase("short name") || option.equalsIgnoreCase("2") || option.equalsIgnoreCase("shortname"))
			{
				return Mode.SHORT_NAME;
			}
			else
			{
				System.out.println("Not a valid name option");
			}
		}
	}
}
