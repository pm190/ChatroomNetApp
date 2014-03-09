package pm190.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

/**
 * 
 * @author Patrick Mackinder
 */
public class XMPPManager implements MessageListener
{
	private XMPPConnection connection;

	public void login(String userName, String password) throws XMPPException
	{
		ConnectionConfiguration config = new ConnectionConfiguration("192.168.0.10", 5222);
		connection = new XMPPConnection(config);

		connection.connect();
		connection.login(userName, password);
	}

	public void sendMessage(String message, String to) throws XMPPException
	{
		Chat chat = connection.getChatManager().createChat(to, this);
		chat.sendMessage(message);
	}

	public void disconnect()
	{
		connection.disconnect();
	}

	public void processMessage(Chat chat, Message message)
	{
		if(message.getType() == Message.Type.chat)
		{
			System.out.println(chat.getParticipant() + " says: " + message.getBody());
		}
	}

	public static void main(String args[]) throws XMPPException, IOException
	{
		XMPPManager c = new XMPPManager();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String msg;
		
		// Enter your login information here
		c.login("test1", "test1");
		
		System.out.println("-----");
		System.out.println("Who do you want to talk to? - Type contacts full email address:");
		String talkTo = br.readLine();

		System.out.println("-----");
		System.out.println("All messages will be sent to " + talkTo);
		System.out.println("Enter your message in the console:");
		System.out.println("-----\n");

		while(!(msg = br.readLine()).equals("bye"))
		{
			c.sendMessage(msg, talkTo);
		}
		c.disconnect();
		System.exit(0);
	}
}
