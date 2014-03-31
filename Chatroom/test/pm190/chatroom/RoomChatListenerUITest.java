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

import pm190.beans.ConnectionBean;
import pm190.beans.RoomManagerBean;

public class RoomChatListenerUITest implements Runnable
{
	private static final long TIMEOUT = 5000;
	private final MultiUserChat muc;
	private final String roomName;
	private volatile boolean inRoom = true;
	private volatile boolean finished = false;
	private final List<ChatMessage> messages = new ArrayList<ChatMessage>();
	private final RoomManagerBean roomManagerBean;
	private final String user;

	public RoomChatListenerUITest(String roomName, String user,
			RoomManagerBean roomManagerBean) throws XMPPException
	{
		this.roomName = roomName;
		this.roomManagerBean = roomManagerBean;
		this.user = user;
		XMPPConnection connection = new ConnectionBean().getConnection();
		connection.loginAnonymously();
		muc = new MultiUserChat(connection, roomName + "@" + ServerProperties.getServicename());
		DiscussionHistory history = new DiscussionHistory();
		history.setSince(new Date());
		muc.join(user, null, history, 5000);
		muc.addParticipantListener(new UserPresenceListener(roomName, user, messages));
	}

	@Override
	public void run()
	{
		while(inRoom)
		{
			Message msg = muc.nextMessage(TIMEOUT);
			if(msg != null && msg.getBody() != null && msg.getFrom() != null)
			{
				String from = StringUtils.parseResource(msg.getFrom());
				ChatMessage chatMessage = new ChatMessage(from + ": " + msg.getBody(), (from.equals(user) ? "blue" : "orange"), roomName);
				System.out.println("Next message: " + chatMessage.getText());
				System.out.println("Next message colour: " + chatMessage.getColour());
				messages.add(chatMessage);
				PushContext pushContext = PushContextFactory.getDefault().getPushContext();
				pushContext.push("/message", chatMessage);
			}
		}
		finished = true;
		muc.leave();
		roomManagerBean.leftRoom(roomName);
	}

	public void stopListening()
	{
		inRoom = false;
	}

	public boolean restartListening()
	{
		if(!finished)
		{
			inRoom = true;
			return true;
		}
		return false;
	}

	public List<ChatMessage> getMessages()
	{
		System.out.println("print msgs");
		return messages;
	}

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

	public MultiUserChat getMultiUserChat()
	{
		return muc;
	}
}