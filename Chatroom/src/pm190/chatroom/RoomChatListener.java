package pm190.chatroom;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
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

	public RoomChatListener(XMPPConnection connection, String room, String username) throws XMPPException
	{
		muc = new MultiUserChat(connection, room + "@" + ServerProperties.getServicename());
		muc.join(username);
		roomChannel = "room/" + room;
		
		//TODO throwout old msgs
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
			if(msg != null && msg.getBody() != null)
			{
				// send message to UI
				System.out.println(msg.getBody());
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
