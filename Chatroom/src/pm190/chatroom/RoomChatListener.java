package pm190.chatroom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

/**
 * Responsible for sending a receiving messages to a room. This class should be created per user
 * per room.
 * @author pm190
 */
public class RoomChatListener implements Runnable
{
	private static final long TIMEOUT = 5000;
	private final MultiUserChat muc;
	private final String roomName;
	private volatile boolean inRoom = true;
	private volatile boolean finished = false;
	private final List<ChatMessage> messages = new ArrayList<ChatMessage>();
	private final UserManager userManager;
	private final String user;
	
	/**
	 * Create a new room listener, join the multi user chat if needed, add room event listener
	 * @param roomName of room to listen on
	 * @param user name of listening user
	 * @param connection to the XMPP server
	 * @param userManager manager object to call back when left room
	 * @param muc multi user chat object to get rooms
	 * @throws XMPPException
	 */
	public RoomChatListener(String roomName, String user, XMPPConnection connection, UserManager userManager, MultiUserChat muc) throws XMPPException
	{
		this.roomName = roomName;
		this.userManager = userManager;
		this.user = user;
		this.muc = muc;
		DiscussionHistory history = new DiscussionHistory();
		history.setSince(new Date());
		if(!muc.isJoined())
		{
			muc.join(user, null, history, 5000);
		}
		muc.addParticipantListener(new RoomEventListener(roomName, user, messages));
	}

	/**
	 * Begin listening to room chat, send messages to user channel and add them to message lists
	 */
	@Override
	public void run()
	{
		while(inRoom)
		{
			Message msg = muc.nextMessage(TIMEOUT);
			if(msg != null && msg.getBody() != null && msg.getFrom() != null)
			{
				String from = StringUtils.parseResource(msg.getFrom());
				if(!from.equals(""))
				{
					ChatMessage chatMessage = new ChatMessage(from + ": " + msg.getBody(), (from.equals(user) ? "self" : "other"), roomName);
					messages.add(chatMessage);
					PushContext pushContext = PushContextFactory.getDefault().getPushContext();
					pushContext.push("/message/"+user, chatMessage);
					System.out.println("Current User=" + user + " from=" + from + " msg=" + msg.getBody() + " class=" + chatMessage.getColourClass() + " this=" + this + " pc=" + pushContext);
				}
			}
		}
		finished = true;
		muc.leave();
		userManager.leftRoom(roomName);
	}

	/**
	 * stop listening to room
	 */
	public void stopListening()
	{
		inRoom = false;
	}

	/**
	 * restart listening to room
	 * @return true if able to restart
	 */
	public boolean restartListening()
	{
		if(!finished)
		{
			inRoom = true;
			return true;
		}
		return false;
	}

	/**
	 * return list of room messages
	 * @return List<ChatMessage> messages
	 */
	public List<ChatMessage> getMessages()
	{
		return messages;
	}

	/**
	 * Send message to room
	 * @param message to send
	 */
	public void sendMessage(String message)
	{
		if(!finished && inRoom)
		{
			try
			{
				muc.sendMessage(message);
			}
			catch(XMPPException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets multi user chat object user be listener
	 * @return multi user chat
	 */
	public MultiUserChat getMultiUserChat()
	{
		return muc;
	}
}
