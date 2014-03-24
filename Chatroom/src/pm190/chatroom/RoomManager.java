package pm190.chatroom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import pm190.beans.ConnectionBean;

/**
 * 
 * @author Patrick Mackinder
 */
public class RoomManager
{
	private final Map<String, Room> rooms = new HashMap<String, Room>();

	public RoomManager()
	{
		try
		{
			User user = new User(new ConnectionBean().getConnection(), "RoomManager", "RoomManager");
			Collection<HostedRoom> hostedRooms = MultiUserChat.getHostedRooms(user.getConnection(), "conference." + ServerProperties.getServerdomain());
			for(HostedRoom room : hostedRooms)
			{
				createRoomDontJoin(user, room.getName());
			}
		}
		catch(XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void joinRoom(User user, String name)
	{
		try
		{
			user.joinRoomWithMUC(name).join(user.getUsername());
		}
		catch(XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createRoomDontJoin(User user, String name)
	{
		Room room = new Room(name, new MultiUserChat(user.getConnection(), name + "@" + ServerProperties.getServicename()));
		rooms.put(name, room);
	}

	public void createRoom(User user, String name)
	{
		if(rooms.containsKey(name))
		{
			joinRoom(user, name);
		}
		else
		{
			Room room = new Room(name, new MultiUserChat(user.getConnection(), name + "@" + ServerProperties.getServicename()));
			rooms.put(name, room);
			joinRoom(user, name);
		}
	}
	
	public void sendMessageToRoom(User user, String message, String roomName)
	{
		try
		{
			MultiUserChat muc = user.getMultiUserChat(roomName);
			if(muc != null)
			{
				muc.sendMessage(message);
			}
		}
		catch(XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> getAllRooms()
	{
		List<String> roomNames = new ArrayList<String>();
		roomNames.addAll(rooms.keySet());
		return roomNames;
	}
}
