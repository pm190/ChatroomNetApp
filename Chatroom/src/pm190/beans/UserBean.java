package pm190.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import pm190.chatroom.RoomManagerFactory;
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
	private final List<String> rooms = new ArrayList<String>();
	private String currentRoom;
	
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
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	public String login(XMPPConnection connection)
	{
		try
		{
			if(!guest)
			{
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
		catch(XMPPException e)
		{
			//TODO do something with exception
			return "";
		}
	}
	
	public void sendMessage(String message)
	{
		user.sendMessageToRoom(message, currentRoom);
	}

	public String getCurrentRoom()
	{
		return currentRoom;
	}

	public void roomTabChange(TabChangeEvent event)
	{
		String tab = (String) event.getData();
		if(!tab.equals("help"))
		{
			currentRoom = tab;
		}
	}
	
	public void roomTabClose(TabCloseEvent event) 
	{
		String tab = (String) event.getData();
		if(!tab.equals("help"))
		{
			if(tab.equals(currentRoom))
			{
				user.getMultiUserChat(currentRoom).leave();
				RoomManagerFactory.getInstance().leaveRoom(user, currentRoom);
				int indx = rooms.indexOf(currentRoom);
				rooms.remove(indx);
				int size = rooms.size();
				if(size == 0)
				{
					currentRoom = "";
				}
				else
				{
					if(indx == size-1)
					{
						currentRoom = rooms.get(size-1);
					}
				}
			}
			else
			{
				user.getMultiUserChat(tab).leave();
				RoomManagerFactory.getInstance().leaveRoom(user, tab);
				rooms.remove(tab);
			}
		}
    }
	
	public void joinRoom(String roomName)
	{
		rooms.add(roomName);
		currentRoom = roomName;
	}
	
	public String registerNewUser(XMPPConnection connection, RegisterBean registerBean)
	{
		try
		{
			AccountManager accountManager = new AccountManager(connection);
			accountManager.createAccount(registerBean.getUsername(), registerBean.getPassword());
			//TODO Attributes
			return "home";
		}
		catch(XMPPException e)
		{
			FacesContext.getCurrentInstance().addMessage("username", new FacesMessage("Account already exists"));
			return "";
		}
	}
	
	public List<String> getFriends()
	{
		List<String> friends = new ArrayList<String>();
		if(user != null)
		{
			Roster roster = user.getConnection().getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for(RosterEntry entry : entries)
			{
				friends.add(entry.getUser());
			}
		}
		return friends;
	}
}
