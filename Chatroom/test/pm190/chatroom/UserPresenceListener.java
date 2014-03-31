package pm190.chatroom;

import java.util.List;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

/**
 * 
 * @author Patrick Mackinder
 */
public class UserPresenceListener implements PacketListener
{
	private final String roomName;
	private final String username;
	private final List<ChatMessage> messages;
	
	public UserPresenceListener(String roomName, String username, List<ChatMessage> messages)
	{
		this.roomName = roomName;
		this.username = username;
		this.messages = messages;
	}

	@Override
	public void processPacket(Packet packet)
	{
		String status = packet.toString();
		String from = StringUtils.parseResource(packet.getFrom());
		if(!username.equals(from) && validStatus(status))
		{
			ChatMessage chatMessage = new ChatMessage(from + getStatusMessage(status), getStatusColour(status), roomName);
			messages.add(chatMessage);
			PushContext pushContext = PushContextFactory.getDefault().getPushContext();
			pushContext.push("/message", chatMessage);
			pushContext.push("/users", "user left/joined room");
		}
	}
	
	private boolean validStatus(String status)
	{
		return (status.equals("available") || status.equals("unavailable"));
	}
	
	private String getStatusMessage(String status)
	{
		if(status.equals("available"))
		{
			return " has entered the room.";
		}
		else
		{
			return " has left the room.";
		}
	}
	
	private String getStatusColour(String status)
	{
		if(status.equals("available"))
		{
			return "green";
		}
		else
		{
			return "grey";
		}
	}
}
