package me.zombieranke.ADS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**Implementation of a hash table holding entrys that represent a share
 * 
 * @author Arthur Bouz
 */
public class MyHashtable implements Serializable
{

	/**Automatically generated id to identify class at load*/
	private static final long serialVersionUID = -6414770308414785767L;
	
	/**The prime number used for the size of the hash table*/
	private static final int hashPrimeNumber = 4001;
	
	/**Size of the ASCII Code table. Used in the hash function to shift bytes*/
	private static final int alphabetLength = 128;
	
	/**Array holding our entrys*/
	private Entry[] table;
	
	/**Creates a new empty hashtable
	 * 
	 */
	public MyHashtable()
	{
		int i = 0;
		table = new Entry[hashPrimeNumber];
		
		for(i=0;i<hashPrimeNumber;i++)
		{
			table[i] = null;
		}
	}
	
	/**Internal function to hash strings
	 * 
	 * @param name  The string that will be hashed by this function
	 * @return Numeric hash (positive) or negative value if an error occured
	 */
	private static int hash(String name)
	{
		char[] nameChar = name.toCharArray();
		int length = name.length();
		int i = 0;
		
		if(nameChar.length>0)
		{
			int sum = nameChar[0];
			
			for(i=1;i<length-1;i++)
			{
				sum = (sum * alphabetLength + nameChar[i]) % hashPrimeNumber;
			}
			
			return sum;
		}
		else
		{
			System.out.println("Specified name was empty! Aborting");
			return -1;
		}
		
	}
	
	/**Adds an Entry to the hashtable
	 * 
	 * @param entry The Entry to add
	 * @param toHash The name that identifies the entry
	 * @return TRue if adding was successful, false if it failed
	 */
	public boolean addEntry(Entry entry,String toHash)
	{
		int i = 0;
		
		int hashValue = hash(toHash);
		
		if(hashValue == -1)
		{
			return false;
		}
		
		int found = searchEntry(toHash);
		
		if(found >= 0)
		{
			return false;
		}
		else
		{
			while(true)
			{
				i++;
				
				if(table[hashValue] == null)
				{
					table[hashValue] = entry;
					return true;
				}
				else if(table[hashValue].isDeleted())
				{
					table[hashValue] = entry;
					return true;
				}
				else
				{
					hashValue += i*i;
				}
			}
		}
		
		
		
	}
	
	/**Delete entries from the hashtable
	 * 
	 * @param name The identifying name for the entry to be deleted
	 * @return True if deletion was successful, false if it failed
	 */
	public boolean deleteEntry(String name)
	{
		int hashValue = searchEntry(name);
		
		if(hashValue == -3)
		{
			return false;
		}
		
		if(hashValue == -2)
		{
			System.out.println("Entry was not found");
			return false;
		}
		else if(hashValue == -1)
		{
			System.out.println("Entry was already deleted");
			return false;
		}
		else
		{
			table[hashValue].deleteShare();
			System.out.println("Entry is now deleted");
			return true;
		}
		
	}
	
	/**Add a share to an entry
	 * 
	 * @param share The share data to add
	 * @param toImport The Entry with which the data should be linked
	 * @return True if import was successful, false if it failed
	 */
	public boolean importShareData(Share share, String toImport)
	{
		int hashValue = searchEntry(toImport);
		
		if(hashValue == -3)
		{
			return false;
		}
		
		if(hashValue == -2)
		{
			System.out.println("Entry was not found");
			return false;
		}
		else if(hashValue == -1)
		{
			System.out.println("Entry was already deleted");
			return false;
		}
		else
		{
			table[hashValue].importShare(share);
			return true;
		}
	
	}
	
	/**Search for an specific entry
	 * 
	 * @param name The name of the entry
	 * @return Integer representing the position of the entry in the table or -1 if deleted, -2 if not found, -3 if hahsing failed
	 */
	public int searchEntry(String name)
	{
		int i = 0;
		int hashValue = hash(name);
		
		if(hashValue == -1)
		{
			return -3;
		}
		
		while(true)
		{
			hashValue += i*i;
			if(table[hashValue] == null)
			{
				return -2;
			}
			
			if(table[hashValue].getName().equalsIgnoreCase(name) || table[hashValue].getShortName().equalsIgnoreCase(name))
			{
				if(table[hashValue].isDeleted())
				{
					return -1;
				}
				else
				{
					return hashValue;
				}
			}
			else
			{
				i++;
			}
		}
	}
	
	/**Plot a share
	 * 
	 * @param name The name of the entry
	 */
	public void plotEntry(String name)
	{
		int hashValue = searchEntry(name);
		
		if(hashValue == -3)
		{
			return;
		}
		
		if(hashValue == -2)
		{
			System.out.println("Entry was not found");
		}
		else if(hashValue == -1)
		{
			System.out.println("Entry was already deleted");
		}
		else
		{
			table[hashValue].plotShare();
		}
	}
	
	/**Print the latest share data
	 * 
	 * @param name The name of the entry
	 */
	public void printLatestEntry(String name)
	{
		int hashValue = searchEntry(name);
		
		if(hashValue == -3)
		{
			return;
		}
		
		if(hashValue == -2)
		{
			System.out.println("Entry was not found");
		}
		else if(hashValue == -1)
		{
			System.out.println("Entry was already deleted");
		}
		else
		{
			table[hashValue].printLatest();
		}
	}
	
	/**Print all share data available
	 * 
	 * @param name The name of the entry
	 */
	public void printEntry(String name)
	{
		int hashValue = searchEntry(name);
		
		if(hashValue == -3)
		{
			return;
		}
		
		if(hashValue == -2)
		{
			System.out.println("Entry was not found");
		}
		else if(hashValue == -1)
		{
			System.out.println("Entry was already deleted");
		}
		else
		{
			table[hashValue].print();
		}
	}
	
	/**Creates a hashtable of existing hashtable using short names 
	 * 
	 * @return The resulting hashtable
	 */
	public MyHashtable createShortNameHashtable()
	{
		MyHashtable hashtable = new MyHashtable();
		
		for(Entry e: table)
		{
			if(e != null)
			{
				hashtable.addEntry(e, e.getShortName());
			}
		}
		
		return hashtable;
	}
	
	/**Saves this hashtable to file (in sub-directory "save" relative to where the project was launched)
	 * 
	 * @param nameOfFile The name of the file the hashtable should be saved as
	 */
	public void save(String nameOfFile)
	{
		try
		{
			File savedir = new File("save/");
			if(!savedir.exists())
			{
				savedir.mkdir();
			}
			FileOutputStream fileOut = new FileOutputStream(new File(savedir,nameOfFile + ".ser"));
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in save/" + nameOfFile + ".ser");
		}
		catch(IOException i)
		{
			i.printStackTrace();
		}
	}
	
	/**Loads a hashtable from file (from sub-directory "save" relative to where the project was launched)
	 * 
	 * @param nameOfFile The name of the file that should be loaded
	 * @return The loaded Hashtable
	 */
	public static MyHashtable load(String nameOfFile)
	{
		MyHashtable hashTable;
		try
		{
			FileInputStream fileIn = new FileInputStream("save/" + nameOfFile + ".ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			hashTable = (MyHashtable) in.readObject();
			in.close();
			fileIn.close();
		}
		catch(IOException i)
		{
			System.out.println("Could not load Hashtable");
			i.printStackTrace();
			return null;
		}
		catch(ClassNotFoundException c)
		{
			System.out.println("Could not load Hashtable");
			c.printStackTrace();
			return null;
		}
	return hashTable;
	}
}