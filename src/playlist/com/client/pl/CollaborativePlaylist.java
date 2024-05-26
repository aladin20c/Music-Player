package playlist.com.client.pl;

import playlist.com.data.Account;
import playlist.com.data.Song;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * class that represents a Collaborative playlist that extends the playlist calls
 * We use a LinkedHashSet to keep track of the owners, the admin being in the first place
 * @version 1.0
 * @author alaeddinecheniour
 */
public class CollaborativePlaylist extends Playlist{


    private final LinkedHashSet<Account> owners;

    /**
     * constructor of the CollaborativePlaylist class.
     * @param name name of the playlist
     * @param admin the admin of the playlist
     * @author alaeddinecheniour.
     */
    public CollaborativePlaylist(String name, Account admin) {
        super(name);
        this.owners = new LinkedHashSet<>();
        this.owners.add(admin);
    }


    /**
     * adding an account to the owners Collection
     * @return boolean
     * @author alaeddinecheniour
     */
    public boolean addAccount(Account acc){
        if(acc==null) return false;
        return this.owners.add(acc);
    }


    /**
     * remove an account from the owners Collection
     * @return boolean
     * @author alaeddinecheniour
     */
    public boolean removeAccount(Account acc){
        if(acc==null) return false;
        return this.owners.remove(acc);
    }

    /**
     * see if there's any owners left
     * @return boolean
     * @author alaeddinecheniour
     */
    public boolean isDeserted(){
        return owners.isEmpty();
    }


    /**
     * check if the given account is the admin.aka, being in the first place in the LinkedHashSet
     * @return boolean
     * @author alaeddinecheniour
     */
    public boolean isAdmin(Account acc){
        if(acc==null || this.owners.isEmpty()) return false;
        return this.owners.iterator().next()==acc;
    }

    /**
     * clear the song set, and the owners set. And removing the list from the owners records
     * @author alaeddinecheniour
     */
    @Override
    public void clear() {
        super.clear();
        Iterator<Account> it = this.owners.iterator();
        Account acc;
        while (it.hasNext()) {
            acc = it.next();
            acc.removePlaylist(this);
        }
        this.owners.clear();
    }

    @Override
    public String toString() {
        if(this.owners.isEmpty()) return super.toString()+"(collaborative)";
        return super.toString()+'('+"collaborative :"+this.owners.iterator().next()+')';
    }


    /**
     * prints the members of a collaborative list, the admin being first
     * @author alaeddinecheniour
     */
    public void showMembers(){
        System.out.println(this.toString() +"'s Members:");
        System.out.print("Admin: ");
        for (Account acc : owners) System.out.println(acc);
    }



    public ArrayList<String> getMembersNames(){
        ArrayList<String> st = new ArrayList<>();
        st.add("Admin: ");
        for (Account acc : owners) st.add(acc.toString());
        return st;
    }
}
