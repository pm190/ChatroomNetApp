package pm190.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.util.StringUtils;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import pm190.chatroom.ChatMessage;
import pm190.chatroom.Friend;
import pm190.chatroom.ServerPropertyUtils;
import pm190.chatroom.UserManager;

/**
 * UserBean represents a user session. It is responsible for all actions a
 * user may need to make. It contains the XMPP connection to the XMPP server
 * which it users for most actions. It is session scoped and so one instance 
 * exists for each client session
 * @author Patrick Mackinder
 */
@ManagedBean
@SessionScoped
public class UserBean implements HttpSessionListener
{
	private static final int PACKET_REPLY_TIMEOUT = 500; // millis
	private volatile String username = "";
	private volatile String password = "";
	private volatile boolean guest = false;
	private volatile XMPPConnection connection;
	private volatile UserManager userManager;

	/**
	 * Creates new UserBean, created on new session. Establishes connection to XMPP server,
	 * or throws exception if cannot connect as without the connection the object cannot behave
	 * as intended.
	 * @throws XMPPException
	 */
	public UserBean() throws XMPPException
	{
		SmackConfiguration.setPacketReplyTimeout(PACKET_REPLY_TIMEOUT);
		ConnectionConfiguration config = new ConnectionConfiguration(ServerPropertyUtils.getServerAddress(), ServerPropertyUtils.getServerPort());
		config.setSecurityMode(SecurityMode.disabled);
		connection = new XMPPConnection(config);
		connection.connect();
	}

	/**
	 * Gets current username
	 * @return username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * Set current username
	 * @param username
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * Gets current password
	 * @return
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * Set current password
	 * @param password
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * Gets guest status
	 * @return guest
	 */
	public boolean isGuest()
	{
		return guest;
	}

	/**
	 * Set current guest status
	 * @param guest=true
	 */
	public void setGuest(boolean guest)
	{
		this.guest = guest;
	}
	
	/**
	 * Gets current user manager
	 * @return
	 */
	public UserManager getUserManager()
	{
		return userManager;
	}

	/**
	 * Performs login to XMPP server through connection.
	 * If guest it logs in anonymously, if not guest it logs in
	 * as member using username and password. Then pushes a message
	 * down the user channel to notify all other users that they have
	 * logged in, which causes a page refresh. Also adds user to
	 * set of online users in OnlineUsersBean.
	 * @return navigation action, handled by faces servlet
	 */
	public String login()
	{
		System.out.println("attempt login");
		//TODO login as different user
		System.out.println("Anon: " + connection.isAnonymous());
		System.out.println("Auth: " + connection.isAuthenticated());
		if(!(connection.isAnonymous() || connection.isAuthenticated()))
		{
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
				PushContext pushContext = PushContextFactory.getDefault().getPushContext();
				pushContext.push("/user", "user logged in");
				userManager = new UserManager(connection, username);
				OnlineUsersBean.addUser(username);
			}
			catch(XMPPException e)
			{
				// TODO return some login error
				return "";
			}
		}
		return "enterChat";
	}
	
	/**
	 * Creates a new account using fields from register bean.
	 * Currently only uses username and password.
	 * @param registerBean
	 * @return navigation action, handled by faces servlet
	 */
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

	/**
	 * Disconnects XMPP connection, and removes user from online user set.
	 * Also pushes down user channel to notify other users of this logout
	 */
	public void expire()
	{
		connection.disconnect();
		OnlineUsersBean.removeUser(username);
		PushContext pushContext = PushContextFactory.getDefault().getPushContext();
		pushContext.push("/user", "user logged out");
	}

	/**
	 * Logout session by expiring current session, then restablishing a new connection
	 * in case user wishes to log in again.
	 * @return navigation action, handled by faces servlet
	 * @throws XMPPException
	 */
	public String logout() throws XMPPException
	{
		expire();
		ConnectionConfiguration config = new ConnectionConfiguration(ServerPropertyUtils.getServerAddress(), ServerPropertyUtils.getServerPort());
		config.setSecurityMode(SecurityMode.disabled);
		connection = new XMPPConnection(config); //create and connect new sesson incase user wants to log back in
		connection.connect();
		return "logout";
	}
	
	/**
	 * Convenience method to UserManger
	 * @return List<String> roomnames
	 */
	public List<String> getAllRoomNames()
	{
		return userManager.getAllRoomNames();
	}

	/**
	 * Convenience method to UserManger
	 * @param roomName
	 */
	public void enterRoom(String roomName)
	{
		userManager.enterRoom(roomName);
	}

	/**
	 * Convenience method to UserManger
	 * @param event
	 */
	public void leaveRoom(TabCloseEvent e)
	{
		userManager.leaveRoom(e);
	}

	/**
	 * Convenience method to UserManger
	 * @param event
	 */
	public void changeRoom(TabChangeEvent e)
	{
		userManager.changeRoom(e);
	}

	/**
	 * Convenience method to UserManger
	 * @param roomName
	 * @return true if special room\
	 */
	public boolean isSpecialRoom(String roomName)
	{
		return userManager.isSpecialRoom(roomName);
	}

	/**
	 * Convenience method to UserManger
	 * @param roomName
	 */
	public void listen(String roomName)
	{
		userManager.listen(roomName);
	}

	/**
	 * Convenience method to UserManger
	 * @param roomName
	 * @return List<ChatMessage> of messages
	 */
	public List<ChatMessage> getMessages(String roomName)
	{
		return userManager.getMessages(roomName);
	}

	/**
	 * Convenience method to UserManger
	 * @return List<String> names of open rooms
	 */
	public List<String> getOpenRoomNames()
	{
		return userManager.getOpenRoomNames();
	}

	/**
	 * Convenience method to UserManger
	 * @param roomName to send message to
	 * @param message to send
	 */
	public void sendMessage(String roomName, String message)
	{
		userManager.sendMessage(roomName, message);
	}

	/**
	 * Convenience method to UserManger
	 * @return current room tab index
	 */
	public int getActiveRoomTabIndex()
	{
		return userManager.getActiveRoomTabIndex();
	}

	/**
	 * Convenience method to UserManger
	 * @param activeRoomTabIndex of room tabs
	 */
	public void setActiveRoomTabIndex(int activeRoomTabIndex)
	{
		userManager.setActiveRoomTabIndex(activeRoomTabIndex);
	}

	/**
	 * Convenience method to UserManger
	 * @return current navigation tab index
	 */
	public int getActiveNavTabIndex()
	{
		return userManager.getActiveNavTabIndex();
	}

	/**
	 * Convenience method to UserManger
	 * @param activeNavTabIndex of navigation tabs
	 */
	public void setActiveNavTabIndex(int activeNavTabIndex)
	{
		userManager.setActiveNavTabIndex(activeNavTabIndex);
	}

	/**
	 * Convenience method to UserManger
	 * @return List<String> users in current room
	 */
	public List<String> getUsersInRoom()
	{
		return userManager.getUsersInRoom();
	}

	/**
	 * Convenience method to UserManger
	 * @param event
	 */
	public void changeNavTab(TabChangeEvent e)
	{
		userManager.changeNavTab(e);
	}
	
	/**
	 * Convenience method to UserManger
	 * @return List<Friend> friends of user
	 */
	public List<Friend> getFriends()
	{
		return userManager.getFriends();
	}
	
	/**
	 * Convenience method to UserManger
	 * @param username of friend to add
	 */
	public void addFriend(String username)
	{
		userManager.addFriend(username);
	}
	
	/**
	 * Convenience method to UserManger
	 * @param roomName to create
	 */
	public void createRoom(String roomName)
	{
		userManager.createRoom(roomName);
	}
	
	/**
	 * Convenience method to UserManger
	 * @return true if user is logged in either as guest or anonymously
	 */
	public boolean isLoggedIn()
	{
		return (connection.isAnonymous() || connection.isAuthenticated());
	}

	/**
	 * Called when session created, not used
	 * @param event
	 */
	@Override
	public void sessionCreated(HttpSessionEvent event)
	{
	}

	/**
	 * Called when session is destroyed. Session should expire
	 * @param event
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event)
	{
		System.out.println("Session Destroyed");
		expire();
	}
}
