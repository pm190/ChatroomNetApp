package pm190.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.util.StringUtils;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import pm190.chatroom.ChatMessage;
import pm190.chatroom.ServerPropertyUtils;
import pm190.chatroom.UserManager;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class UserBean
{
	private static final int PACKET_REPLY_TIMEOUT = 500; // millis
	private volatile String username = "";
	private volatile String password = "";
	private volatile boolean guest = false;
	private volatile XMPPConnection connection;
	private volatile UserManager userManager;

	public UserBean() throws XMPPException
	{
		SmackConfiguration.setPacketReplyTimeout(PACKET_REPLY_TIMEOUT);
		ConnectionConfiguration config = new ConnectionConfiguration(ServerPropertyUtils.getServerAddress(), ServerPropertyUtils.getServerPort());
		config.setSecurityMode(SecurityMode.disabled);
		connection = new XMPPConnection(config);
		connection.connect();
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
	
	public UserManager getUserManager()
	{
		return userManager;
	}

	public String login()
	{
		System.out.println("attempt login");
		try
		{
			if(guest)
			{
				connection.loginAnonymously();
				username = StringUtils.parseResource(connection.getUser());
			}
			else
			{
				connection.login(username, password);
			}
			userManager = new UserManager(connection, username);
		}
		catch(XMPPException e)
		{
			// TODO return some login error
			return "";
		}
		return "enterChat";
	}
	
	public String register(RegisterBean registerBean)
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
			//TODO return error, probably username already exists
			return "";
		}
	}
	
	//TODO call this on session expire
	public void expire()
	{
		connection.disconnect();
	}
	
	//TODO call this on user logout
	public void logout()
	{
		//TODO figure out how to logout, possible just establish fresh connection
	}
	
	public List<String> getAllRoomNames()
	{
		return userManager.getAllRoomNames();
	}

	public void enterRoom(String roomName)
	{
		userManager.enterRoom(roomName);
	}

	public void leaveRoom(TabCloseEvent e)
	{
		userManager.leaveRoom(e);
	}

	public void changeRoom(TabChangeEvent e)
	{
		userManager.changeRoom(e);
	}

	public boolean isSpecialRoom(String roomName)
	{
		return userManager.isSpecialRoom(roomName);
	}

	public void listen(String roomName)
	{
		userManager.listen(roomName);
	}

	public List<ChatMessage> getMessages(String roomName)
	{
		return userManager.getMessages(roomName);
	}

	public List<String> getOpenRoomNames()
	{
		return userManager.getOpenRoomNames();
	}

	public void sendMessage(String roomName, String message)
	{
		userManager.sendMessage(roomName, message);
	}

	public int getActiveRoomTabIndex()
	{
		return userManager.getActiveRoomTabIndex();
	}

	public void setActiveRoomTabIndex(int activeRoomTabIndex)
	{
		userManager.setActiveRoomTabIndex(activeRoomTabIndex);
	}

	public int getActiveNavTabIndex()
	{
		return userManager.getActiveNavTabIndex();
	}

	public void setActiveNavTabIndex(int activeNavTabIndex)
	{
		userManager.setActiveNavTabIndex(activeNavTabIndex);
	}

	public List<String> getUsersInRoom()
	{
		return userManager.getUsersInRoom();
	}

	public void changeNavTab(TabChangeEvent e)
	{
		userManager.changeNavTab(e);
	}
	
	public List<String> getFriends()
	{
		return userManager.getFriends();
	}
	
	public void createRoom(String roomName)
	{
		userManager.createRoom(roomName);
	}
}
