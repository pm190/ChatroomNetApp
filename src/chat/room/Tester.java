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
		//Create instant public room with name testroom1  and user that will enter automatically and being set as owner
		rc.createInstantPublicRoom("testRoom1", "javatester");
		//Create instant public room with name testroom1  and user that will enter automatically and being set as owner
		rc.createInstantPublicRoom("testRoom2", "javatester");



		//start private chat this method need some work to work
		rc.startPrivateChat("testRoom1","nice", "sparktestclient");
		//send message to private chat
		rc.sendMessaage("hi I am Marcin","sparktestclient");

		// invite to multi user chat
		rc.invite("testRoom2","sparktestclient", "Lets talk");

		//Send Message to muc room
		rc.sendMessaage("hi I am Marcin","testRoom2");

		//List of Room
		rc.getListOfActiveRooms();
		

		boolean isRunning = true;
		while (isRunning) 
		{
			Thread.sleep(50);
		}







	}


}
