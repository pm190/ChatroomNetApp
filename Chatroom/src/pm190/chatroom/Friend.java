package pm190.chatroom;

/**
 * 
 * @author Patrick Mackinder
 */
public class Friend
{
	private final String name;
	private final boolean offline;
	private final String style;
	
	public Friend(String name, boolean offline)
	{
		this.name = name;
		this.offline = offline;
		this.style = offline ? "offline" : "online";
	}
	
	public String getName()
	{
		return name;
	}

	public boolean getOffline()
	{
		return offline;
	}

	public String getStyle()
	{
		return style;
	}
}
