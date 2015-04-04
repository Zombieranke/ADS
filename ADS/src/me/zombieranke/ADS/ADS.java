package me.zombieranke.ADS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;

public class ADS {

	public static void main(String[] args) throws IOException, ParseException
	{
		Scanner sc = new Scanner(System.in);
		while(menu(sc));
		sc.close();
	}
	
	private static boolean menu(Scanner sc)
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
		
		Share toAdd = new Share(name,shortName,wkn);
		
		//Implement adding to Hashtable here
		
		
	}
	
	private static void del(Scanner sc)
	{
		
	}
	
	private static void importData(Scanner sc)
	{
		
	}
	
	private static void search(Scanner sc)
	{
		
	}
	
	private static void plot(Scanner sc)
	{
		
	}
	
	private static void save(Scanner sc)
	{
		
	}
	
	private static void load(Scanner sc)
	{
		
	}
}
