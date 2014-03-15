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
		//Create instant public room with name testroom1  and user that will enter automatically and being set as owner
		rc.createInstantPublicRoom("testRoom1","javatester");
		//Create instant public room with name testroom1  and user that will enter automatically and being set as owner
		rc.createInstantPublicRoom("testRoom2", "javatester");
		
		
		//First find room with given Name
		Room testroom1 = rc.getRoomByName("testroom1");
		//Send Message to room
		rc.sendMessaage("hi I am Marcin",testroom1);
		
		
		
		
		// just for testing infinite loop to maintain connection
		boolean isRunning = true;
		while (isRunning) 
		{
			Thread.sleep(50);

		}
		

	}


}
