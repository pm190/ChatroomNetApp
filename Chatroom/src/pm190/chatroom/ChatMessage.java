package pm190.chatroom;

public class ChatMessage {
	private final String text;
	private final String colourClass;
	private final String roomName;
	/**
	 * @param message
	 * @param from
	 * @param colorClass
	 * @param roomName 
	 */
	public ChatMessage(String text, String colorClass, String roomName) {
		this.text = text;
		this.colourClass = colorClass;
		this.roomName = roomName;
	}
	
	/**
	 * @return the message
	 */
	public String getText() {
		return text;
	}

	public String getColourClass()
	{
		return colourClass;
	}

	/**
	 * @return the roomName
	 */
	public String getRoomName() {
		return roomName;
	}
}
