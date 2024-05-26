package playlist.com.UI.scenes;

import playlist.com.UI.Window;
import playlist.com.client.pl.Playlist;
import playlist.com.main.Error;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
/**
 * playlists scene class
 */
public class PlayListSection extends Scene {


    private static PlayListSection playlistsection;

    private JLabel messageLabel;
    private JTextField textField;
    private JButton create,back,open,delete,updatePlaylists,unsubscribe;
    private JCheckBox check;
    private String selected="";


    private DefaultListModel<String> listModel;
    private JList<String> resultList;


    public PlayListSection() {
        super("PLAYLISTSECTION");
    }

    public static PlayListSection get() {
        if (PlayListSection.playlistsection == null) {
            PlayListSection.playlistsection = new PlayListSection();
        }
        return PlayListSection.playlistsection;
    }


    @Override
    public void initialise() {
        //login button
        JLabel label = new JLabel("Playlists Section: you can see your playlists and modify them, press update Playlists");
        label.setForeground(Color.BLACK);
        label.setBounds(200, 10, 600, 25);
        panel.add(label);

        back = new JButton("back");
        back.setBounds(50, 10, 75, 25);
        back.setFocusable(false);
        back.addActionListener(this);
        panel.add(back);



        textField = new JTextField();
        textField.setBounds(50, 50, 500, 25);
        panel.add(textField);

        create = new  JButton("create");
        create.setBounds(800, 50, 75, 25);
        create.setFocusable(false);
        create.addActionListener(this);
        panel.add(create);

        check = new JCheckBox();
        check.setBounds(740,40,50,50);
        check.setFocusable(false);
        check.addActionListener(this);
        panel.add(check);

        JLabel note = new JLabel("collaborative?");
        note.setForeground(Color.BLACK);
        note.setBounds(630, 50, 100, 25);
        panel.add(note);



        updatePlaylists = new JButton("update playlists");
        updatePlaylists.setBounds(50, 100, 200, 25);
        updatePlaylists.setFocusable(false);
        updatePlaylists.addActionListener(this);
        panel.add(updatePlaylists);

        listModel = new DefaultListModel<>();
        resultList = new JList<>();
        resultList.setModel(listModel);
        resultList.getSelectionModel().addListSelectionListener(e -> this.selected = resultList.getSelectedValue());

        JScrollPane scroll= new JScrollPane(resultList);
        scroll.setBounds(50,200,900,300);
        panel.add(scroll);

        unsubscribe = new  JButton("unsubscribe");
        unsubscribe.setBounds(140, 600, 110, 25);
        unsubscribe.setFocusable(false);
        unsubscribe.addActionListener(this);
        panel.add(unsubscribe);

        open = new  JButton("open");
        open.setBounds(50, 600, 75, 25);
        open.setFocusable(false);
        open.addActionListener(this);
        panel.add(open);

        delete = new  JButton("delete");
        delete.setBounds(250, 600, 75, 25);
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

            Window.get().setPanel(Home.get());
            textField.setText("");
            messageLabel.setText("");
            listModel.clear();

        }else if (e.getSource() == updatePlaylists) {

            ArrayList<String> list = connected_account.showPlaylists();
            listModel.clear();
            listModel.addAll(list);
            messageLabel.setText("");

        }else if (e.getSource() == create){
            if(check.isSelected()){
                if (connected_account.makeCollaborativePlaylist(textField.getText())) {
                    messageLabel.setText("collaborative playlist created!");
                } else {
                    messageLabel.setText("illegal name");
                }
            }else{
                if (connected_account.makePrivatePlaylist(textField.getText())) {
                    messageLabel.setText("private playlist created!");
                } else {
                    messageLabel.setText("illegal name");
                }
            }
        }else if (e.getSource() == delete){

            messageLabel.setText(Error.Error[connected_account.deletePlaylist(getIndex())]);

        }else if(e.getSource() == unsubscribe){

            if (selected==null ) return;
            messageLabel.setText(Error.Error[connected_account.unsubscribeFromAPlaylist(getIndex())]);

        }else if(e.getSource() == open){

            if (selected==null ) return;
            Playlist pl = connected_account.getPlaylistAtIndex(getIndex());
            if (pl == null) messageLabel.setText(Error.Error[8]);
            else{
                textField.setText("");
                messageLabel.setText("");
                listModel.clear();
                PlayListContent.get().setPlaylist(pl);
                Window.get().setPanel(PlayListContent.get());
            }

        }
    }


    private String getIndex(){
        if(selected==null || !selected.matches("\\d+-.+")) return null;
        int i = selected.indexOf('-');
        return selected.substring(0,i);
    }
}
