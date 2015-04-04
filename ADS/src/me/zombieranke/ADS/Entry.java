package me.zombieranke.ADS;

public class Entry
{
	private String name;
	private String kuerzel;
	private boolean deletedFlag;
	private Share share;
	
	public Entry(String name, String kuerzel)
	{
		this.name = name;
		this.kuerzel = kuerzel;
		deletedFlag = false;
	}
	
	public void importShare()
	{
		
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
	
	public String getKuerzel()
	{
		return kuerzel;
	}
	
	
	
	
}
