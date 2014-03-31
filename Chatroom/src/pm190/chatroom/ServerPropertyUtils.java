package pm190.chatroom;

/**
 * 
 * @author Patrick Mackinder
 */
public class ServerPropertyUtils
{
	private static final String serverAddress = "92.236.126.119";
	private static final int serverPort = 5222;
	private static final String serverDomain = "olympus";
	private static final String serviceName = "conference.olympus";

	public static String getServerAddress()
	{
		return serverAddress;
	}

	public static int getServerPort()
	{
		return serverPort;
	}

	public static String getServerDomain()
	{
		return serverDomain;
	}

	public static String getServiceName()
	{
		return serviceName;
	}
}
