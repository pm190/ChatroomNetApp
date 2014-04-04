package pm190.chatroom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

import pm190.beans.OnlineUsersBean;

/**
 * Manages all user actions with XMPP connection. Maintains lists of all rooms, current rooms. Maintains
 * tab index's from UI and map of listeners to room names.
 * @author pm190
 */
public class UserManager
{
	private final XMPPConnection connection;
	private final String username;
	private final List<String> openRoomNames = new CopyOnWriteArrayList<String>();
	private final List<String> specialRoomNames = new CopyOnWriteArrayList<String>();
	private final List<String> allRoomNames = new CopyOnWriteArrayList<String>();
	private final Map<String, RoomChatListener> listeners = new ConcurrentHashMap<String, RoomChatListener>();
	private volatile int activeRoomTabIndex;
	private volatile int activeNavTabIndex;

	/**
	 * Creates new user manager with a connection and a username
	 * @param connection to XMPP server
	 * @param username of user
	 */
	public UserManager(XMPPConnection connection, String username)
	{
		this.connection = connection;
		this.username = username;
		// TODO read from special pages folder
		specialRoomNames.add("help");
		enterRoom("help"); // start with help tab open
		allRoomNames.addAll(specialRoomNames);

		Collection<HostedRoom> hostedRooms = getHostedRooms();
		for(HostedRoom room : hostedRooms)
		{
			allRoomNames.add(StringUtils.parseName(room.getJid()));
		}
	}

	private Collection<HostedRoom> getHostedRooms()
	{
		Collection<HostedRoom> rooms = new ArrayList<HostedRoom>();
		try
		{
			return MultiUserChat.getHostedRooms(connection, "conference." + ServerPropertyUtils.getServerDomain());
		}
		catch(XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rooms;
	}

	/**
	 * Gets list of all room names, including special room names
	 * Gets new list from server and removes any room locally that is no longer on the server
	 * @return the allRoomNames
	 */
	public List<String> getAllRoomNames()
	{
		Collection<HostedRoom> rooms = getHostedRooms();
		List<String> roomNames = new ArrayList<String>();
		for(HostedRoom room : rooms)
		{
			roomNames.add(StringUtils.parseName(room.getJid()));
		}
		roomNames.addAll(specialRoomNames);
		allRoomNames.clear();
		allRoomNames.addAll(roomNames);
		System.out.println(username +  " rooms: " + allRoomNames);
		return allRoomNames;
	}

	/**
	 * Enter the room, and update tab index
	 * @param roomName to enter
	 */
	public void enterRoom(String roomName)
	{
		if(roomName != null && !roomName.equals(""))
		{
			if(!openRoomNames.contains(roomName))
			{
				openRoomNames.add(roomName);
			}
			activeRoomTabIndex = openRoomNames.indexOf(roomName);
		}
	}

	/**
	 * Leave room, called on tab close from UI, updates tab index
	 * @param event
	 */
	public void leaveRoom(TabCloseEvent e)
	{
		String roomName = e.getTab().getTitle();
		RoomChatListener listener = listeners.get(roomName);
		if(listener != null)
		{
			listener.stopListening();
		}
		int index = openRoomNames.indexOf(roomName);
		openRoomNames.remove(roomName);
		if(activeRoomTabIndex > index)
		{
			activeRoomTabIndex--;
		}
		
	}

	/**
	 * Change room, called from changing room tab in UI, updates index
	 * @param event
	 */
	public void changeRoom(TabChangeEvent e)
	{
		String roomName = e.getTab().getTitle();
		activeRoomTabIndex = openRoomNames.indexOf(roomName);
		System.out.println("Active Tab Index: " + activeRoomTabIndex);
	}

	/**
	 * Checks if room name is a special room
	 * @param roomName of room to check
	 * @return true if room is in special rooms list
	 */
	public boolean isSpecialRoom(String roomName)
	{
		return specialRoomNames.contains(roomName);
	}

	/**
	 * Begin listening to room
	 * @param roomName to listen to
	 */
	public void listen(String roomName)
	{
		System.out.println("listen requested");
		RoomChatListener listener = listeners.get(roomName);
		if(listener != null)
		{
			if(listener.restartListening())
			{
				return;
			}
		}
		MultiUserChat muc = new MultiUserChat(connection, roomName + "@" + ServerPropertyUtils.getServiceName());
		createListener(roomName, muc);
	}

	private void createListener(String roomName, MultiUserChat muc)
	{
		try
		{
			RoomChatListener listener = new RoomChatListener(roomName, username, connection, this, muc);
			listeners.put(roomName, listener);
			Thread t = new Thread(listener);
			t.setDaemon(true);
			t.start();
			System.out.println("joined " + roomName);
		}
		catch(XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Called when listener thread terminates. Updates user that room has been removed.
	 * @param roomName
	 */
	public void leftRoom(String roomName)
	{
		listeners.remove(roomName);
		PushContext pushContext = PushContextFactory.getDefault().getPushContext();
		pushContext.push("/room", "room removed");
	}

	/**
	 * Get room listenr
	 * @param roomName of room being listened to
	 * @return room chat listener of given room, or null if doesn't exist
	 * @throws XMPPException
	 */
	public RoomChatListener getListener(String roomName) throws XMPPException
	{
		return listeners.get(roomName);
	}

	/**
	 * Returns list of messages for a given room
	 * @param roomName of room
	 * @return messages for room name
	 */
	public List<ChatMessage> getMessages(String roomName)
	{
		RoomChatListener listener = listeners.get(roomName);
		if(listener != null)
		{
			return listener.getMessages();
		}
		return Collections.emptyList();
	}

	/**
	 * Gets list of rooms a user is listening in
	 * @return List<String> open rooms
	 */
	public List<String> getOpenRoomNames()
	{
		return openRoomNames;
	}

	/**
	 * Send message to given room
	 * @param roomName of room to send message to
	 * @param message to send
	 */
	public void sendMessage(String roomName, String message)
	{
		RoomChatListener listener = listeners.get(roomName);
		if(listener != null)
		{
			listener.sendMessage(message);
		}
	}

	/**
	 * Get tab index of room tabs
	 * @return
	 */
	public int getActiveRoomTabIndex()
	{
		return activeRoomTabIndex;
	}

	/**
	 * Set tab index for room tabs
	 * @param activeRoomTabIndex to set
	 */
	public void setActiveRoomTabIndex(int activeRoomTabIndex)
	{
		this.activeRoomTabIndex = activeRoomTabIndex;
	}

	/**
	 * Gets tab index for navigation tabs
	 * @return
	 */
	public int getActiveNavTabIndex()
	{
		return activeNavTabIndex;
	}

	/**
	 * Set tab index of navigation tabs
	 * @param activeNavTabIndex
	 */
	public void setActiveNavTabIndex(int activeNavTabIndex)
	{
		this.activeNavTabIndex = activeNavTabIndex;
	}
	
	/**
	 * Gets users in current room, determined by active tab index
	 * @return List<String> users in current room
	 */
	public List<String> getUsersInRoom()
	{
		List<String> users = new ArrayList<String>();
		if(activeRoomTabIndex < openRoomNames.size())
		{
			String roomName = openRoomNames.get(activeRoomTabIndex);
			if(roomName != null)
			{
				RoomChatListener listener = listeners.get(roomName);
				if(listener != null)
				{
					Iterator<String> occupants = listener.getMultiUserChat().getOccupants();
					while(occupants.hasNext())
					{
						users.add(StringUtils.parseResource(occupants.next()));
					}
				}
			}
		}
		return users;
	}

	/**
	 * Change navigation tab, update nav tab index
	 * @param event
	 */
	public void changeNavTab(TabChangeEvent e)
	{
		TabView tabView = (TabView) e.getComponent();
		activeNavTabIndex = tabView.getChildren().indexOf(e.getTab());
		System.out.println("Nav tab index: " + activeNavTabIndex);
	}

	/**
	 * Gets list of friends. Compares roster with online user list, add all offline users to end of friend
	 * list
	 * @return List<Friend> friends
	 */
	public List<Friend> getFriends()
	{
		List<Friend> friends = new ArrayList<Friend>();
		List<Friend> offlineFriends = new ArrayList<Friend>();
		Roster roster = connection.getRoster();
		Collection<RosterEntry> entries = roster.getEntries();
		for(RosterEntry entry : entries)
		{
			String user = entry.getUser();
			if(OnlineUsersBean.contains(user))
			{
				friends.add(new Friend(user, false));
			}
			else
			{
				offlineFriends.add(new Friend(user, true));
			}
		}
		friends.addAll(offlineFriends);
		return friends;
	}
	
	/**
	 * Add a friend to users friend list, updates roster on XMPP server
	 * @param username
	 */
	public void addFriend(String username)
	{
		System.out.println("Attempt add friend");
		Roster roster = connection.getRoster();
		try
		{
			roster.createEntry(username, username, null);
			System.out.println("Added friend " + username);
		}
		catch(XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create a new room, if not already created. Joins and begins listening to room. Pushs down room
	 * channel to notify other users of new room
	 * @param roomName to create
	 */
	public void createRoom(String roomName)
	{
		for(HostedRoom room : getHostedRooms())
		{
			if(StringUtils.parseName(room.getJid()).equals(roomName))
			{
				return;
			}
		}
		try
		{
			MultiUserChat muc = new MultiUserChat(connection, roomName + "@" + ServerPropertyUtils.getServiceName());
			muc.create(username);
			muc.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
			createListener(roomName, muc);
			allRoomNames.add(roomName);
			openRoomNames.add(roomName);
			activeRoomTabIndex = openRoomNames.indexOf(roomName);
			PushContext pushContext = PushContextFactory.getDefault().getPushContext();
			pushContext.push("/room", "room added");
		}
		catch(XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
