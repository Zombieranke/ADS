package me.zombieranke.ADS;

public class MyHashtable
{

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
			System.out.println("Entry already exists. If you want to update the data use the 'IMPORT' function\n");
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
					System.out.println("Entry added\n");
					return true;
				}
				else if(table[hashValue].isDeleted())
				{
					table[hashValue] = entry;
					System.out.println("Entry added\n");
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
	
	public void save(String nameOfFile)
	{
		
	}
	
	public static MyHashtable load(String nameOfFile)
	{
		return null;
	}
}














