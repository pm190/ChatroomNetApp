package chat.room;

import java.util.HashMap;
import java.util.TreeSet;

import muc.MultiUserChatManager;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;


/**This class contain methods to create and manage rooms
 * Also methods to join rooms and discover room list and joined rooms
 * @author Marcin Kopacz
 *
 */
public class RoomManager 
{
	private XMPPConnection connection;
	private TreeSet<Room> roomList ;
	
	/**Constructor for RoomManager
	 * To create room we need to use user connection 
	 * @param connection XMPPConnection
	 */
	public RoomManager(XMPPConnection connection) 
	{
		this.connection = connection;
		roomList = new TreeSet<Room>();
		
	}
	
	
	/**Create simple room with basic settings
	 * Insert owner of the room into room
	 * @param roomName String
	 * @param username String
	 */
	public void createInstantPublicRoom(String roomName, String username)
	{
		try
		{
			
			// Create a MultiUserChat using a Connection for a room
			String [] serverDetails = IgniteConnector.getServerDetails();
			String roomNameFull = roomName + "@conference." + serverDetails[2];
			MultiUserChat muc = new MultiUserChat(connection, roomNameFull);

			// Create the room
			muc.create(username);
			
			// Send an empty room configuration form which indicates that we want
			// an instant room
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));

			//show confirmation in console
			System.out.println("Connected: " + roomName + " has been created");
			
			joinRoomByName(roomName,username);
			
			Room room = new Room(muc,roomName);
			roomList.add(room);
			
		}
		catch (XMPPException e) 
		{
			
			e.printStackTrace();
		}
	}

	
	public void joinRoomByName(String roomName, String username)
	{
		try 
		{
		Room room =getRoomByName(roomName);
		room.muc.join(username);
		} 
		catch (XMPPException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void startPrivateChat(MultiUserChat muc,String privateNameRoom, String participant)
	{
		String [] serverDetails = IgniteConnector.getServerDetails();
		String roomNameFull = "Priv-"+ privateNameRoom + "@conference." + serverDetails[2]+ "/"+ participant;
		MyMessageListener ml = new MyMessageListener();
		Chat chat = muc.createPrivateChat(roomNameFull,ml);
		
		Room chatRoom = new Room(chat, participant);
		roomList.add(chatRoom);
	    
	}
	
	
	/**send Message to Room
	 * @param messaage string
	 * @param muc
	 */
	public void sendMessaage(String messaage, Room room)
	{
		try 
		{
			if(room.muc != null)
			{
			room.muc.sendMessage(messaage);
			}
			else
			{
			room.chatRoom.sendMessage(messaage);
			}
			
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	

	
	/** Finds room with givenName
	 * ChatName == MultiUserChat Name or ChatName == 
	 * @param ChatName 
	 * @return Room
	 */
	public Room getRoomByName(String chatName)
	{
		for (Room room : roomList)
		{
			if (room.getRoomName().equals(chatName))
			{
				return room;
			}
				
		}
		return null;
		
	}
}
