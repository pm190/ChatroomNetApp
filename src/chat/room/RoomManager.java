package chat.room;

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
 * 
 * @author Marcin Kopacz
 *
 */
public class RoomManager 
{
	private XMPPConnection connection;


	
	static {
	    XMPPConnection.DEBUG_ENABLED = true;
	}
	
	public RoomManager(XMPPConnection connection) 
	{
		this.connection = connection;
	}


	public void createInstantPublicRoom(String roomName, String username)
	{
		try
		{
			// Create a MultiUserChat using a Connection for a room
			String [] serverDetails = IgniteConnector.getServerDetails();
			String roomNameFull = roomName + "@conference." + serverDetails[2];
			MultiUserChat muc = new MultiUserChat(connection, roomNameFull);

			// Create the room
			muc.create("roomName");
			
			// Send an empty room configuration form which indicates that we want
			// an instant room
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));

			
			muc.join(username);
			//show confirmation in console
			System.out.println("Connected: " + roomName + " has been created");
		}
		catch (XMPPException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




}
