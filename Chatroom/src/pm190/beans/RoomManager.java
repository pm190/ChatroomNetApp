package pm190.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import pm190.chatroom.User;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class RoomManager
{
	public void joinRoom(User user, String name)
	{
		user.joinRoom(name);
	}
}
