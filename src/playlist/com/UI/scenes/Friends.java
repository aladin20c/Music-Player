package playlist.com.UI.scenes;

import playlist.com.UI.Window;
import playlist.com.data.Data;
import playlist.com.main.Error;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * friends scene class
 */
public class Friends extends Scene {


    private static Friends friends;

    private JButton back,search,sesFriends,add,delete;
    private JTextField searchField;
    private DefaultListModel<String> listModel;
    private JList<String> resultList;
    private String selected="";
    private JLabel messageLabel;





    public Friends() {
        super("FRIENDS");
    }

    public static Friends get() {
        if (Friends.friends == null) {
            Friends.friends = new Friends();
        }
        return Friends.friends;
    }


    @Override
    public void initialise() {
        //login button
        searchField = new JTextField();
        searchField.setBounds(50, 50, 840, 25);
        panel.add(searchField);

        search = new  JButton("search");
        search.setBounds(890, 50, 75, 25);
        search.setFocusable(false);
        search.addActionListener(this);
        panel.add(search);

        back = new JButton("back");
        back.setBounds(50, 10, 75, 25);
        back.setFocusable(false);
        back.addActionListener(this);
        panel.add(back);


        JLabel label = new JLabel("Friends Section: you can see your friends or search for other accounts to befriend");
        label.setForeground(Color.BLACK);
        label.setBounds(200, 10, 600, 25);
        panel.add(label);


        sesFriends = new JButton("your friends");
        sesFriends.setBounds(50, 100, 120, 25);
        sesFriends.setFocusable(false);
        sesFriends.addActionListener(this);
        panel.add(sesFriends);


        listModel = new DefaultListModel<>();

        resultList = new JList<>();
        resultList.setModel(listModel);
        resultList.getSelectionModel().addListSelectionListener(e -> this.selected = resultList.getSelectedValue());

        JScrollPane scroll= new JScrollPane(resultList);
        scroll.setBounds(50,200,900,300);
        panel.add(scroll);


        add = new  JButton("add");
        add.setBounds(50, 600, 75, 25);
        add.setFocusable(false);
        add.addActionListener(this);
        panel.add(add);

        delete = new  JButton("delete");
        delete.setBounds(200, 600, 75, 25);
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
            searchField.setText("");
            messageLabel.setText("");
            listModel.clear();

        }else if(e.getSource() == search){

            listModel.clear();
            String expr = searchField.getText();
            ArrayList<String> st = Data.searchAccounts(expr);
            listModel.addAll(st);

        }else if(e.getSource() == sesFriends){

            ArrayList<String> res = getConnected_account().showFriends();
            listModel.clear();
            listModel.addAll(res);

        }else if(e.getSource() == add){

            int res = getConnected_account().addFriend(selected);
            messageLabel.setText(Error.Error[res]);

        }else if(e.getSource() == delete){

            int res = getConnected_account().deleteFriend(selected);
            messageLabel.setText(Error.Error[res]);

        }
    }
}
