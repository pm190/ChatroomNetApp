package pm190.chatroom;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Patrick Mackinder
 */
public class Room
{
	private final String name;
	private final Map<String, User> users = new HashMap<String, User>();
	private final Chat chat = new Chat();
	
	public Room(String name)
	{
		this.name = name;
	}
}
