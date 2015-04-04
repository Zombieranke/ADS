package me.zombieranke.ADS;

public class Entry
{
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
	
	public void print()
	{
		System.out.println("Name: " + name + ", Short name: " + shortName + ", WKN: " + wkn);
		share.print();
	}
	
	public void printLatest()
	{
		System.out.println("Name: " + name + ", Short name: " + shortName + ", WKN: " + wkn);
		share.printLatest();
	}
	
	public String getShortName()
	{
		return shortName;
	}
}
