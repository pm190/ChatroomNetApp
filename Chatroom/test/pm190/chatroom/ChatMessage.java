package pm190.chatroom;

public class ChatMessage {
	private final String text;
	private final String from;
	private final String colour;
	private final String roomName;
	/**
	 * @param message
	 * @param from
	 * @param colour
	 * @param roomName 
	 */
	public ChatMessage(String message, String from, String colour, String roomName) {
		this.text = message;
		this.from = from;
		this.colour = colour;
		this.roomName = roomName;
	}
	
	/**
	 * @return the message
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}
	
	/**
	 * @return the colour
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * @return the roomName
	 */
	public String getRoomName() {
		return roomName;
	}
}
