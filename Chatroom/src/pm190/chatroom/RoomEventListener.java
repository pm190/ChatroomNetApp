package pm190.chatroom;

import java.util.List;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

/**
 * Listens to room events, notably users leaving and joining rooms.
 * @author Patrick Mackinder
 */
public class RoomEventListener implements PacketListener
{
	private final String roomName;
	private final String username;
	private final List<ChatMessage> messages;
	
	/**
	 * Create new room
	 * @param roomName of room to listen to
	 * @param username of listening user
	 * @param messages list to add message to
	 */
	public RoomEventListener(String roomName, String username, List<ChatMessage> messages)
	{
		this.roomName = roomName;
		this.username = username;
		this.messages = messages;
	}

	/**
	 * Process a packet from room, only interested with available and unavailable statuses
	 * Push messages to users to notify them of user joining/leaving
	 */
	@Override
	public void processPacket(Packet packet)
	{
		String status = packet.toString();
		String from = StringUtils.parseResource(packet.getFrom());
		System.out.println("New room packet=" + status + " from=" + from);
		if(!username.equals(from) && validStatus(status))
		{
			ChatMessage chatMessage = new ChatMessage(from + getStatusMessage(status), getStatusColourClass(status), roomName);
			messages.add(chatMessage);
			PushContext pushContext = PushContextFactory.getDefault().getPushContext();
			pushContext.push("/message/"+username, chatMessage);
			pushContext.push("/user", "user left/joined room");
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
	
	private String getStatusColourClass(String status)
	{
		if(status.equals("available"))
		{
			return "join";
		}
		else
		{
			return "leave";
		}
	}
}
