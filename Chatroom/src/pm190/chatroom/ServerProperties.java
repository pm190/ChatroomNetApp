package pm190.chatroom;

/**
 * 
 * @author Patrick Mackinder
 */
public class ServerProperties
{
	private static final String serverAddress = "92.236.126.119";
	private static final int serverPort = 5222;
	private static final String serverDomain = "olympus";
	private static final String serviceName = "conference.olympus";

	public static String getServeraddress()
	{
		return serverAddress;
	}

	public static int getServerport()
	{
		return serverPort;
	}

	public static String getServerdomain()
	{
		return serverDomain;
	}

	public static String getServicename()
	{
		return serviceName;
	}
}
