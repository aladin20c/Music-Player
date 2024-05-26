package playlist.com.client.notifs;
import playlist.com.data.Account;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * class that represents a Notification, it contains the sender account and the time of sending
 * @version 1.0
 * @author alaeddinecheniour
 */
public abstract class Notification {

    private final Account sender;
    private final LocalDateTime time;

    public Notification(Account sender) {
        this.sender = sender;
        this.time=LocalDateTime.now();
    }

    @Override
    public String toString() {return
            this.time.getDayOfMonth()+"/"+this.time.getMonth().toString()+'/'+this.time.getYear()+" at "+this.time.getHour()+'h'+
                    ": you've got a notification from '"+sender.getPseudo()+'\''+':'+' ';}


    /**
     * checks if the notification is older than a day
     * @return boolean
     */
    public boolean isOld(){
        long p =  time.until(LocalDateTime.now(), ChronoUnit.HOURS);
        return p>24;
    }
}
