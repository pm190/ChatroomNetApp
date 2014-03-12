package chat.room;

public class Tester {

	public static void main(String[] args) throws InterruptedException 
	{


		//run test for conncetion to our ignite server 
		IgniteConnector testConnector = new IgniteConnector();
		testConnector.connect();

		//log in
		testConnector.performLogin("javatester","test");
		testConnector.setStatus(true, "mistrzu entered");

		
		// just for testing infinite loop to maintain conncetion
		boolean isRunning = true;
		while (isRunning) 
		{
			Thread.sleep(50);

		}
		

	}


}
