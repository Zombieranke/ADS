package me.zombieranke.ADS;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;





public class ADS {

	public static void main(String[] args) throws IOException, ParseException
	{
		Share s = new Share("Microsoft", "MSFT", "870747");
		Scanner sc = new Scanner(System.in);
		String pathToCsv = sc.nextLine();
		sc.close();
		s.importCsv(pathToCsv);
		s.print();
		
	}

}
