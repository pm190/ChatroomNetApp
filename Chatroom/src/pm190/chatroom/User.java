package pm190.chatroom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * 
 * @author Patrick Mackinder
 */
public class User
{
	private final XMPPConnection connection;
	private final String username;
	private final Set<Room> rooms = new HashSet<Room>();
	
	public User(XMPPConnection connection)
	{
		this.connection = connection;
		try
		{
			connection.loginAnonymously();
		}
		catch(XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			String fullname = connection.getUser();
			this.username = fullname.substring(0, fullname.indexOf("@"));
		}
	}
	
	public User(XMPPConnection connection, String username, String password)
	{
		this.connection = connection;
		this.username = username;
		try
		{
			connection.login(username, password);
		}
		catch(XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public XMPPConnection getConnection()
	{
		return connection;
	}

	public String getUsername()
	{
		return username;
	}
	
	public void joinRoom(String roomName)
	{
		MultiUserChat muc = new MultiUserChat(connection, roomName + "@" + ServerProperties.getServicename());
		try
		{
			muc.join(username);
			rooms.add(new Room(roomName));
		}
		catch(XMPPException e)
		{
			e.printStackTrace();
		}
	}
	
	public List<String> getRoomNames()
	{
		List<String> roomNames = new ArrayList<String>();
		for(Room room : rooms)
		{
			roomNames.add(room.getName());
		}
		return roomNames;
	}
}
