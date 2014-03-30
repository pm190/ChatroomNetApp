package pm190.chatroom;

import java.util.Date;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import pm190.beans.ConnectionBean;

public class RoomChatListenerTest {
	private final MultiUserChat muc;

	public RoomChatListenerTest(String room, String user) throws XMPPException {
		XMPPConnection connection = new ConnectionBean().getConnection();
		connection.loginAnonymously();
		muc = new MultiUserChat(connection, room + "@"
				+ ServerProperties.getServicename());
		DiscussionHistory history = new DiscussionHistory();
		history.setSince(new Date());
		muc.join(user, null, history, 5000);
	}

	public void listen() {
		while (true) {
			Message msg = muc.nextMessage();
			if (msg != null && msg.getBody() != null) {
				System.out.println(msg.getBody());
			}
		}
	}

	public static void main(String[] args) throws XMPPException {
		RoomChatListenerTest r = new RoomChatListenerTest("lobby", "testListener");
		r.listen();
	}
}
