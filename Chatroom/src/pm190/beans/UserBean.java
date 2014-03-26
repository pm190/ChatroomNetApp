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
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

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
	private String currentRoom;
	private int currentRoomIndex = -1;
	private List<String> messages;
	
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
	
	public void sendMessage(String message, String roomName)
	{
		user.sendMessageToRoom(message, roomName);
	}

	public String getCurrentRoom()
	{
		return currentRoom;
	}
	
	public void setCurrentRoom(String currentRoom)
	{
		this.currentRoom = currentRoom;
		messages = new ArrayList<String>();
	}

	public void roomTabChange(TabChangeEvent event)
	{
		setCurrentRoom(event.getTab().getTitle());
		currentRoomIndex = ((TabView)event.getComponent()).getChildren().indexOf(event.getTab());
	}

	public int getCurrentRoomIndex()
	{
		return currentRoomIndex;
	}

	public void setCurrentRoomIndex(int currentRoomIndex)
	{
		this.currentRoomIndex = currentRoomIndex;
	}
	
	public void incIndex()
	{
		currentRoomIndex++;
	}
	
	public void decIndex()
	{
		currentRoomIndex--;
	}
	
	public List<String> getRoomMessages()
	{
		String msg = getMessage();
		while(msg != null)
		{
			messages.add(msg);
			msg = getMessage();
		}
		return messages;
	}
	
	private String getMessage()
	{
		String msg = null;
		if(currentRoom != null)
		{
			msg = user.nextMessage(currentRoom);
		}
		return msg;
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
	
	public void roomTabClose(TabCloseEvent event) 
	{
		//TODO change current room and current room index
		user.getMultiUserChat(currentRoom).leave();
    }
}
