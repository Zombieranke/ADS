package me.zombieranke.ADS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;

/**Main class for the share management program
 * 
 * @author Christoph Majcen and Arthur Bouz
 *
 */
public class ADS {
	
	/**Hashtable indexing via share name*/
	static MyHashtable nameTable;
	
	/**Hashtable indexing via share short name*/
	static MyHashtable shortNameTable;
	
	/** Describes the serch mode*/
	private enum Mode{NAME,SHORT_NAME}; 

	/**Main function for the share management program
	 * 
	 * @param args Does nothing
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws IOException, ParseException
	{
		Scanner sc = new Scanner(System.in);
		while(menu(sc));
		sc.close();
	}
	
	/**The menu function for user interaction
	 * 
	 * @param sc The Scanner responsible for handling user input
	 * @return True if program should not be exited
	 * @throws IOException
	 * @throws ParseException
	 */
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

	/**Prints the menu points*/
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
	
	/**Adds a share to both hashtables
	 * 
	 * @param sc The Scanner responsible for handling user input
	 */
	private static void add(Scanner sc)
	{
		boolean worked = false;
		boolean worked2 = false;
		
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
		
		worked = nameTable.addEntry(toAdd,name);
		worked2 = shortNameTable.addEntry(toAdd,shortName);
		if(worked && worked2)
		{
			System.out.println("Entry added\n");
		}
		else
		{
			System.out.println("Entry already exists. If you want to update the data use the 'IMPORT' function\n");
		}
		
		
	}
	
	/**Deletes a share from both hashtables
	 * 
	 * @param sc The Scanner responsible for handling user input
	 */
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
	
	/**Imports price data for a share
	 * 
	 * @param sc The Scanner responsible for handling user input
	 */
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
	
	/**Searches for a share
	 * 
	 * @param sc The Scanner responsible for handling user input
	 */
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
	
	/**Plots a share
	 * 
	 * @param sc The Scanner responsible for handling user input
	 */
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
	
	/**Saves nameTable to disk(shortNameTable one is recreated at load)
	 * 
	 * @param sc The Scanner responsible for handling user input
	 */
	private static void save(Scanner sc)
	{
		if(nameTable == null)
		{
			System.out.println("There is nothing to save");
		}
		else
		{
			System.out.println("Specify the filename you want to save your data in: ");
			String fileName = sc.nextLine();
			nameTable = nameTable.createNameHashtable();
			nameTable.save(fileName);
		}
	}
	
	/** Loads nameTable from disk
	 * 
	 * @param sc The Scanner responsible for handling user input
	 */
	private static void load(Scanner sc)
	{
		System.out.println("Specify the filename you want to load your data from: ");
		String fileName = sc.nextLine();
		nameTable = MyHashtable.load(fileName);
		if(nameTable == null)
		{
			System.out.println("Loading failed!");
		}
		else
		{
		shortNameTable = nameTable.createShortNameHashtable();
		}
	}
	
	/**Evaluate if user wants t search according to name or short name
	 * 
	 * @param sc The Scanner responsible for handling user input
	 * @return The search mode
	 */
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
