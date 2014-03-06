package pm190.chatroom;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * @author Patrick Mackinder
 */
public class Chat
{
	Collection<String> messages = new ArrayList<String>();
	
	public Chat()
	{
	}
	
	public void addMessage(String message)
	{
		messages.add(message);
	}
}
