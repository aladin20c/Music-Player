package playlist.com.UI.scenes;

import playlist.com.UI.Window;
import playlist.com.client.pl.Playlist;
import playlist.com.console.StateHandler;
import playlist.com.data.Song;
import playlist.com.main.Error;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * song page scene class
 */
public class SongPage extends Scene {


    private static SongPage sogpage;
    private Playlist playlist;
    private Song song;


    private JLabel note1,note2;
    private JLabel messageLabel;
    private JTextField friendField;
    private JButton back,play,recommend;




    public SongPage() {
        super("SONGPAGE");
    }

    public static SongPage get() {
        if (SongPage.sogpage == null) {
            SongPage.sogpage = new SongPage();
        }
        return SongPage.sogpage;
    }



    @Override
    public void initialise() {
        JLabel label = new JLabel("You selecte this song");
        label.setForeground(Color.BLACK);
        label.setBounds(200, 10, 600, 25);
        panel.add(label);

        back = new JButton("back");
        back.setBounds(50, 10, 75, 25);
        back.setFocusable(false);
        back.addActionListener(this);
        panel.add(back);


        note1 = new JLabel("title");
        note1.setForeground(Color.BLACK);
        note1.setBounds(400, 150, 400, 25);
        panel.add(note1);

        note2 = new JLabel("artist");
        note2.setForeground(Color.BLACK);
        note2.setBounds(400, 250, 400, 25);
        panel.add(note2);


        recommend = new  JButton("recommend to a friend");
        recommend.setBounds(10, 560, 250, 25);
        recommend.setFocusable(false);
        recommend.addActionListener(this);
        panel.add(recommend);

        friendField = new JTextField("write your friend name");
        friendField.setBounds(300, 560, 300, 25);
        panel.add(friendField);


        play = new  JButton("play");
        play.setBounds(10, 600, 75, 25);
        play.setFocusable(false);
        play.addActionListener(this);
        panel.add(play);

        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(500, 450, 300, 25);
        panel.add(messageLabel);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {


            note1.setText("Title: ");
            note2.setText("Artist: ");
            friendField.setText("");
            messageLabel.setText("");

            setSong(null);
            if(playlist!=null) Window.get().setPanel(PlayListContent.get());
            else Window.get().setPanel(Search.get());;

        }if (e.getSource() == play) {

            getConnected_account().play(song);


        }if (e.getSource() == recommend) {

            messageLabel.setText(Error.Error[connected_account.recommendSongToFriend(friendField.getText(),song)]);

        }
    }










    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        if(song!=null){
            this.song = song;
            note1.setText("Title :"+song.getTitle());
            note2.setText("Title :"+song.getArtist().getName());
        }
    }
}
