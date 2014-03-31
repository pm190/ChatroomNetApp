package pm190.chatroom;

public class ChatMessage {
	private final String text;
	private final String colour;
	private final String roomName;
	/**
	 * @param message
	 * @param from
	 * @param colour
	 * @param roomName 
	 */
	public ChatMessage(String text, String colour, String roomName) {
		this.text = text;
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
