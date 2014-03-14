package XMPPClient;
import org.jivesoftware.smack.*;
public class MyXMPPClient {
	
	public MyXMPPClient(){
	// Create a connection to XMPP admin port.
	
	XMPPConnection connection = new XMPPConnection("92.236.126.119");
	
	/*92.236.126.119:9090 -> XMPP admin port
92.236.126.119:5222 -> XMPP chat port
92.236.126.119:8080 -> Tomcat server*/
	
	try
	{
		// Connect to the server
	connection.connect();
	connection.login("TaliUni", "Tali4345");
	}
	catch(XMPPException xmppe)
	{
		
	}
	
	
	
	}


}
