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
	private final Map<String, MultiUserChat> rooms = new HashMap<String, MultiUserChat>();
	private final Map<String, List<String>> usersInRooms = new HashMap<String, List<String>>();

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

	public void joinRoom(User user, String roomName)
	{
		try
		{
			user.joinRoomWithMUC(roomName);
			if(!usersInRooms.containsKey(roomName))
			{
				usersInRooms.put(roomName, new ArrayList<String>());
			}
			usersInRooms.get(roomName).add(user.getUsername());
		}
		catch(XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createRoomDontJoin(User user, String roomName)
	{
		rooms.put(roomName, new MultiUserChat(user.getConnection(), roomName + "@" + ServerProperties.getServicename()));
	}

	public void createRoom(User user, String roomName)
	{
		if(rooms.containsKey(roomName))
		{
			joinRoom(user, roomName);
		}
		else
		{
			rooms.put(roomName, new MultiUserChat(user.getConnection(), roomName + "@" + ServerProperties.getServicename()));
			joinRoom(user, roomName);
		}
	}

	public List<String> getAllRooms()
	{
		List<String> roomNames = new ArrayList<String>();
		roomNames.addAll(rooms.keySet());
		return roomNames;
	}
	
	public List<String> getAllUsersInRoom(String roomName, User user)
	{
		List<String> roomNames = new ArrayList<String>();
		if(roomName != null && !roomName.equals(""))
		{
			List<String> names = usersInRooms.get(roomName);
			if(names != null)
			{
				names.remove(user.getUsername());
				roomNames.addAll(names);
			}
		}
		return roomNames;
	}

	public void leaveRoom(User user, String roomName)
	{
		List<String> users = usersInRooms.get(roomName);
		users.remove(user.getUsername());
		if(users.size()==0)
		{
			usersInRooms.remove(roomName);
			rooms.remove(roomName);
		}
	}
}
