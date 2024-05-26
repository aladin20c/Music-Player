package playlist.com.client.pl;

import playlist.com.data.Song;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * class that represents a playlist, every playlist has a name and a list of songs. We use a LinkedHashSet To preserve the order Of insertion
 * @version 1.0
 * @author alaeddinecheniour
 */
public class Playlist{


    private final String name;
    private final LinkedHashSet<Song> songs;

    /**
     * constructor of the Playlist class.
     * @param name name of the playlist
     * @author alaeddinecheniour.
     */
    public Playlist(String name) {
        this.name = name;
        this.songs = new LinkedHashSet<>(8);
    }


    /**
     * adds a song to the playlist by adding it to the LinkedHashSet
     * @param song
     * @return boolean
     * @author alaeddinecheniour.
     */
    public boolean addSong(Song song){
        return this.songs.add(song);
    }

    /**
     * removes a song from the playlist
     * @param song
     * @return boolean
     * @author alaeddinecheniour.
     */
    public boolean removeSong(Song song){
        return this.songs.remove(song);
    }


    /**
     * see if the playlist has a song
     * @param song
     * @return boolean
     * @author alaeddinecheniour.
     */
    public boolean containsSong(Song song){
        return this.songs.contains(song);
    }


    /**
     * clears the playlist, by clearing all the songs from the LinkedHashSet
     * @author alaeddinecheniour.
     */
    public void clear(){songs.clear();}

    @Override
    public String toString() {return name;}

    /**
     * prints all the songs in the playlist
     * @author alaeddinecheniour.
     */
    public void showSongs(){
        System.out.println(this.toString() +"'s Songs:");
        if(songs.isEmpty())System.out.println("empty playlist");
        else for (Song s : songs) System.out.println(s);
    }

    /**
     * returns an arraylist of strings composed of songs titles
     * @author alaeddinecheniour.
     */
    public ArrayList<String> getSongsTitles() {
        ArrayList<String> st = new ArrayList<>();
        for (Song s : this.songs) st.add(s.toString());
        if(st.isEmpty())st.add("empty playlist");
        return st;
    }
}
