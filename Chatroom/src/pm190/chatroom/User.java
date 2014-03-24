package pm190.chatroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
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
	
	public MultiUserChat joinRoomWithMUC(String roomName)
	{
		MultiUserChat muc = new MultiUserChat(connection, roomName + "@" + ServerProperties.getServicename());
		roomChats.put(roomName, muc);
		return roomChats.get(roomName);
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
	
	public String nextMessage(String roomName)
	{
		MultiUserChat muc = getMultiUserChat(roomName);
		if(muc != null)
		{
			Message msg = muc.pollMessage();
			if(msg != null)
			{
				return StringUtils.parseResource(msg.getFrom()) + ": " + msg.getBody();
			}
		}
		return null;
	}
}
