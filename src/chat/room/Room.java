package chat.room;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class Room 
{
	
	private String roomName;
	public MultiUserChat muc;
	public Chat chatRoom;
	
	
	public Room(MultiUserChat muc, String roomName) 
	{
		this.roomName = roomName;
		this.muc = muc;
	}

	public Room(Chat chat, String participant) 
	{
		this.roomName = participant;
		this.chatRoom = chat;
	}
	
	public boolean equals(Room other) 
	{
		Room otherItem = (Room) other;
		if (roomName.equals(otherItem.roomName))
		{
			return true;
		}
		return false;
	}

	public String getRoomName() {
		return roomName;
	}

	public int compareTo(Room other)
	{
		return  roomName.compareTo(other.getRoomName());
		
	}
	
	
}
