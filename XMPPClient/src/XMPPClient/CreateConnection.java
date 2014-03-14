package XMPPClient;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;



//a class that establishes a connection with the xmpp server
//it also creats an instance of Account that is then passed up to xmpptest
public class CreateConnection {

	private static final int packetReplyTimeout = 500; // millis
	private String server;
	private int port;

	private ConnectionConfiguration config;
	private XMPPConnection connection;

	private ChatManager chatManager;
	private MessageListener messageListener;
	private Account myAccount;
	
	

	public CreateConnection(String server, int port) {
		this.server = server;
		this.port = port;
		
		
		
	}




	public void init() throws XMPPException {

		System.out.println(String.format("Initializing connection to server %1$s port %2$d", server, port));

		SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);

		config = new ConnectionConfiguration(server, port);
		
		//Unsure why set to false in this example and seems to work fine with it not set to false, so leaving out
	//	config.setSASLAuthenticationEnabled(false);
		//unsure what this is
		config.setSecurityMode(SecurityMode.disabled);

		connection = new XMPPConnection(config);
		connection.connect();

		System.out.println("Connected: " + connection.isConnected());

		chatManager = connection.getChatManager();
		messageListener = new MyMessageListener();
		myAccount = new Account(connection);

	}


	
	public XMPPConnection getConnection()
	{
		return connection;
	}
	public ConnectionConfiguration getConfig()
	{
		return config;
	}
	public Account getAccount()
	{
		return myAccount;
	}
	public XMPPConnection connect()throws XMPPException
	{
		connection = new XMPPConnection(config);
		connection.connect();
		return connection;
	}
	public void destroy() {

		if (connection!=null && connection.isConnected()) {
			connection.disconnect();
		}

	}



	class MyMessageListener implements MessageListener {



		@Override
		public void processMessage(Chat chat, Message message) {
			String from = message.getFrom();
			String body = message.getBody();
			System.out.println(String.format("Received message '%1$s' from %2$s", body, from));

		}



	}
	
	



}

