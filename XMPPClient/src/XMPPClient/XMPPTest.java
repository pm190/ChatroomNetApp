package XMPPClient;

public class XMPPTest {

    public static void main(String[] args) throws Exception {
    	/*92.236.126.119:9090 -> XMPP admin port
    	92.236.126.119:5222 -> XMPP chat port
    	92.236.126.119:8080 -> Tomcat server*/
         

        Account setAccount;
        CreateConnection xmppManager = new CreateConnection("92.236.126.119", 5222);
        xmppManager.init();
        //create an account manager called setAccount, that contains the connection created within xmppManager
        setAccount = new Account(xmppManager.getConnection());
       
    /*tests
        setAccount.createGuestAccount();
        setAccount.createUserAccount("TaliTest", "test");
        setAccount.performLogin("TaliTest", "test");
        setAccount.changePassword("change");
        setAccount.deleteUserAccount();*/
        
        
        boolean isRunning = true;

         

        while (isRunning) {

            Thread.sleep(50);

        }

         

        xmppManager.destroy();

         

    }
    
    /* This is for connecting from eclipse and logingin, etc
   
     

      

     String buddyJID = "test2";
     String buddyName = "test2";

     xmppManager.createEntry(buddyJID, buddyName);

      

     xmppManager.sendMessage("Hello mate", "test2@olympus");//92.236.126.119");*/


 

}


