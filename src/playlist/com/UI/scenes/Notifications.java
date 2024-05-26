package playlist.com.UI.scenes;

import playlist.com.UI.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * notifications scene class
 */
public class Notifications extends Scene {


    private static Notifications notifications;
    private JButton back,notification1,notification2;
    private DefaultListModel<String> listModel;
    private JList<String> resultList;


    public Notifications() {
        super("NOTIFICATIONS");
    }

    public static Notifications get() {
        if (Notifications.notifications == null) {
            Notifications.notifications = new Notifications();
        }
        return Notifications.notifications;
    }


    @Override
    public void initialise() {

        back = new JButton("back");
        back.setBounds(50, 10, 75, 25);
        back.setFocusable(false);
        back.addActionListener(this);
        panel.add(back);

        //login button
        JLabel label = new JLabel("Notifications : You can consult your notifications");
        label.setForeground(Color.BLACK);
        label.setBounds(200, 10, 600, 25);
        panel.add(label);


        notification1 = new JButton("show all notifications");
        notification1.setBounds(50, 150, 200, 25);
        notification1.setFocusable(false);
        notification1.addActionListener(this);
        panel.add(notification1);

        notification2 = new JButton("show new notifications");
        notification2.setBounds(300, 150, 200, 25);
        notification2.setFocusable(false);
        notification2.addActionListener(this);
        panel.add(notification2);


        listModel = new DefaultListModel<>();
        resultList = new JList<>();
        resultList.setModel(listModel);
        resultList.getSelectionModel().addListSelectionListener(e -> {});

        JScrollPane scroll= new JScrollPane(resultList);
        scroll.setBounds(50,200,900,300);
        panel.add(scroll);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            listModel.clear();
            Window.get().setPanel(Home.get());
        }else if (e.getSource() == notification1 ) {
            listModel.clear();
            listModel.addAll(getConnected_account().showNotifications());
        }else if (e.getSource() == notification2) {
            listModel.clear();
            listModel.addAll(getConnected_account().showNewNotifications());
        }






    }
}
