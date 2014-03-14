package XMPPClient;

import org.jivesoftware.smack.*;
import org.jivesoftware.smackx.*;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.sasl.*;

import java.util.*;


//To create an Account
//need to pass in a connection (currently got from XMPPManager)
//can then create an account
//delete that account (so long as logged in as that user!)
//logout that account
//create a guest account - the server must have anonymous login allowed
//to delete accounts on XMPP server doesn't appear that smack allows this, 
//think would need to write script that runs over the server - with the way XMPP does guest account potentially should be fine
//without deleting accounts

public class Account {
	
	//currently passing in connection and SASLAuthentication - which is currently set up in XMPPManager
	//is this something that should actually be setup in Account, rather than passed in?
	//or should it be passed in, as created at the startup of the application and then passed to account class?
	//currently SASLAuthentication being passed in from XMPPManager is set to "false" - I'm pretty sure
	//that for anonymous authenticaion it needs to be true - and unsure why in this example it's been set to false
	private AccountManager accountMan;
	private XMPPConnection connection;
	private SASLAuthentication saslAuthentication; 
	private SASLAnonymous saslAnon;
	
	
	public Account(XMPPConnection connection)
	{
		this.connection = connection;
		accountMan = new AccountManager(connection);
		
	}
	
	//create a user account with just a username and password
	public void createUserAccount (String userName, String password) throws XMPPException
	{
		//Error code produced when username is already used is 
		// 409 = conflict
		System.out.println("creating account " + userName + password);
		

		accountMan.createAccount(userName,  password);


	}
	public void performLogin(String username, String password) throws XMPPException 
	{
		//if already logged in XMPPException Error 409 - conflict thrown
		//if wrong password used xmppe.getMessage() produces "SASL authentication failed using mechanism DIGEST-MD5"
		
		if (connection!=null && connection.isConnected()) 
		{
			connection.login(username, password);

		}
		

	}
	//allow user to change password, user currently logged in when does so
	public void changePassword(String newPassword)throws XMPPException
	{
		
			accountMan.changePassword(newPassword);
		
	}
	
	//for a user to delete their account, they can only do this
		//whilst logged in from that account
	//seems to output error message stream:error (not-authorized)
	//at org.jivesoftware.smack.PacketReader.parsePackets(PacketReader.java:306)
	//at org.jivesoftware.smack.PacketReader.access$000(PacketReader.java:44)
	//at org.jivesoftware.smack.PacketReader$1.run(PacketReader.java:76)
	//but this seems to be standard when looking on web
		public void deleteUserAccount()throws XMPPException
		{
			
			accountMan.deleteAccount();
		}
		//this logouts out the user, but it does so by disconnecting the connection, so
		//can't simply log back in from here, but have to reset up connections etc
		public void logout()
		{
			connection.disconnect();
		}

		
		public void createGuestAccount()throws XMPPException
		{
			//this guest login is shown on server under sessions, with name anonymous and resource showing as I'd guess that JID
				connection.loginAnonymously();
			
			
		}
		
		//could use authenticateAnonymously to give a name to guest login's
		//would need to query users and give a different name??
				/*authenticateAnonymously
				public java.lang.String authenticateAnonymously()
				                                         throws XMPPException
				Performs ANONYMOUS SASL authentication. If SASL authentication was successful then resource binding and session establishment will be performed. 
				This method will return the full JID provided by the server while binding a resource to the connection.
				The server will assign a full JID with a randomly generated resource and possibly with no username.
				Returns:
				the full JID provided by the server while binding a resource to the connection.
				Throws:
				XMPPException - if an error occures while authenticating.
				Think need to change setSASLAuthentication to true in XMPP Manager to allow anon authetication
				Also need to set server up to allow anony registration
				this will possibly return no username, but assigns full JID with randomly generated resource
				query set up script on XMPP to offer up a guest login username??*/
		
		

/*-----------------------------Not tested yet from here on----------------------------------------------------------------------------------------------*/
	public void createUserAccountWithAttributes(String userName, String password, Map<String, String> attributes)throws XMPPException
	{
		accountMan = new AccountManager(connection);
		accountMan.createAccount(userName,  password, attributes);
		
	}
	public void setStatus(boolean available, String status) {

		Presence.Type type = available? Type.available: Type.unavailable;
		Presence presence = new Presence(type);

		presence.setStatus(status);
		connection.sendPacket(presence);

	}







	public void sendMessage(String message, String buddyJID) throws XMPPException {

		System.out.println(String.format("Sending mesage '%1$s' to user %2$s", message, buddyJID));
		
	//	Chat chat = chatManager.createChat(buddyJID, messageListener);
	//			chat.sendMessage(message);

	}



	public void createEntry(String user, String name) throws Exception {

		System.out.println(String.format("Creating entry for buddy '%1$s' with name %2$s", user, name));
		Roster roster = connection.getRoster();
		roster.createEntry(user, name, null);

	}
	
	
	
	
	

}


