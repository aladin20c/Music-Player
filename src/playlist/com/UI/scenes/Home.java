package playlist.com.UI.scenes;

import playlist.com.UI.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * home scene class
 */
public class Home extends Scene {


    private static Home home;

    private JButton notifications,friends,playlists,search,delete,disconnect;



    public Home() {
        super("HOME");
    }

    public static Home get() {
        if (Home.home == null) {
            Home.home = new Home();
        }
        return Home.home;
    }


    @Override
    public void initialise() {
        //login button

        notifications = new  JButton("notifications");
        notifications.setBounds(425, 50, 150, 25);
        notifications.setFocusable(false);
        notifications.addActionListener(this);
        panel.add(notifications);

        friends = new  JButton("friends");
        friends.setBounds(425, 150, 150, 25);
        friends.setFocusable(false);
        friends.addActionListener(this);
        panel.add(friends);


        playlists = new  JButton("playlists");
        playlists.setBounds(425, 250, 150, 25);
        playlists.setFocusable(false);
        playlists.addActionListener(this);
        panel.add(playlists);

        search = new  JButton("search");
        search.setBounds(425, 350, 150, 25);
        search.setFocusable(false);
        search.addActionListener(this);
        panel.add(search);

        delete = new  JButton("delete Account");
        delete.setBounds(425, 450, 150, 25);
        delete.setFocusable(false);
        delete.addActionListener(this);
        panel.add(delete);

        disconnect = new  JButton("disconnect");
        disconnect.setBounds(425, 550, 150, 25);
        disconnect.setFocusable(false);
        disconnect.addActionListener(this);
        panel.add(disconnect);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == notifications) {
            Window.get().setPanel(Notifications.get());
        }else if(e.getSource() == friends){
            Window.get().setPanel(Friends.get());
        }else if(e.getSource() == playlists){
            Window.get().setPanel(PlayListSection.get());
        }else if(e.getSource() == search){
            Window.get().setPanel(Search.get());
        }else if(e.getSource() == delete){
            getConnected_account().deleteAccount();
            setConnected_account(null);
            Window.get().setPanel(Menu.get());
        }else if(e.getSource() == disconnect){
            setConnected_account(null);
            Window.get().setPanel(Menu.get());
        }
    }
}
