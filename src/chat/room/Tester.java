package chat.room;


import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class Tester {

	public static void main(String[] args) throws InterruptedException 
	{


		//run test for IgniteConnection to our openfire server 
		IgniteConnector testConnector = new IgniteConnector();
		testConnector.connect("javatester","test");
		testConnector.setStatus(true, "mistrzu entered");
		
		
		
		//To create room we need to user connection because rooms instance are tied to user connection
		//If we want to have permanents rooms we need to have some bots
		XMPPConnection connection = testConnector.getConnection();
		RoomManager rc = new RoomManager(connection);
		//Create instant public room with given name and user that will enter automaticly
		rc.createInstantPublicRoom("testRoom","javatester");
		
		//Create private chat with another user
		//This tart a chat with another user in MultiUserChat instance
		//MultiUserChat muc = get
		//Chat chat1 = RoomManager.startPrivateChat(muc,"privateNameRoom", "sparktestclient");
		//RoomManager.sendMessages("Hi, how old are you",chat1);
		//RoomManager.sendMessages("I am 22",chat1);
		
		
		
		// just for testing infinite loop to maintain connection
		boolean isRunning = true;
		while (isRunning) 
		{
			Thread.sleep(50);

		}
		

	}


}
