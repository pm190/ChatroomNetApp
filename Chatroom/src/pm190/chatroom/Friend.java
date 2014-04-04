package pm190.chatroom;

/**
 * Friend represents a friend in one point in time. It is used when generating a list of online
 * and offline friends.
 * @author Patrick Mackinder
 */
public class Friend
{
	private final String name;
	private final boolean offline;
	private final String style;
	
	/**
	 * Create a friend with a name, and status
	 * @param name
	 * @param offline=true, online=false
	 */
	public Friend(String name, boolean offline)
	{
		this.name = name;
		this.offline = offline;
		this.style = offline ? "offline" : "online";
	}
	
	/**
	 * Gets friend name
	 * @return friend name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets offline status
	 * @return true=offline, false=online
	 */
	public boolean getOffline()
	{
		return offline;
	}

	/**
	 * Gets the style
	 * @return offline or online depending on status
	 */
	public String getStyle()
	{
		return style;
	}
}
