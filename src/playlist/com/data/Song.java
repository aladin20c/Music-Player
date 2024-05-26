package playlist.com.data;


import java.util.*;



/**
 * class that represents songs, we suppose that every song is identified by its artist and its name
 * @version 1.0
 * @author alaeddinecheniour
 */

public class Song implements Comparable<Song>{


    private final String title;
    private final Artist artist;
    private final String lyrics;
    private final String link;


    /**
     * private constructor of the Song class.
     * @param title title of the song
     * @param artist name of the artist of the song
     * @param link link of the song
     * @param lyrics content of the song
     * @author alaeddinecheniour.
     */
    private Song(Artist artist, String title, String link, String lyrics) {
        this.title = title;
        this.artist = artist;
        this.artist.addSong(this);
        this.lyrics = lyrics;
        this.link = link;
        Data.addSong(this);
    }


    /**
     * method that returns an artist with the specified name
     * @param name of the artist in question. if the parameter is null or if the string is blank it returns the "unknown" artist
     * @return if an artist with this name already exist, then it gets returned, otherwise new artist is created and returned
     * this is the only method that can be used by other classes that allows the creation of an artist in this class, and it controls whether there are duplicates
     * @author alaeddinecheniour
    */
    public static Song getOrMakeSong(String name,String title,String link, String lyrics){
        if(title==null || title.isBlank()){return null;}
        Artist artist = Artist.getOrMakeArtist(name);
        var songs = Data.getSong(title);
        if(songs==null) return new Song(artist,title,link,lyrics);
        for(Song s : songs){if(s.artist.equals(artist)) return s;}
        return new Song(artist,title,link,lyrics);
    }

    /**
     * methods that checks if this song's lyrics contains a string
     * @param lyric lyric to check
     * @return boolean
     * @author alaeddinecheniour
     */
    public boolean containsLyric(String lyric){
        return this.lyrics.contains(lyric);
    }


    /**
     *we suppose that two songs are equal if they have the same title and the same artist
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return title.equals(song.title) && artist.equals(song.artist);
    }


    @Override
    public int hashCode() {return Objects.hash(title, artist);}


    @Override
    public String toString() {return "title: " + title + this.artist.toString();}


    public String getTitle() {return title;}


    public Artist getArtist() {return artist;}

    /**
     * we compare songs through  comparing their titles, when the argument i null, we suppose that "this" is "bigger" than null, and we return 1.
     * if the two songs have equal titles, we then compare the artists
     * @param o Artist
     * @return boolean
     */
    @Override
    public int compareTo(Song o) {
        if(o==null || o.title==null) return 1;
        int comparison = this.title.compareTo(o.title);
        return (comparison==0)? this.artist.compareTo(o.artist):comparison;
    }

}
