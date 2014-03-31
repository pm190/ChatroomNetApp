package pm190.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.TabCloseEvent;

import pm190.chatroom.ChatMessage;
import pm190.chatroom.RoomChatListenerUITest;

@ManagedBean
@SessionScoped
public class RoomManagerBean {

	private final List<String> openRoomNames = new ArrayList<String>();
	private final List<String> specialRoomNames = new ArrayList<String>();
	private final List<String> allRoomNames = new ArrayList<String>();
	private final Map<String, RoomChatListenerUITest> listeners = new ConcurrentHashMap<String, RoomChatListenerUITest>();
	private volatile int activeRoomTabIndex;
	private volatile int activeNavTabIndex;
	
	public RoomManagerBean() {
		//TODO read from special pages folder
		specialRoomNames.add("help");
		enterRoom("help");
		allRoomNames.addAll(specialRoomNames);
		allRoomNames.add("lobby");
		allRoomNames.add("room101");
	}

	/**
	 * @return the allRoomNames
	 */
	public List<String> getAllRoomNames() {
		return allRoomNames;
	}

	public void enterRoom(String roomName) {
		if(roomName != null && !roomName.equals(""))
		{
			if(!openRoomNames.contains(roomName))
			{
				openRoomNames.add(roomName);
			}
			activeRoomTabIndex = openRoomNames.indexOf(roomName);
		}
	}

	public void leaveRoom(TabCloseEvent e) {
		String roomName = e.getTab().getTitle();
		RoomChatListenerUITest listener = listeners.get(roomName);
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
	
	public void changeRoom(TabChangeEvent e) {
		String roomName = e.getTab().getTitle();
		activeRoomTabIndex = openRoomNames.indexOf(roomName);
		System.out.println("Active Tab Index: " + activeRoomTabIndex);
	}

	public boolean isSpecialRoom(String roomName)
	{
		return specialRoomNames.contains(roomName);
	}
	
	public void listen(String roomName) {
		System.out.println("listen requested");
		try {
			RoomChatListenerUITest listener = listeners.get(roomName);
			if (listener != null) {
				if (listener.restartListening()) {
					return;
				}
			}
			listener = new RoomChatListenerUITest(roomName, "testListener", this);
			listeners.put(roomName, listener);
			Thread t = new Thread(listener);
			t.setDaemon(true);
			t.start();
			System.out.println("joined " + roomName);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void leftRoom(String roomName)
	{
		listeners.remove(roomName);
	}

	public RoomChatListenerUITest getListener(String roomName) throws XMPPException {
		return listeners.get(roomName);
	}

	public List<ChatMessage> getMessages(String roomName) {
		RoomChatListenerUITest listener = listeners.get(roomName);
		if(listener != null)
		{
			return listener.getMessages();
		}
		return Collections.emptyList();
	}
	
	public List<String> getOpenRoomNames() {
		return openRoomNames;
	}
	
	public void sendMessage(String roomName, String message)
	{
		RoomChatListenerUITest listener = listeners.get(roomName);
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

	public List<String> getUsersInRoom() {
		List<String> users = new ArrayList<String>();
		if(activeRoomTabIndex < openRoomNames.size())
		{
			String roomName = openRoomNames.get(activeRoomTabIndex);
			if(roomName != null)
			{
				RoomChatListenerUITest listener = listeners.get(roomName);
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
}