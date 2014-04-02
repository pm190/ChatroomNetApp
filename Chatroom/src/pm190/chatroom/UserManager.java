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
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

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

	public void changeRoom(TabChangeEvent e)
	{
		String roomName = e.getTab().getTitle();
		activeRoomTabIndex = openRoomNames.indexOf(roomName);
		System.out.println("Active Tab Index: " + activeRoomTabIndex);
	}

	public boolean isSpecialRoom(String roomName)
	{
		return specialRoomNames.contains(roomName);
	}

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

	public void leftRoom(String roomName)
	{
		listeners.remove(roomName);
	}

	public RoomChatListener getListener(String roomName) throws XMPPException
	{
		return listeners.get(roomName);
	}

	public List<ChatMessage> getMessages(String roomName)
	{
		RoomChatListener listener = listeners.get(roomName);
		if(listener != null)
		{
			return listener.getMessages();
		}
		return Collections.emptyList();
	}

	public List<String> getOpenRoomNames()
	{
		return openRoomNames;
	}

	public void sendMessage(String roomName, String message)
	{
		RoomChatListener listener = listeners.get(roomName);
		if(listener != null)
		{
			listener.sendMessage(message);
		}
	}

	public int getActiveRoomTabIndex()
	{
		return activeRoomTabIndex;
	}

	public void setActiveRoomTabIndex(int activeRoomTabIndex)
	{
		this.activeRoomTabIndex = activeRoomTabIndex;
	}

	public int getActiveNavTabIndex()
	{
		return activeNavTabIndex;
	}

	public void setActiveNavTabIndex(int activeNavTabIndex)
	{
		this.activeNavTabIndex = activeNavTabIndex;
	}

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

	public void changeNavTab(TabChangeEvent e)
	{
		TabView tabView = (TabView) e.getComponent();
		activeNavTabIndex = tabView.getChildren().indexOf(e.getTab());
		System.out.println("Nav tab index: " + activeNavTabIndex);
	}

	public List<String> getFriends()
	{
		List<String> friends = new ArrayList<String>();
		Roster roster = connection.getRoster();
		Collection<RosterEntry> entries = roster.getEntries();
		for(RosterEntry entry : entries)
		{
			if(roster.getPresence(entry.getUser()).getMode() == Presence.Mode.available)
			{
				friends.add(entry.getUser());
			}
		}
		return friends;
	}

	/**
	 * @param roomName
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
