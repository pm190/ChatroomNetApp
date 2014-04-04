package pm190.beans;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * OnlineUserBean is application scoped and contains the set of users
 * that have logged in through the application. It does not contain users
 * from other XMPP clients. Users are stored as set of usernames.
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@ApplicationScoped
public class OnlineUsersBean
{
	private static final Set<String> users = new CopyOnWriteArraySet<String>();
	
	/**
	 * Add user to set
	 * @param user
	 */
	public static void addUser(String user)
	{
		users.add(user);
	}
	
	/**
	 * Remove user from set
	 * @param user
	 */
	public static void removeUser(String user)
	{
		users.remove(user);
	}
	
	/**
	 * Returns the set of online users
	 * @return users
	 */
	public static Set<String> getUsers()
	{
		return users;
	}
	
	/**
	 * Returns true if user in set of users
	 * @param user
	 * @return boolean
	 */
	public static boolean contains(String user)
	{
		return users.contains(user);
	}
}
