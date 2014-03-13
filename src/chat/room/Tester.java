package chat.room;


import org.jivesoftware.smack.XMPPConnection;

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
		
		
		
		
		
		// just for testing infinite loop to maintain connection
		boolean isRunning = true;
		while (isRunning) 
		{
			Thread.sleep(50);

		}
		

	}


}
