package playlist.com.data;


import playlist.com.client.notifs.BlindTest;
import playlist.com.client.notifs.Notification;
import playlist.com.client.notifs.Recommendation;
import playlist.com.client.notifs.TestChallenge;
import playlist.com.client.pl.CollaborativePlaylist;
import playlist.com.client.pl.Playlist;
import playlist.com.main.OpenWeb;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;


/**
 * Abstract class that represents an account. Every pseudonym is unique.
 * @version 1.0
 * @author alaeddinecheniour
 */
public abstract class  Account implements Comparable<Account>{


    private final String pseudo;
    private final String mdp;
    private final LocalDate birthDay;
    private int age;

    private final TreeSet<Account> family;
    private final TreeSet<Account> friends;
    private final ArrayList<Playlist> playlists;
    private final ArrayList<Notification> notifications;

    private final HashSet<BlindTest> blindTestsCreated=new HashSet<>();

    /*map that contains songs as a key and the number of times played and the number of times recommended*/
    TreeMap<Song, int[]> stats = new TreeMap<>();

    /**
     * constructor of the Account class.
     * @param pseudo the pseudonym of the account
     * @param mdp password
     * @param birthDay the birthday of the person in question
     * @author alaeddinecheniour.
     */
    Account(String pseudo, String mdp, LocalDate birthDay) {
        this.pseudo = pseudo;
        this.mdp = mdp;
        this.birthDay = birthDay;
        this.age= Period.between(this.birthDay, LocalDate.now()).getYears();
        this.family = new TreeSet<>();
        this.friends = new TreeSet<>();
        this.notifications = new ArrayList<>();
        this.playlists = new ArrayList<>();
        Data.addAccount(this);
    }

    /*Connection related functions*/

    /**
     * we compare accounts through  comparing their pseudonyms, when the argument is null, we suppose that "this" is "bigger" than null, and we return 1
     * @param pseudo Account, the pseudonym for the new account, is limited by 255 characters
     * @param mdp String, the password for the new account, pseudo and mdp are linted by 255 characters
     * @param year int, year of birth
     * @param month int, month of birth
     * @param day int, day of birth
     * @return int , it returns 0 if the persons with this information can sign up, 1 if the |pseudo| or the |password| > 255 or if they are empty, 2 if the pseudo already exists, 3 id the date is invalid and 4 if the person is a minor
     * @author alaeddinecheniour
     */
    public static int canSignUp(String pseudo, String mdp, int year,int month,int day){
        //pseudo and mdp are linted by 255 characters
        if(pseudo==null || mdp==null || pseudo.isBlank() || mdp.isBlank()|| pseudo.length()>255 || mdp.length() > 255 ){return 1;}
        //pseudo already exists
        if(Data.getAccount(pseudo)!=null){return 2;}
        LocalDate birthday;
        try {
            birthday = LocalDate.of(year, month, day);
            if(birthday.isAfter(LocalDate.now())) {throw new DateTimeException("date specified is superior to the current date");}
        }catch (DateTimeException e){
            return 3;
        }
        //minors cannot sign up themselves
        boolean minor= Period.between(birthday, LocalDate.now()).getYears() < 18;
        if(minor){return 4;}

        return 0;
    }


    /**
     * method that returns an account when the pseudonym and the password are provided
     * @param pseudo the pseudonym of the account
     * @param mdp password     * @return if an artist with this name already exist, then it gets returned, otherwise new artist is created and returned
     * @return the account if the pseudonym and the password are correct, null instead.
     * @author alaeddinecheniour
     */
    public static Account signIn(String pseudo, String mdp){
        Account acc = Data.getAccount(pseudo);
        if(acc==null){
            return null;//incorrect pseudo
        }else if(mdp==null || !mdp.equals(acc.mdp)){
            return null;//incorrect password
        }else{
            return acc;
        }
    }

    /**
     * method that registers an account to the database. it checks if the information are correct with the method canSignUp
     * @param pseudo the pseudonym of the account
     * @param mdp password
     * @return the account if the pseudonym and the password are correct, null instead.
     * @author alaeddinecheniour
     */
    public static int signUp(String pseudo, String mdp, int year,int month,int day){
        int a = canSignUp( pseudo,  mdp,  year, month, day);
        if(a==0){new MainAccount(pseudo,mdp,LocalDate.of(year, month, day));}
        return a;
    }



    /**
     * it transcribes the integer returned by the canSignUp method
     * @param a int, parameter returned by the canSignUp method
     * @return String
     * @author alaeddinecheniour
     */
    public static String transcribeSignUpValue(int a){
        return switch (a) {
            case 0 -> "You've signed Up!";
            case 1 -> "pseudonym and password are limited by 255 char, and shouldn't be empty";
            case 2 -> "pseudonym is taken";
            case 3 -> "invalid Date";
            case 4 -> "wait till you turn 18";
            default -> "unable to sign up due to unknown reason";
        };
    }


    /**
     * Deleting the account will delete all friendship links, and it will delete recursively all attached accounts
     * also you'll unsubscribe from all collaborative lists
     */
    public void emptyAccount(){
        for (Playlist pl : this.playlists){
            if(! (pl instanceof CollaborativePlaylist)) continue;
            CollaborativePlaylist cpl = (CollaborativePlaylist) pl;
            cpl.removeAccount(this);
            if(cpl.isDeserted()) cpl.clear();
        }

        this.playlists.clear();
        this.notifications.clear();
        this.blindTestsCreated.clear();

        for (Account fr : this.friends){
            fr.friends.remove(this);
        }
        this.friends.clear();
    }

    public void deleteAccount(){
        for (Account fm : this.family){
            Data.deleteAccount(fm);
            fm.emptyAccount();
            fm.family.clear();
        }
        Data.deleteAccount(this);
        this.emptyAccount();
        this.family.clear();
    }





    /*Friends Related functions*/

    /**
     * show friend list
     * @author alaeddinecheniour
     */
    public ArrayList<String> showFriends(){
        ArrayList<String> res = new ArrayList<>();
        res.add("Family:");
        if(this.family.isEmpty()) {
            res.add("you have no family members attached to your account");
        }else{
            for (Account a : this.family) res.add(a.toString());
        }
        res.add("Friends:");
        if(this.friends.isEmpty()) {
            res.add("you have no friends");
        }else{
            for (Account a : this.friends) res.add(a.toString());
        }
        return res;
    }




    /**
     * method that adds an account to the friendList
     * @param name, String the account to befriend
     * @return int , 0 if success and >0 otherwise
     * @author alaeddinecheniour
     */
    public int addFriend(String name){
        if(name==null || name.isBlank()) return 1;
        Account acc = Data.getAccount(name);
        if(acc==null) return 2;
        if(acc==this) return 3;
        if(this.family.contains(acc)) return 4;
        if(this.isAMinor() ^ acc.isAMinor()) return 5;
        if (this.friends.add(acc) && acc.friends.add(this)){
            return 0;
        }else{
            return 6;
        }
    }


    /**
     * method that deletes an account from the friendList
     * @param name, String the account to delete
     * @return int , 0 if success and >0 otherwise
     * @author alaeddinecheniour
     */
    public int deleteFriend(String name){
        if(name==null || name.isBlank()) return 1;
        Account acc = Data.getAccount(name);
        if(acc==null) return 2;
        if(acc==this) return 3;
        if(this.family.contains(acc)) return 4;
        if (this.friends.remove(acc) && acc.friends.remove(this)){
            return 0;
        }else{
            return 7;
        }
    }


    /*Playlist Related functions*/


    /**
     * show playlists
     * @author alaeddinecheniour
     */
    public ArrayList<String> showPlaylists(){
        ArrayList<String> res = new ArrayList<>();
        res.add("playlists:");
        if(this.playlists.isEmpty()){
            res.add("you have no playlists");
            return res;
        }
        int i=0;
        for(Playlist a : this.playlists) {
            res.add(i+"-"+a.toString());i++;
        }
        return res;
    }

    /**
     * method that adds  creates a new private playlist and adds it to the arraylist Playlists
     * @param name of the playlist
     * @return boolean
     * @author alaeddinecheniour
     */
    public boolean makePrivatePlaylist(String name){
        if(name==null || name.isBlank()) return false;
        this.playlists.add(new Playlist(name));
        return true;
    }

    /**
     * method that adds  creates a new collaborative playlist and adds it to the arraylist Playlists
     * @param name of the playlist
     * @return boolean
     * @author alaeddinecheniour
     */
    public boolean makeCollaborativePlaylist(String name){
        if(name==null || name.isBlank()) return false;
        this.playlists.add(new CollaborativePlaylist(name,this));
        return true;
    }


    /**
     * methods that returns the playlist at an index
     * @param index String
     * @return the playlist if the index is rightly written
     */
    public Playlist getPlaylistAtIndex(String index){
        if (index == null || !index.matches("\\d+")) return null;
        int i = Integer.parseInt(index);
        if(i<0 || i>=this.playlists.size()) return null;
        return this.playlists.get(i);
    }


    /**
     * method that adds  deletes a playlist from the author's list
     * @param index a string representing the index of the list to be deleted
     * @return int, 1 if the string parameter doesn't match a number, 2 if the index is out of bounds, and then it calls the method deletePlaylist(Playlist pl) or  deletePlaylist(CollaborativePlaylist pl) and return their values
     * @author alaeddinecheniour
     */
    public int deletePlaylist(String index){
        Playlist pl = getPlaylistAtIndex(index);
        if(pl==null) return 8;
        if(pl instanceof CollaborativePlaylist){
            CollaborativePlaylist cpl = (CollaborativePlaylist) pl;
            if(!cpl.isAdmin(this)) return 9;
            cpl.clear();
            return 0;
        }
        this.playlists.remove(pl);
        pl.clear();
        return 0;
    }


    /**
     * remove this account form the collaborative playlist
     * @param index, string index of the element in the arraylist of playlists
     * @return  int
     * @author alaeddinecheniour
     */
    public int unsubscribeFromAPlaylist(String index){
        Playlist pl = getPlaylistAtIndex(index);
        if(pl==null) return 8;
        if(! (pl instanceof CollaborativePlaylist)) return 10;
        CollaborativePlaylist cpl = (CollaborativePlaylist) pl;
        this.playlists.remove(cpl);
        cpl.removeAccount(this);
        if(cpl.isDeserted()) cpl.clear();
        return 0;
    }



    /**
     * adds a friend to the collaborative playlist
     * @param name, name of the friend
     * @param pl the playlist in question
     * @return  int
     * @author alaeddinecheniour
     */
    public int addFriendToACollaborativeList(String name, Playlist pl){
        if(pl==null) return 11;
        if(! (pl instanceof CollaborativePlaylist)) return 10;

        Account account= Data.getAccount(name);
        if(account==null) return 2;
        if(account==this) return 3;
        if(!this.friends.contains(account) && !this.family.contains(account)) return 12;

        if (((CollaborativePlaylist) pl).addAccount(account)){
            return 0;
        }else{
            return 13;
        }
    }



    /**
     * attach a family member to this account
     * @param account, family member
     * @author alaeddinecheniour
     */
    public void addFamilyMember(Account account){
        if(account!=null && account!=this){
            for (Account acc :this.family) {
                acc.family.add(account);
                account.family.add(acc);
            }
            this.family.add(account);
            account.family.add(this);
        }

    }


    /**
     * plays a song, increases the amount of times "this" songs was played in the stats Map, and opens it in a page web with the desktop default navigator
     * @param song, the song in question
     * @author alaeddinecheniour
     */
    public  void play(Song song){
        int[] stats =  this.stats.get(song);
        if(stats==null){
            int[] a = {1,0};
            this.stats.put(song,a);
        }else {
            stats[0]++;
        }
        OpenWeb.browse(song.getTitle(),song.getArtist().getName());
    }

    /**
     * send a recommendation for a song to a friend
     * @param name, of the friend
     * @param song, the song in question
     * @author alaeddinecheniour
     */
    public int recommendSongToFriend(String name, Song song){
        if(song==null) return 14;
        Account account= Data.getAccount(name);
        if(account==null) return 2;
        if(account==this) return 3;
        if(!friends.contains(account) && !family.contains(account)) return 12;
        account.notifications.add(new Recommendation(this,song));
        int[] stat = account.stats.get(song);
        if(stat==null){
            int[] a = {0,1};
            account.stats.put(song,a);
        }else{
            stat[1]++;
        }
        return 0;
    }




    /**
     * prints all notifications in console
     * @author alaeddinecheniour
     */
    public ArrayList<String> showNotifications(){
        ArrayList<String> st = new ArrayList<>();
        st.add("Notifications:");
        if(notifications.isEmpty()){
            st.add("you have no notifications");
        } else {
            for (Notification n : this.notifications) st.add(n.toString());
        }
        return st;
    }

    /**
     * prints only new notifications in console (received less than 24 hours ago)
     * @author alaeddinecheniour
     */
    public ArrayList<String> showNewNotifications(){
        ArrayList<String> st = new ArrayList<>();
        st.add("Notifications:");
        if(notifications.isEmpty()){
            st.add("you have no notifications");
        }else for (Notification n : this.notifications) {
            if(!n.isOld()) st.add(n.toString());
        }
        return st;
    }


    /**
     * returns a blind tests to be taken that exists in the notifications
     * @param id, of the blind tests
     * @author alaeddinecheniour
     */
     public BlindTest getBlindTest(String id){
        if (id == null || !id.matches("\\d+")) return null;
        long i = Long.parseLong(id);
        BlindTest bt;

        for (Notification nt : this.notifications){
            if(nt instanceof TestChallenge){
                bt = ((TestChallenge)nt).getBlindTest(i);
                if(bt!=null) return bt;
            }
        }
        return null;
    }

    /**
     * returns a blind test that was created by this and is in the BlindTestMade list
     * @param id, of the blind tests
     * @author alaeddinecheniour
     */
    public BlindTest getMyTest(String id){
        if (id == null || !id.matches("\\d+")) return null;
        long i = Long.parseLong(id);

        for (BlindTest bt : this.blindTestsCreated){
            if(bt.getId()==i){
                return bt;
            }
        }
        return null;
    }

    /**
     * // Method to send invitations to friends
     * @param name, of the friend
     * @param bt, the blind test
     * @author alaeddinecheniour
     */

    public int addFriendToBlindTest(String name,BlindTest bt) {
        if(!bt.isCreator(this)) return 9;
        if(name==null || name.isBlank()) return 1;
        Account acc = Data.getAccount(name);
        if(acc==null) return 2;
        if(acc==this) return 3;
        if(!this.family.contains(acc) && !this.friends.contains(acc)) return 12;
        return bt.addPerson(acc);
    }





    /*Utilities*/


    /**
     * return whether the account "this" is a minor or not. its checks whether his age is smaller than 18
     * @return boolean
     * @author alaeddinecheniour
     */
    public boolean isAMinor(){return this.age < 18;}

    /**
     * two accounts are equals if they have the same pseudo
     * @param o Object
     * @return boolean
     * @author alaeddinecheniour
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(pseudo, account.pseudo);
    }


    @Override
    public int hashCode() {return Objects.hash(pseudo);}


    /**
     * we compare accounts through  comparing their pseudonyms, when the argument is null, we suppose that "this" is "bigger" than null, and we return 1
     * @param o Account
     * @return boolean
     * @author alaeddinecheniour
     */
    @Override
    public int compareTo(Account o) {
        if(o==null || o.pseudo==null) return 1;
        return this.pseudo.compareTo(o.pseudo);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {return this.pseudo;}

    /**
     * @return string
     */
    public String getPseudo() {return pseudo;}

    /**
     * @param pl, removes it from the playlist arraylist
     */
    public void removePlaylist(Playlist pl){
        this.playlists.remove(pl);
    }

    /**
     * @param notify, adds it to the notifications arraylist
     */
    public void addNotification(Notification notify){this.notifications.add(notify);}

    /**
     * @param acc, removes it from the family account set
     */
    public void removeFamilyMember(Account acc){
            this.family.remove(acc);
            for (Account a: this.family) a.family.remove(acc);
    }

    /**
     * @param bt, adds it to the blindTestsCreated arraylist
     */
    public void addBlindTest(BlindTest bt){this.blindTestsCreated.add(bt);}


    /**
     * returns the number of times this listened to this song
     * @return int
     * @author alaeddinecheniour
     */
    public int getNumberOfPlayFor(Song s){
        int[] a = this.stats.get(s);
        if(a==null) return 0;
        return a[0];
    }

    /**
     * returns the number of recommendation received for a song
     * @return int
     * @author alaeddinecheniour
     */
    public int getNumberOfRecommendationsFor(Song s){
        int[] a = this.stats.get(s);
        if(a==null) return 0;
        return a[1];
    }

}
