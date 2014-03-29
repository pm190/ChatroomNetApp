package pm190.beans;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import pm190.chatroom.RoomManager;
import pm190.chatroom.RoomManagerFactory;
import pm190.chatroom.User;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@ApplicationScoped
public class RoomsBean
{
	private final RoomManager roomManager;
	
	public RoomsBean()
	{
		roomManager = RoomManagerFactory.getInstance();
	}
	
	public List<String> getAllRooms()
	{
		return roomManager.getAllRooms();
	}
	
	public List<String> getAllUsersInRoom(String roomName, User user)
	{
		return roomManager.getAllUsersInRoom(roomName, user);
	}
	
	public void joinRoom(UserBean userBean, String roomName)
	{
		userBean.joinRoom(roomName);
		roomManager.joinRoom(userBean.getUser(), roomName);
	}
	
	public void leaveRoom(User user, String roomName)
	{
		roomManager.leaveRoom(user, roomName);
	}
}
