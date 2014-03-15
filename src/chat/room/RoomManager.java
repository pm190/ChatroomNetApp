package chat.room;

import java.util.TreeSet;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
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
			System.out.println(roomNameFull);
			MultiUserChat muc = new MultiUserChat(connection, roomNameFull);

			// Create the room
			muc.create(username);
			
			// Send an empty room configuration form which indicates that we want
			// an instant room
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));

			//show confirmation in console
			System.out.println("Connected: " + roomName + " has been created");
			
			Room room = new Room(muc,roomName);
			roomList.add(room);
			
			joinRoomByName(roomName,username);
			
			
			
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
	
	
	public void invite(String roomName,String username, String message)
	{
		
			Room room = getRoomByName(roomName);
			String [] serverDetails = IgniteConnector.getServerDetails();
			String participant = username +"@" + serverDetails[2] + "/Spark 2.6.3";
			room.muc.invite(participant, message);
		
	}
	
	
	//
	public void startPrivateChat(String roomOfOrgin,String privateNameRoom, String participant)
	
	{
		Room room = getRoomByName(roomOfOrgin);
		String [] serverDetails = IgniteConnector.getServerDetails();
		String roomNameFull = "Priv-"+ privateNameRoom + "@conference." + serverDetails[2]+ "/"+ participant;
		MyMessageListener ml = new MyMessageListener();
		Chat chat = room.muc.createPrivateChat(roomNameFull,ml);
		
		Room chatRoom = new Room(chat, participant);
		roomList.add(chatRoom);
	    
	}
	
	
	/**send Message to Room
	 * @param messaage string
	 * @param muc
	 */
	public void sendMessaage(String messaage, String roomName)
	{
		Room room = getRoomByName(roomName);
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
