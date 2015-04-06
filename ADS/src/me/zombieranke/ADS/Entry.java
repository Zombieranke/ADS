package me.zombieranke.ADS;

import java.io.Serializable;

/**A share entry which is holdable by MyHashtable
 * 
 * @author Christoph Majcen and Arthur Bouz
 */
public class Entry implements Serializable
{
	/**Automatically generated id to identify class at load*/
	private static final long serialVersionUID = -1198101165367088077L;
	
	/**Name of the share*/
	private String name;
	
	/**Short name of the share*/
	private String shortName;
	
	/**Identifier of the share*/
	private String wkn;
	
	/**Indicates whether the share was already deleted*/
	private boolean deletedFlag;
	
	/**Price data of the share*/
	private Share share;
	
	/**Creates a new share entry
	 * 
	 * @param name Name of the share
	 * @param shortName Short name of the share
	 * @param wkn Identifier of the share
	 */
	public Entry(String name, String shortName, String wkn)
	{
		this.name = name;
		this.shortName = shortName;
		this.wkn = wkn;
		deletedFlag = false;
	}
	
	/**Replace current prize data with specified price data
	 * 
	 * @param share The prize data to import
	 */
	public void importShare(Share share)
	{
		this.share = share;
		deletedFlag = false;
	}
	
	/**Deletes priz data attached to it */
	public void deleteShare()
	{
		share = null;
		deletedFlag = true;
	}
	
	/**Check if entry is already deleted
	 * 
	 * @return True if already deleted, false if not
	 */
	public boolean isDeleted()
	{
		return deletedFlag;
	}
	
	/**Get name of this share
	 * 
	 * @return the name of the share
	 */
	public String getName()
	{
		return name;
	}
	
	/**Get short name of this share
	 * 
	 * @return the short name of the share
	 */
	public String getShortName()
	{
		return shortName;
	}
	
	/**Print all prize data with name, short name and wkn as header */
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
	
	/**Print the most recent prize data with name, short name and wkn as header */
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
	
	/**Plot prize data with name, short name and wkn as header */
	public void plotShare()
	{
		System.out.println("Name: " + name + "          Short name: " + shortName + "           WKN: " + wkn);
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
