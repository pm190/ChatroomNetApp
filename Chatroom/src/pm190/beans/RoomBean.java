package pm190.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class RoomBean
{
	private String roomName;
	
	public RoomBean()
	{
	}

	public String getRoomName()
	{
		return roomName;
	}

	public void setRoomName(String roomName)
	{
		this.roomName = roomName;
	}
}
