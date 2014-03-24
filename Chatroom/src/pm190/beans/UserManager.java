package pm190.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import pm190.chatroom.User;

/**
 * 
 * @author Patrick Mackinder
 */
@ManagedBean
@ApplicationScoped
public class UserManager
{
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
