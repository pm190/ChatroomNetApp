package pm190.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import pm190.chatroom.ServerProperties;

/**
 * 
 * @author Tali
 */
@ManagedBean
@SessionScoped
public class ConnectionBean
{
	private final int packetReplyTimeout = 500; // millis
	private final ConnectionConfiguration config;
	private XMPPConnection connection;

	public ConnectionBean() throws XMPPException
	{
		SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);
		config = new ConnectionConfiguration(ServerProperties.getServeraddress(), ServerProperties.getServerport());
		config.setSecurityMode(SecurityMode.disabled);
		connection = new XMPPConnection(config);
		connection.connect();
	}

	public void destroy()
	{
		if(connection != null && connection.isConnected())
		{
			connection.disconnect();
		}
	}
	
	public XMPPConnection getConnection()
	{
		return connection;
	}
}
