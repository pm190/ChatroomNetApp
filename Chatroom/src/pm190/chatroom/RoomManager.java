package pm190.chatroom;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.*;
import org.jivesoftware.smackx.Form;

/**
 * 
 * @author Patrick Mackinder
 */
public class RoomManager
{
	public void createRoom(Connection connection,String roomName) throws XMPPException
	{
		MultiUserChat muc = new MultiUserChat(connection, roomName);
		muc.create(roomName);
		muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
	}
	
}
