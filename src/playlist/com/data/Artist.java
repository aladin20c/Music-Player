package playlist.com.data;

import java.util.*;

/**
 * class that represents an artist. Every artist has a name attribute. We suppose here
 * that every name is unique and if two artist have the same name . then they are the same artist
 * @version 1.0
 * @author alaeddinecheniour
 */

public class Artist implements Comparable<Artist>{

    private final String name;
    private final HashSet<Song> songs;
    public final static Artist unknown = new Artist("");


    /**
     * a private constructor of the Artist class. after initialising the parameters, the artist gets added to the database
     * @param name name of the artist
     * @author alaeddinecheniour.
     */
    private Artist(String name) {
        this.name = name;
        this.songs = new HashSet<>(8);
        Data.addArtist(this);
    }


    /**
     * method that adds a song to the artist, if the parameter is null, nothing happens
     * @param song to be added
     * @author alaeddinecheniour
     */
    void addSong(Song song){if(song!=null){this.songs.add(song);}}


    /**
     * method that returns an artist with the specified name
     * @param name of the artist in question. if the parameter is null or if the string is blank it returns the "unknown" artist
     * @return if an artist with this name already exist, then it gets returned, otherwise new artist is created and returned
     * this is the only method that can be used by other classes that allows the creation of an artist in this class, and it controls weather there are duplicates
     * @author alaeddinecheniour
     */
    public static Artist getOrMakeArtist(String name){
        if(name==null || name.isBlank()){return unknown;}
        Artist artist = Data.getArtist(name);
        if(artist!=null) return artist;
        else return new Artist(name);
    }

    /**
     * two artists are equals if they have the same name
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist)) return false;
        Artist artist = (Artist) o;
        return name.equals(artist.name);
    }

    @Override
    public int hashCode() {return Objects.hash(name);}

    @Override
    public String toString() {return '('+this.name+')';}


    public String getName() {return name;}

    /**
     * we compare artists through  comparing their names, when the argument i null, we suppose that "this" is "bigger" than null, and we return 1
     * @param o Artist
     * @return boolean
     */
    @Override
    public int compareTo(Artist o) {
        if(o==null || o.name==null) return 1;
        return this.name.compareTo(o.name);
    }
}
