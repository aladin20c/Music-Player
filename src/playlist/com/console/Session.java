package playlist.com.console;

import playlist.com.data.Account;
import playlist.com.data.Data;

/**
 * class that follows the singleton pattern that opens a console sess  ion to use the program
 * @author alaeddinecheniour.
 */
public class Session implements Runnable{

    /**
     * singleton session
     */
    private static Session session;

    /**
     * Session class constructor set to private to respect the singleton pattern
     */
    private Session() {}

    /**
     * returns the singleton session
     * @return  Session
     */
    public static Session getSession() {
        if(session==null){session= new Session();}
        return session;
    }

    /**
     * to run the console app
     */
    @Override
    public void run() {
        State state = StateHandler.MENU;
        while (state!=null){
            state = state.handle();
        }
    }



}
