package pm190.chatroom;

/**
 * Convenience class for server properties
 * @author Patrick Mackinder
 */
public class ServerPropertyUtils
{
	private static final String serverAddress = "92.236.126.119";
	private static final int serverPort = 5222;
	private static final String serverDomain = "olympus";
	private static final String serviceName = "conference.olympus";

	/**
	 * Gets server IP
	 * @return server IP
	 */
	public static String getServerAddress()
	{
		return serverAddress;
	}

	/**
	 * Gets server port
	 * @return port
	 */
	public static int getServerPort()
	{
		return serverPort;
	}

	/**
	 * Gets XMPP server domain
	 * @return domain
	 */
	public static String getServerDomain()
	{
		return serverDomain;
	}

	/**
	 * Gets XMPP service name
	 * @return service name
	 */
	public static String getServiceName()
	{
		return serviceName;
	}
}
