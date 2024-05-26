package playlist.com.UI.scenes;

import playlist.com.UI.Window;
import playlist.com.client.pl.CollaborativePlaylist;
import playlist.com.client.pl.Playlist;
import playlist.com.data.Data;
import playlist.com.data.Song;
import playlist.com.main.Error;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * playlist content scene class
 */
public class PlayListContent extends Scene {


    private static PlayListContent playlistcontent;
    private Playlist playlist;

    private JLabel messageLabel;
    private JTextField titleField,artistField,friendField;
    private JButton add,back,open,delete,updateSongs,updateMembers,addFriend;

    private DefaultListModel<String> listModel;
    private String selected="";
    private JList<String> resultList;


    public PlayListContent() {
        super("PLAYLISTCONTENT");
    }

    public static PlayListContent get() {
        if (PlayListContent.playlistcontent == null) {
            PlayListContent.playlistcontent = new PlayListContent();
        }
        return PlayListContent.playlistcontent;
    }

    public Playlist getPlaylist() {return playlist;}

    public void setPlaylist(Playlist playlist) {this.playlist = playlist;}

    @Override
    public void initialise() {
        //login button
        /*playlist.toString()+*/
        JLabel label = new JLabel(/*playlist.toString()+*/"Playlists: you can see your songs in this playlist, press update Songs");
        label.setForeground(Color.BLACK);
        label.setBounds(200, 10, 600, 25);
        panel.add(label);

        back = new JButton("back");
        back.setBounds(50, 10, 75, 25);
        back.setFocusable(false);
        back.addActionListener(this);
        panel.add(back);



        titleField = new JTextField("write songs title to add...");
        titleField.setBounds(50, 50, 400, 25);
        panel.add(titleField);

        artistField = new JTextField("write song's artist name...");
        artistField.setBounds(50, 100, 400, 25);
        panel.add(artistField);

        add = new  JButton("add");
        add.setBounds(600, 50, 75, 25);
        add.setFocusable(false);
        add.addActionListener(this);
        panel.add(add);

        JLabel note1 = new JLabel("title");
        note1.setForeground(Color.BLACK);
        note1.setBounds(500, 50, 100, 25);
        panel.add(note1);

        JLabel note2 = new JLabel("artist");
        note2.setForeground(Color.BLACK);
        note2.setBounds(500, 100, 100, 25);
        panel.add(note2);



        updateSongs = new JButton("update songs");
        updateSongs.setBounds(50, 150, 200, 25);
        updateSongs.setFocusable(false);
        updateSongs.addActionListener(this);
        panel.add(updateSongs);

        updateMembers = new JButton("update members");
        updateMembers.setBounds(250, 150, 200, 25);
        updateMembers.setFocusable(false);
        updateMembers.addActionListener(this);
        panel.add(updateMembers);


        listModel = new DefaultListModel<>();
        resultList = new JList<>();
        resultList.setModel(listModel);
        resultList.getSelectionModel().addListSelectionListener(e -> this.selected = resultList.getSelectedValue());
        JScrollPane scroll= new JScrollPane(resultList);
        scroll.setBounds(50,200,900,300);
        panel.add(scroll);

        addFriend = new  JButton("add Friend");
        addFriend.setBounds(10, 560, 110, 25);
        addFriend.setFocusable(false);
        addFriend.addActionListener(this);
        panel.add(addFriend);

        friendField = new JTextField("write your friend name");
        friendField.setBounds(140, 560, 300, 25);
        panel.add(friendField);


        open = new  JButton("open");
        open.setBounds(10, 600, 75, 25);
        open.setFocusable(false);
        open.addActionListener(this);
        panel.add(open);

        delete = new  JButton("delete");
        delete.setBounds(100, 600, 75, 25);
        delete.setFocusable(false);
        delete.addActionListener(this);
        panel.add(delete);

        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(500, 550, 300, 25);
        panel.add(messageLabel);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {


            titleField.setText("");
            artistField.setText("");
            friendField.setText("");
            messageLabel.setText("");
            listModel.clear();
            setPlaylist(null);
            Window.get().setPanel(PlayListSection.get());

        }else if (e.getSource() == updateSongs) {

            ArrayList<String> list = playlist.getSongsTitles();
            listModel.clear();
            listModel.addAll(list);
            messageLabel.setText("");

        }else if (e.getSource() == updateMembers) {

            if(playlist instanceof CollaborativePlaylist){
                ArrayList<String> list = ((CollaborativePlaylist) playlist).getMembersNames();
                listModel.clear();
                listModel.addAll(list);
                messageLabel.setText("");
            }else {
                messageLabel.setText(Error.Error[10]);
            }
        }else if (e.getSource() == addFriend) {

            String name = friendField.getText();
            messageLabel.setText(Error.Error[ connected_account.addFriendToACollaborativeList(name,playlist) ]);

        }else if(e.getSource() == add) {

            Song s = Data.getSong(titleField.getText(),artistField.getText());
            if(s==null){
                messageLabel.setText(Error.Error[14]);
            }else {
                if(playlist.addSong(s)){
                    messageLabel.setText(Error.Error[0]);
                }else{
                    messageLabel.setText(Error.Error[15]);
                }
            }



        }else if(e.getSource() == open) {

            int a =selected.indexOf(':');
            int b = selected.lastIndexOf('(');
            int c = selected.lastIndexOf(')');

            if (selected.isBlank() || a < 0 || b < 0 || c < 0) return;

            String title = selected.substring(a+2,b);
            String artist = selected.substring(b+1,c);


            Song s = Data.getSong(title,artist);
            if(s==null){
                messageLabel.setText(Error.Error[14]);
            }else {

                titleField.setText("");
                artistField.setText("");
                friendField.setText("");
                messageLabel.setText("");
                listModel.clear();
                SongPage.get().setPlaylist(playlist);
                SongPage.get().setSong(s);
                Window.get().setPanel(SongPage.get());
            }

        }else if(e.getSource() == delete) {

            int a =selected.indexOf(':');
            int b = selected.lastIndexOf('(');
            int c = selected.lastIndexOf(')');

            if (selected.isBlank() || a < 0 || b < 0 || c < 0) return;

            String title = selected.substring(a+2,b);
            String artist = selected.substring(b+1,c);


            Song s = Data.getSong(title,artist);
            if(s==null){
                messageLabel.setText(Error.Error[14]);
            }else {
                if(playlist.removeSong(s)){
                    messageLabel.setText(Error.Error[0]);
                }else{
                    messageLabel.setText(Error.Error[14]);
                }
            }




        }



    }
}
