package playlist.com.client.notifs;

import playlist.com.data.Account;
import playlist.com.data.Song;

/**
 * class that etends Notification and used as a song recommendation notification from a friend to another
 * @version 1.0
 * @author alaeddinecheniour
 */
public class Recommendation extends Notification{

    private final Song recommendation;

    public Recommendation(Account sender, Song song) {
        super(sender);
        this.recommendation = song;
    }




    @Override
    public String toString() {
        return super.toString() + "song recommendation " + recommendation.toString();
    }
}
