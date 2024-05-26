package playlist.com.UI.scenes;

import playlist.com.UI.Window;
import playlist.com.data.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * login scene class
 */
public class LogIn extends Scene{


    private static LogIn login;

    private JButton loginButton,back;
    private JTextField userIDField;
    private JPasswordField userPasswordField;
    private JLabel messageLabel;


    public LogIn() {
        super("LOGIN");
    }

    public static LogIn get() {
        if (LogIn.login == null) {
            LogIn.login = new LogIn();
        }
        return LogIn.login;
    }


    @Override
    public void initialise() {

        JLabel userIDLabel = new JLabel("User id:");
        userIDLabel.setForeground(Color.BLACK);
        userIDLabel.setBounds(350, 200, 75, 25);
        panel.add(userIDLabel);

        back = new JButton("back");
        back.setBounds(50, 10, 75, 25);
        back.setFocusable(false);
        back.addActionListener(this);
        panel.add(back);

        JLabel userPasswordLabel = new JLabel("Password:");
        userPasswordLabel.setForeground(Color.BLACK);
        userPasswordLabel.setBounds(350, 400, 75, 25);
        panel.add(userPasswordLabel);

        userIDField = new JTextField();
        userIDField.setBounds(450, 200, 200, 25);
        panel.add(userIDField);

        userPasswordField = new JPasswordField();
        userPasswordField.setBounds(450, 400, 200, 25);
        panel.add(userPasswordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(300, 500, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);
        panel.add(loginButton);


        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(500, 500, 300, 25);
        panel.add(messageLabel);



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {

            Window.get().setPanel(Menu.get());
            userIDField.setText("");
            userPasswordField.setText("");
            messageLabel.setText("");

        }else if(e.getSource()==loginButton){

            String userID = userIDField.getText();
            String password = String.valueOf(userPasswordField.getPassword());

            Account acc = Account.signIn(userID,password);
            if(acc==null){
                messageLabel.setText("unable to find this pseudo with this password, try again");
            }else {
                setConnected_account(acc);
                userIDField.setText("");
                userPasswordField.setText("");
                messageLabel.setText("");
                Window.get().setPanel(Home.get());
            }

        }
    }
}
