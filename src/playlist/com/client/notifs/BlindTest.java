package playlist.com.client.notifs;


import playlist.com.data.Account;
import playlist.com.data.Data;
import playlist.com.data.Song;
import playlist.com.main.Error;

import java.util.*;


/**
 * class that represents a BlindTest, every blind test has a unique id, a creator, A list of strings and answers.
 * When building a blindtest you can't add people to it. after launching it, you can invite people and they can answer until it is closed
 * @version 1.0
 * @author alaeddinecheniour
 */
public class BlindTest {

    private static long COUNT = 0;

    private final long id;
    Account creator;
    private final int mode;
    private final ArrayList<String> lyrics=new ArrayList<>();
    private final ArrayList<Song> songs = new ArrayList<>();
    private final TreeMap<Account,Integer> score = new TreeMap<>();
    private boolean isBeingBuilt;
    private boolean isClosed;


    /**
     * constructor of the BlindTest class.
     * @param creator
     * @param mode : 1,2,3 : for 1 you must guess the song's titles, for 2 you must guess the artists, for 3 you must guess both between a list of shuffled answers
     * @author alaeddinecheniour.
     */
    public BlindTest(Account creator,int mode) {
        this.id=COUNT++;
        this.creator=creator;
        this.mode = mode;
        isBeingBuilt=true;
        isClosed = false;
        creator.addNotification(new SimpleNotification(creator,"you ve created a blind test with id: "+id));
    }

    /**
     * prints the statuts of the Blind test
     * @author alaeddinecheniour.
     */
    public void status(){
        System.out.println(this);
        System.out.println("Creator: "+creator.toString());
        if(lyrics.isEmpty()){
            System.out.println("no quizzes yet");
        }else{
            System.out.println("you must guess these");
            switch (mode) {
                case 1 -> System.out.println("you must guess the songs");
                case 2 -> System.out.println("you must guess the artists");
                case 3 -> {
                    System.out.println("you must guess the songs and the artists, choose from this list");
                    ArrayList<Song> array = new ArrayList<>(this.songs);
                    Collections.shuffle(array);
                    for (Song s : songs) System.out.print(s.toString() + "  ");
                }
            }
            for (String s : lyrics){
                System.out.println(s+":? , ");
            }
        }
        if (isBeingBuilt){
            System.out.println("it is not launched yet");
        }else{
            System.out.println("current scores : ");
            if(score.isEmpty()){
                System.out.println("no one is invited to take the test yet");
            }else{
                for (Map.Entry<Account,Integer> entry : score.entrySet() ){
                    System.out.println(entry.getKey().toString() + " : "+entry.getValue());
                }
            }
        }
        if(isClosed){
            System.out.println("test is closed");
        }

    }


    /**
     * check if the blindtest is empty
     * @author alaeddinecheniour.
     */
    public boolean isEmpty(){return this.lyrics.isEmpty();}

    /**
     * leunch the blind test
     * @author alaeddinecheniour.
     */
    public void launch(){
        isBeingBuilt=false;
        creator.addBlindTest(this);
    }

    /**
     * check if the account passed in parameters is the creator
     * @author alaeddinecheniour.
     */
    public boolean isCreator(Account acc){return creator.equals(acc);}


    /**
     * adds a person to the blind test
     * @author alaeddinecheniour.
     */
    public int addPerson(Account acc){
        if(isBeingBuilt) return 17;
        if (isClosed) return 20;
        if (score.containsKey(acc)) return 22;
        score.put(acc,-1);
        acc.addNotification(new TestChallenge(creator,this));
        return 0;
    }



    /**
     * adds a quiz to the test. checks if the songs contains the lyrics
     * @author alaeddinecheniour.
     */
    public int addLyricQuestion(String lyr, String title, String artist) {
        if (!isBeingBuilt) return 18;
        Song s = Data.getSong(title,artist);
        if(s==null || lyr==null) return 14;
        if(!s.containsLyric(lyr)) return 21;
        this.lyrics.add(lyr);
        this.songs.add(s);
        return 0;
    }


    /**
     * prints the response format for console
     * @author alaeddinecheniour.
     */
    public void responseShape(){
        System.out.println("you need to respond to the "+this.lyrics.size()+" questions like this format");
        switch (mode){
            case 1:System.out.println("song1###song2###song3###song4###song5###songN"); break;
            case 2:System.out.println("artist1###artist2###artist3###artist4###artist5###artistN"); break;
            case 3:System.out.println("song1###artist1###song2###artist2###song3###artist3###song4###artist4###song5###artist5###songN###artistN"); break;
        }
    }


    /**
     * closes the test. checks if the person closing the test is the admin
     * @author alaeddinecheniour.
     */
    public void close(Account acc) {
        if(acc!=this.creator) {System.out.println(Error.Error[9]);return;}
        if(isBeingBuilt) {System.out.println(Error.Error[17]);return;}
        isClosed = true;
        broadcast();
    }


    /**
     * mathod that allows a participnt to respond to a blindtest
     * @author alaeddinecheniour.
     */

    public int respond (Account acc, String response){
        if(acc==null || !this.score.containsKey(acc)) return 24;
        if(this.score.get(acc)!=-1) return 25;
        String[] data = response.split("###");
        int score=0;

        if(mode==1){
            if(data.length!=this.lyrics.size()) return 23;

            int i=0;
            for (Song s : songs){
                if(s.getTitle().equals(data[i])) score++;
                i++;
            }

        }else if(mode==2){
            if(data.length!=this.lyrics.size()) return 23;
            int i=0;
            for (Song s : songs){
                if(s.getArtist().getName().equals(data[i])) score++;
                i++;
            }

        }else{
            if(data.length!=(this.lyrics.size()*2)) return 23;
            int i=0;
            for (Song s : songs){
                if( s.getTitle().equals(data[i]) &&  s.getArtist().getName().equals(data[i+1])  ) score++;
                i+=2;
            }

        }

        this.score.put(acc,score);

        SimpleNotification sn = new SimpleNotification(creator,acc.getPseudo()+" scores "+score+" on "+this.toString());
        creator.addNotification(sn);
        for (Account key : this.score.keySet()) {
            key.addNotification(sn);
        }


        return 26;

    }

    /**
     * broadcast the final scores
     * @author alaeddinecheniour.
     */
    public void broadcast(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.toString()).append('\n');
        sb.append("creator : ").append(creator.toString()).append('\n');
        sb.append("solutions").append('\n');
        sb.append(lyrics.toString()).append('\n');
        sb.append(songs.toString()).append('\n');
        sb.append("score").append('\n');
        for (Account key : score.keySet()) {
            sb.append(key.toString()).append("=").append(score.get(key)).append(", ");
        }
        SimpleNotification sn = new SimpleNotification(creator,sb.toString());
        creator.addNotification(sn);
        for (Account key : score.keySet()) {
            key.addNotification(sn);
        }
    }



    public boolean isClosed() {return isClosed;}
    public boolean isBeingBuilt() {return isBeingBuilt;}


    @Override
    public String toString() {
        return "{ Blind Test " + "id:" + id + " created by " + creator.toString() +"mode "+mode+ '}';
    }

    public long getId() {
        return id;
    }


}
