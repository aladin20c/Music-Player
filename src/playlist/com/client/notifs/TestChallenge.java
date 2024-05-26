package playlist.com.client.notifs;

import playlist.com.data.Account;


/**
 * class that extends Notification that allows to notify a person of an invitation to a blind test
 * @author alaeddinecheniour.
 */
public class TestChallenge extends Notification{


    private BlindTest blindTest;


    public TestChallenge(Account sender, BlindTest blindTest) {
        super(sender);
        this.blindTest = blindTest;
    }

    @Override
    public String toString() {
        return super.toString()+"is inviting you to a blind test "+blindTest.toString();
    }

    public BlindTest getBlindTest(long i) {
        if(blindTest== null || blindTest.getId()!=i) return null;
        return blindTest;
    }
}
