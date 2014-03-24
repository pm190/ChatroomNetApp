package pm190.chatroom;

/**
 * 
 * @author Patrick Mackinder
 */
public class RoomManagerFactory
{
	private static final RoomManager roomManager = new RoomManager();
	
	public static RoomManager getInstance()
	{
		return roomManager;
	}
}
