package me.zombieranke.ADS;

public class myHashtable
{

	private static final int hashPrimeNumber = 10007;
	private static final int alphabetLength = 128;
	private Entry[] table;
	
	
	public myHashtable()
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
	
	
	public boolean addEntry(myHashtable nameTable,myHashtable kuerzelTable,String name,String kuerzel)
	{
		boolean added = false;
		int i = 0;
		Entry entry = new Entry(name,kuerzel);
		
		int hashValue = hash(name);
		int hashValue2 = hash(kuerzel);
		
		
		while(!added)
		{
			i++;
			
			if(nameTable.table[hashValue] == null)
			{
				nameTable.table[hashValue] = entry;
				added = true;
			}
			else if(nameTable.table[hashValue].isDeleted())
			{
				nameTable.table[hashValue] = entry;
				added = true;
			}
			else if(nameTable.table[hashValue].getName() == name || nameTable.table[hashValue].getKuerzel() == name)
			{
				System.out.println("Entry already exists. If you want to update the data use the 'import' function\n");
				return false;
			}
			else
			{
				hashValue += i*i;
			}
		}
		
		i = 0;
		
		added = false;
		
		
		while(!added)
		{
			i++;
			
			if(kuerzelTable.table[hashValue2] == null)
			{
				kuerzelTable.table[hashValue2] = entry;
				added = true;
			}
			else if(kuerzelTable.table[hashValue2].isDeleted())
			{
				kuerzelTable.table[hashValue2] = entry;
				added = true;
			}
			else if(kuerzelTable.table[hashValue2].getName() == name || kuerzelTable.table[hashValue2].getKuerzel() == name)
			{
				System.out.println("Entry already exists. If you want to update the data use the 'import' function\n");
				return false;
			}
			else
			{
				hashValue2 += i*i;
			}
		}
		
		
		if(nameTable.table[hashValue].equals(kuerzelTable.table[hashValue2]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void deleteEntry(myHashtable nameTable,String name)
	{
		int hashValue = searchEntry(nameTable,name);
		if(hashValue == -2)
		{
			System.out.println("Entry was not found\n");
		}
		else if(hashValue == -1)
		{
			System.out.println("Entry already deleted\n");
		}
		else
		{
			nameTable.table[hashValue].deleteShare();
		}
		
	}
	
	public int searchEntry(myHashtable nameTable,String name)
	{
		int i = 0;
		int hashValue = hash(name);
		
		while(true)
		{
			hashValue += i*i;
			if(nameTable.table[hashValue] == null)
			{
				return -2;
			}
			
			if(nameTable.table[hashValue].getName() == name || nameTable.table[hashValue].getKuerzel() == name)
			{
				if(nameTable.table[hashValue].isDeleted())
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
	
	
	
}














