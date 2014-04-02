package pm190.beans;

import java.util.HashSet;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@ApplicationScoped
public class OnlineUsersBean
{
	private static final Set<String> users = new HashSet<String>();
	
	public static void addUser(String user)
	{
		users.add(user);
	}
	
	public static void removeUser(String user)
	{
		users.remove(user);
	}
	
	public static Set<String> getUsers()
	{
		return users;
	}
	
	public static boolean contains(String user)
	{
		return users.contains(user);
	}
}
