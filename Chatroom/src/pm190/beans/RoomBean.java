package pm190.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Represents a new room to be created. Session scoped and can be reused
 * be reused by calling reset()
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class RoomBean
{
	private String roomName;
	
	/**
	 * Creates new room bean, created on new session
	 */
	public RoomBean()
	{
	}

	/**
	 * Gets current room name
	 * @return roomName
	 */
	public String getRoomName()
	{
		return roomName;
	}

	/**
	 * Set current room name
	 * @param roomName
	 */
	public void setRoomName(String roomName)
	{
		this.roomName = roomName;
	}
	
	/**
	 * Sets roomName to empty string
	 */
	public void reset()
	{
		this.roomName = "";
	}
}
