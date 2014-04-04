package pm190.chatroom;

/**
 * Chat message represents a message sent by a user. A message contain a text, the CSS class
 * that decides the colour and the roomName to go to.
 * @author pm190
 */
public class ChatMessage 
{
	private final String text;
	private final String colourClass;
	private final String roomName;

	/**
	 * Creates a room chat with a message text, CSS Colour class and roomName
	 * @param text
	 * @param colorClass
	 * @param roomName
	 */
	public ChatMessage(String text, String colorClass, String roomName) 
	{
		this.text = text;
		this.colourClass = colorClass;
		this.roomName = roomName;
	}
	
	/**
	 * Gets the message text
	 * @return the message
	 */
	public String getText() 
	{
		return text;
	}

	/**
	 * Gets the CSS Colour class
	 * @return colourClass
	 */
	public String getColourClass()
	{
		return colourClass;
	}

	/**
	 * Gets the room name
	 * @return the roomName
	 */
	public String getRoomName() 
	{
		return roomName;
	}
}
