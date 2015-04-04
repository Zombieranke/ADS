package me.zombieranke.ADS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MyHashtable implements Serializable
{

	/**
	 * Random serial to identify class type
	 */
	private static final long serialVersionUID = -6414770308414785767L;
	private static final int hashPrimeNumber = 10007;
	private static final int alphabetLength = 128;
	private Entry[] table;
	
	
	public MyHashtable()
	{
		int i = 0;
		table = new Entry[hashPrimeNumber];
		
		for(i=0;i<hashPrimeNumber;i++)
		{
			table[i] = null;
		}
	}
	
	
	private static int hash(String name)
	{
		char[] nameChar = name.toCharArray();
		int length = name.length();
		int i = 0;
		
		int sum = nameChar[0];
		
		for(i=1;i<length-1;i++)
		{
			sum = (sum * alphabetLength + nameChar[i]) % hashPrimeNumber;
		}
		
		return sum;
	}
	
	
	public boolean addEntry(Entry entry,String toHash)
	{
		int i = 0;
		
		int hashValue = hash(toHash);
		
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
	
	public boolean deleteEntry(String name)
	{
		int hashValue = searchEntry(name);
		
		if(hashValue == -2)
		{
			System.out.println("Entry was not found\n");
			return false;
		}
		else if(hashValue == -1)
		{
			System.out.println("Entry was already deleted\n");
			return false;
		}
		else
		{
			table[hashValue].deleteShare();
			System.out.println("Entry is now deleted\n");
			return true;
		}
		
	}
	
	public boolean importShareData(Share share, String toImport)
	{
		int hashValue = searchEntry(toImport);
		
		if(hashValue == -2)
		{
			System.out.println("Entry was not found\n");
			return false;
		}
		else if(hashValue == -1)
		{
			System.out.println("Entry was already deleted\n");
			return false;
		}
		else
		{
			table[hashValue].importShare(share);
			return true;
		}
	
	}
	
	public int searchEntry(String name)
	{
		int i = 0;
		int hashValue = hash(name);
		
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
	
	public void plotEntry(String name)
	{
		int hashValue = searchEntry(name);
		
		if(hashValue == -2)
		{
			System.out.println("Entry was not found\n");
		}
		else if(hashValue == -1)
		{
			System.out.println("Entry was already deleted\n");
		}
		else
		{
			table[hashValue].plotShare();
		}
	}
	
	public void printLatestEntry(String name)
	{
		int hashValue = searchEntry(name);
		
		if(hashValue == -2)
		{
			System.out.println("Entry was not found\n");
		}
		else if(hashValue == -1)
		{
			System.out.println("Entry was already deleted\n");
		}
		else
		{
			table[hashValue].printLatest();
		}
	}
	
	public void printEntry(String name)
	{
		int hashValue = searchEntry(name);
		
		if(hashValue == -2)
		{
			System.out.println("Entry was not found\n");
		}
		else if(hashValue == -1)
		{
			System.out.println("Entry was already deleted\n");
		}
		else
		{
			table[hashValue].print();
		}
	}
	
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
			System.out.printf("Serialized data is saved in save/" + nameOfFile + ".ser");
		}
		catch(IOException i)
		{
			i.printStackTrace();
		}
	}
	
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
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return null;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Could not load Hashtable");
	         c.printStackTrace();
	         return null;
	      }
		 return hashTable;
	}
}














