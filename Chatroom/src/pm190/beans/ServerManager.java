package pm190.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;

import pm190.chatroom.ServerProperties;
import pm190.chatroom.User;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@ApplicationScoped
public class ServerManager
{
	public List<HostedRoom> getRooms(User user)
	{
		List<HostedRoom> rooms = new ArrayList<HostedRoom>();
		try
		{
			Collection<HostedRoom> hostedRooms = MultiUserChat.getHostedRooms(user.getConnection(), "conference." + ServerProperties.getServerdomain());
			for(HostedRoom room : hostedRooms)
			{
				rooms.add(room);
			}
		}
		catch(XMPPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rooms;
	}
	
	public List<String> getJoinedRooms(User user)
	{
		List<String> rooms = new ArrayList<String>();
		Iterator<String> joinedRooms = MultiUserChat.getJoinedRooms(user.getConnection(), user.getUsername() + "@olympus");
		while(joinedRooms.hasNext())
		{
			rooms.add(joinedRooms.next());
		}
		return rooms;
	}
	
	public List<String> getFriends(User user)
	{
		List<String> friends = new ArrayList<String>();
		Roster roster = user.getConnection().getRoster();
		Collection<RosterEntry> entries = roster.getEntries();
		for(RosterEntry entry : entries)
		{
			friends.add(entry.getUser());
		}
		return friends;
	}
}
