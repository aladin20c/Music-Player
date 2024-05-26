package playlist.com.UI.scenes;

import playlist.com.data.Account;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * abtract scene class
 */
public abstract class Scene implements ActionListener {

    public JPanel panel;
    public final String NAME;
    public static Account connected_account;

    public Scene(String str) {
        this.NAME = str;
        panel = new JPanel();
        panel.setLayout(null);
        initialise();
    }

    public static void setConnected_account(Account acc) {connected_account = acc;}
    public static Account getConnected_account() {return connected_account;}


    /**
     * initialise scene : panel : buttons, etc....
     */
    public abstract void initialise();

    /**
     * handles user input
     * @param e : user input
     */
    @Override
    public void actionPerformed(ActionEvent e) {}

}
