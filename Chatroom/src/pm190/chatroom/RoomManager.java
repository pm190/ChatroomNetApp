package pm190.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
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
	
	public static void main(String args[]) throws XMPPException, IOException
	{
		XMPPConnection connection;

		ConnectionConfiguration config = new ConnectionConfiguration("92.236.126.119", 5222);
		connection = new XMPPConnection(config);

		connection.connect();
		connection.login("sa521", "sa521");
		
		RoomManager rm = new RoomManager();
		rm.createRoom(connection, "testroom");
	}
}
