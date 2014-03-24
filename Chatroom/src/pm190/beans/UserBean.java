package pm190.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jivesoftware.smack.XMPPConnection;

import pm190.chatroom.User;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class UserBean
{
	private String username = "";
	private String password = "";
	private boolean guest = false;
	private User user;
	
	public UserBean()
	{
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isGuest()
	{
		return guest;
	}

	public void setGuest(boolean guest)
	{
		this.guest = guest;
	}

	public User getUser()
	{
		return user;
	}
	
	public String login(XMPPConnection connection)
	{
		if(!guest)
		{
			//TODO get login exception
			User user = new User(connection, username, password);
			this.user = user;
		}
		else
		{
			User user = new User(connection);
			this.user = user;
		}
		return "enterChat";
	}
	
	public void sendMessage(String message)
	{
		
	}
}
