package playlist.com.client.notifs;

import playlist.com.data.Account;


/**
 * a simple notification composed of a string as a message
 * @author alaeddinecheniour.
 */
public class SimpleNotification extends Notification{

    String st;

    public SimpleNotification(Account sender, String st) {
        super(sender);
        this.st = st;
    }


    @Override
    public String toString() {
        return super.toString()+st;
    }
}
