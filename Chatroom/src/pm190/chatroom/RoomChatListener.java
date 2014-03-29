package pm190.chatroom;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

/**
 * 
 * @author Patrick Mackinder
 */
public class RoomChatListener implements Runnable
{
	private static final long TIMEOUT = 5000; // so we wake up periodically to
												// check if user is still in
												// room
	private final MultiUserChat muc;
	private final String roomChannel;
	private volatile boolean inRoom = true;

	public RoomChatListener(XMPPConnection connection, String room)
	{
		muc = new MultiUserChat(connection, room);
		roomChannel = "room/" + room;
	}

	@Override
	public void run()
	{
		while(true)
		{
			Message msg = muc.nextMessage(TIMEOUT);
			if(!inRoom)
			{
				break;
			}
			if(msg != null)
			{
				// send message to UI
				PushContext pushContext = PushContextFactory.getDefault().getPushContext();
				pushContext.push(roomChannel, StringUtils.parseResource(msg.getFrom()) + ": " + msg.getBody());
			}
		}
	}

	public void leaveRoom()
	{
		inRoom = false;
	}
}
