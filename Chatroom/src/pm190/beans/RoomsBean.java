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
	
	public List<String> getAllUsersInRooms(String roomName, User user)
	{
		return roomManager.getAllUsersInRoom(roomName, user);
	}
	
	public void joinRoom(User user, String name)
	{
		roomManager.joinRoom(user, name);
	}
}
