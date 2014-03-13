package chat.room;

import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.XMPPConnection;

public class Tester {

	public static void main(String[] args) throws InterruptedException 
	{


		//run test for IgniteConnection to our ignite server 
		IgniteConnector testConnector = new IgniteConnector();
		testConnector.connect("javatester","test");
		testConnector.setStatus(true, "mistrzu entered");
		
		
		XMPPConnection con = testConnector.getConnection();
		ChatManager chatManager = con.getChatManager();
		
		RoomManager rc = new RoomManager(chatManager);
		
		
		
		
		
		
		// just for testing infinite loop to maintain connection
		boolean isRunning = true;
		while (isRunning) 
		{
			Thread.sleep(50);

		}
		

	}


}
