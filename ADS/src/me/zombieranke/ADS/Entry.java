package me.zombieranke.ADS;

import java.io.Serializable;

public class Entry implements Serializable
{
	/**Automatically generated id to identify class at load*/
	private static final long serialVersionUID = -1198101165367088077L;
	private String name;
	private String shortName;
	private String wkn;
	private boolean deletedFlag;
	private Share share;
	
	public Entry(String name, String shortName, String wkn)
	{
		this.name = name;
		this.shortName = shortName;
		this.wkn = wkn;
		deletedFlag = false;
	}
	
	public void importShare(Share share)
	{
		this.share = share;
		deletedFlag = false;
	}
	
	public void deleteShare()
	{
		share = null;
		deletedFlag = true;
	}
	
	
	public boolean isDeleted()
	{
		return deletedFlag;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getShortName()
	{
		return shortName;
	}
	
	public void print()
	{
		System.out.println("Name: " + name + ", Short name: " + shortName + ", WKN: " + wkn);
		if(share == null)
		{
			System.out.println("Entry has no data yet. If you want to update the data use the 'IMPORT' function\n");
		}
		else
		{
		share.print();
		}
	}
	
	public void printLatest()
	{
		System.out.println("Name: " + name + ", Short name: " + shortName + ", WKN: " + wkn);
		if(share == null)
		{
			System.out.println("Entry has no data yet. If you want to update the data use the 'IMPORT' function\n");
		}
		else
		{
			share.printLatest();
		}
	}
	
	public void plotShare()
	{
		if(share == null)
		{
			System.out.println("Entry has no data yet. If you want to update the data use the 'IMPORT' function\n");
		}
		else
		{
			share.plot();
		}
	}
	
}
