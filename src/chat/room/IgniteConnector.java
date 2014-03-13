package chat.room;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;



/**This class holds methods that let user connect to xmpp server
 * @author Marcin Kopacz
 *
 */
public class IgniteConnector 
{
	private static final int packetReplyTimeout = 500; // millis
	private static String serverHost = "92.236.126.119";
	private static int serverPort = 5222;
	private static String openfireDomain ="olympus";

	private ConnectionConfiguration config;
	private XMPPConnection connection;




	/**Connect to ignite type of server
	 * Use server declared in  serverHost, serverPort
	 * @param username String
	 * @param password String
	 */
	public void connect(String username, String password)
	{
		try{

			//Set timeout
			SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);

			//create config for conncetion
			config = new ConnectionConfiguration(serverHost, serverPort);
			config.setSASLAuthenticationEnabled(false);
			config.setSecurityMode(SecurityMode.disabled);

			
			// Create a connection to the config XMPP server.
			connection = new XMPPConnection(config);
			
			// Connect to the server
			connection.connect();
			System.out.println("Connection has been established: " + connection.isConnected());

			//log in
			performLogin(username, password);

			
		} 
		catch (XMPPException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**User can Set up his status 
	 * @param available boolean(default true for available)
	 * @param status description
	 */
	public void setStatus(boolean available, String status) 
	{
		Presence.Type type = available? Type.available: Type.unavailable;
		Presence presence = new Presence(type);
		presence.setStatus(status);
		connection.sendPacket(presence);

	}

	/**Log in user with given username and password
	 * @param username
	 * @param password
	 */
	public void performLogin(String username, String password)
	{
		try
		{
			if (connection!=null && connection.isConnected()) 
			{
				connection.login(username, password);
				System.out.println("User: " + username +" -succefull login");
			}
		}
		catch (XMPPException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	/**Disconnect a connection
	 */
	public void destroyConnection() 
	{
		if (connection!=null && connection.isConnected()) 
		{
		connection.disconnect();
		}

	}


	/** Create String array consisting of server and port number
	 * @return string array[2]
	 */
	public static String[] getServerDetails()
	{
		String [] details = new String[]{serverHost, Integer.toString(serverPort),openfireDomain};
		return details;
	}


	/**Change default ignite server
	 * 
	 * @param serverHost 
	 * @param serverPort
	 */
	public void setIgniteServer(String serverHost, int serverPort,String openfireDomain)
	{
		IgniteConnector.serverHost = serverHost;
		IgniteConnector.serverPort = serverPort;
		IgniteConnector.openfireDomain = openfireDomain;
	}

	/**Get connection
	 * 
	 * @return
	 */
	public XMPPConnection getConnection()
	{
		return connection;
	}
	
	
}
