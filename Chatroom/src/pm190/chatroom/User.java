package pm190.chatroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private final Map<String, MultiUserChat> roomChats = new HashMap<String, MultiUserChat>();
	
	public User(XMPPConnection connection) throws XMPPException
	{
		this.connection = connection;
		connection.loginAnonymously();
		String fullname = connection.getUser();
		//TODO change to stringutils parse resource
		this.username = fullname.substring(0, fullname.indexOf("@"));
	}
	
	public User(XMPPConnection connection, String username, String password) throws XMPPException
	{
		this.connection = connection;
		this.username = username;
		connection.login(username, password);
	}

	public XMPPConnection getConnection()
	{
		return connection;
	}

	public String getUsername()
	{
		return username;
	}
	
	public void joinRoomWithMUC(String roomName) throws XMPPException
	{
		RoomChatListener roomChatListener = new RoomChatListener(connection, roomName, username);
		Thread roomThread = new Thread(roomChatListener);
		roomThread.setDaemon(true);
		roomThread.start();
		MultiUserChat muc = new MultiUserChat(connection, roomName + "@" + ServerProperties.getServicename());
		roomChats.put(roomName, muc);
	}
	
	public MultiUserChat getMultiUserChat(String roomName)
	{
		return roomChats.get(roomName);
	}
	
	public List<String> getRoomNames()
	{
		List<String> roomNames = new ArrayList<String>();
		roomNames.addAll(roomChats.keySet());
		return roomNames;
	}
	
	public void sendMessageToRoom(String message, String roomName)
	{
		try
		{
			MultiUserChat muc = getMultiUserChat(roomName);
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
}
