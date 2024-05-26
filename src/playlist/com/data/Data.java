package playlist.com.data;


import com.opencsv.CSVReader;
import playlist.com.client.notifs.BlindTest;
import playlist.com.client.pl.Playlist;

import java.io.FileReader;
import java.time.LocalDate;
import java.util.*;

/**
 * Data class, stores all the apps data
 * @version 1.0
 * @author alaeddinecheniour
 */
public final class Data {

    private final static TreeMap<String,Artist> artists = new TreeMap<>();
    private static final TreeMap<String, Account> accounts = new TreeMap<>();
    private final static TreeMap<String, ArrayList<Song>> songs = new TreeMap<>();


    /**
     * to add Fake data at the start of the program like fake accounts
     * @author alaeddinecheniour.
     */
    public static void initialise(String Path){
        System.out.println("[data] loading data...");
        try (CSVReader csvReader = new CSVReader(new FileReader(Path))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                Song.getOrMakeSong(values[0],values[1],values[2],values[3]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        MainAccount mc = new MainAccount("ala","ala", LocalDate.of(2001,12,23));
        mc.attachAFamilyMember("brother","brother",2020,12,12);
        mc.attachAFamilyMember("sister","sister",2022,2,2);

        new MainAccount("ali","ali", LocalDate.of(1999,6,9)).addFriend("ala");
        new MainAccount("layla","layla", LocalDate.of(1980,4,4)).addFriend("ala");

        mc.makePrivatePlaylist("hi");
        Playlist pl = mc.getPlaylistAtIndex("0");
        pl.addSong(Data.getSong("Ahe's My Kind Of Girl","ABBA"));
        pl.addSong(Data.getSong("Bang","ABBA"));
        pl.addSong(Data.getSong("Cassandra","ABBA"));
        pl.addSong(Data.getSong("Crazy World","ABBA"));
        pl.addSong(Data.getSong("Crying Over You","ABBA"));

        BlindTest bl = new BlindTest(mc,1);
        mc.addBlindTest(bl);
        bl.addLyricQuestion("Look at her face","Ahe's My Kind Of Girl","ABBA");
        bl.addLyricQuestion("Making somebody happy","Bang","ABBA");
        bl.launch();

        mc.addFriend("ali");
        mc.addFriend("layla");

        bl.addPerson(Data.getAccount("ali"));
        bl.addPerson(Data.getAccount("layla"));

    }

    /**
     * static method that searches if there's an artist that goes by this name
     * @param name name of the artist
     * @return it returns the artist if found or returns null
     * @author alaeddinecheniour.
     */
    static Artist getArtist(String name){
        if (name==null || name.isBlank()){return null;}
        return artists.getOrDefault(name,null);
    }


    /**
     * static method that adds an artist to the dataBase
     * @param artist the artist to add in question
     * if someone already exists with this name the artist won't get added
     * @author alaeddinecheniour.
     */
    static void addArtist(Artist artist){
        if(artist!=null) {artists.putIfAbsent(artist.getName(), artist);}
    }


    /**
     * static method that adds a song to the dataBase
     * @param song the song to add
     * If a song already exists with the same title and the same artist, it won't get added
     * @author alaeddinecheniour.
     */
    static void addSong(Song song){
        if(song==null || song.getTitle() == null || song.getTitle().isBlank()) return;
        ArrayList<Song> list = songs.get(song.getTitle());
        if(list==null){
            list=new ArrayList<>(1); //we choose an initial capacity of one because not so many songs have the same name
            list.add(song);
            songs.put(song.getTitle(),list);
        }else{
            if(!list.contains(song)) list.add(song);
        }
    }



    /**
     * method that searches if there's a song or many songs that goes by this name
     * @param title name of the song
     * @return it returns a collection of songs if found or null instead
     * @author alaeddinecheniour.
     */
     static Collection<Song> getSong(String title){
        if (title==null || title.isBlank()){return null;}
        return songs.get(title);
    }


    /**
     * method that searches if there's a song sang buy an artist
     * @param title name of the song
     * @param artist the singer
     * @return it returns ths song if found or null instead
     * @author alaeddinecheniour.
     */
    public static Song getSong(String title, String artist){
        if (title==null || title.isBlank() || artist==null){return null;}
        ArrayList<Song> songList = songs.get(title);
        if(songList==null) return null;
        for (Song s : songList){
            if(s.getArtist().getName().equals(artist)) return s;
        }
        return null;
    }


    /**
     * static method that searches if there's an account that goes by this pseudo
     * @param pseudo pseud of the account
     * @return it returns the account if found or returns null instead
     * @author alaeddinecheniour.
     */
     static Account getAccount(String pseudo){
        if (pseudo==null || pseudo.isBlank()){return null;}
        return accounts.getOrDefault(pseudo,null);
    }


    /**
     * static method that adds an account to the dataBase
     * @param account the account to add in question
     * if an account already exists with this pseudo the account won't get added
     * @author alaeddinecheniour.
     */
    static void addAccount(Account account){
        if(account!=null) {accounts.putIfAbsent(account.getPseudo(), account);}
    }

    /**
     * static method that removes an account from the dataBase
     * @param account the account to remove
     * @author alaeddinecheniour.
     */
    static void deleteAccount(Account account){accounts.remove(account.getPseudo());}

    /**
        @return array list of account s names taht contains an expression
     */
    public static ArrayList<String> searchAccounts(String infix){
        ArrayList<String> res = new ArrayList<>();
        for (String st : accounts.navigableKeySet()){
            if(st.contains(infix)) {
                res.add(st);
            }
        }
        return res;
    }

    /**
     @return array list of song s names that contains an expression,
     @param param specify the order of the songs, "a" for alphabetic, "l" number of times played by acc, "r" number of recommendation for acc
     */
    public static ArrayList<Song> search(String expr,String param, Account acc){
        ArrayList<Song> res = new ArrayList<>();
        if(expr==null || param==null ||expr.isBlank() || acc==null) return res;



        for (List<Song> list : songs.values()){
            for (Song s : list) {
                if(s.getTitle().contains(expr)) {
                    res.add(s);;
                }
            }
        }


        if(param.equals("a")){

            res.sort(Song::compareTo);

        }else if(param.equals("l")){
            res.sort((o1, o2) -> {
                int a = acc.getNumberOfPlayFor(o1);
                int b = acc.getNumberOfPlayFor(o2);
                if (a == b) return o1.compareTo(o2);
                if (a > b) return 1;
                return -1;
            });
        } else if(param.equals("r")){
            res.sort((o1, o2) -> {
                int a = acc.getNumberOfRecommendationsFor(o1);
                int b = acc.getNumberOfRecommendationsFor(o2);
                if (a == b) return o1.compareTo(o2);
                if (a > b) return 1;
                return -1;
            });
        }

        return res;
    }


    /**
     @return array list of artists s names taht contains an expression
     */
    public static ArrayList<String> searchArtists(String expr){
        ArrayList<String> res = new ArrayList<>();
        if(expr==null) return res;
        for (String st : artists.keySet()){
            if(st.contains(expr)) res.add(st);
        }
        return res;
    }


}
