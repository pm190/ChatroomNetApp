package chat.room;

import java.util.HashMap;

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
	
	
	/**Constructor for RoomManager
	 * To create room we need to use user connection 
	 * @param connection XMPPConnection
	 */
	public RoomManager(XMPPConnection connection) 
	{
		this.connection = connection;
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
			HashMap<String,Room> RoomCollection= new HashMap<String,Room>();
					
			Room Room  = new Room();
			// Create a MultiUserChat using a Connection for a room
			String [] serverDetails = IgniteConnector.getServerDetails();
			String roomNameFull = roomName + "@conference." + serverDetails[2];
			MultiUserChat muc = new MultiUserChat(connection, roomNameFull);

			// Create the room
			muc.create("roomName");
			
			// Send an empty room configuration form which indicates that we want
			// an instant room
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));

			joinRoom(muc,username);
			//show confirmation in console
			System.out.println("Connected: " + roomName + " has been created");
		}
		catch (XMPPException e) 
		{
			
			e.printStackTrace();
		}
	}

	
	public void joinRoom(MultiUserChat muc,String username)
	{
		try 
		{
			muc.join(username);
		} 
		catch (XMPPException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static Chat startPrivateChat(MultiUserChat muc,String privateNameRoom, String participant)
	{
		String [] serverDetails = IgniteConnector.getServerDetails();
		String roomNameFull = "Priv-"+ privateNameRoom + "@conference." + serverDetails[2]+ "/"+ participant;
		MyMessageListener ml = new MyMessageListener();
		Chat chat = muc.createPrivateChat(roomNameFull,ml);
		
	    return chat;
	}
	
	
	
	public static void sendMessages(String message, Chat chat)
	{
		 try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

}
