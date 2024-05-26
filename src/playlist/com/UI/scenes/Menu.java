package playlist.com.UI.scenes;

import playlist.com.UI.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * menu scene class
 */
public class Menu extends Scene {


    private static Menu menu;

    private JButton loginButton;
    private JButton signupButton;
    private JButton attachAccountButton;


    public Menu() {
        super("MENU");
    }

    public static Menu get() {
        if (Menu.menu == null) {
            Menu.menu = new Menu();
        }
        return Menu.menu;
    }


    @Override
    public void initialise() {
        //login button

        loginButton = new  JButton("Login");
        loginButton.setBounds(450, 100, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);
        panel.add(loginButton);

        //signup button

        signupButton = new JButton("Signup");
        signupButton.setBounds(450, 300, 100, 25);
        signupButton.setFocusable(false);
        signupButton.addActionListener(this);
        panel.add(signupButton);

        //attachButton

        attachAccountButton = new JButton("Attach");
        attachAccountButton.setBounds(450, 500, 100, 25);
        attachAccountButton.setFocusable(false);
        attachAccountButton.addActionListener(this);
        panel.add(attachAccountButton);


        JLabel userPasswordLabel = new JLabel("Press this to attach a family member to your account");
        userPasswordLabel.setForeground(Color.BLACK);
        userPasswordLabel.setBounds(325, 400, 350, 25);
        panel.add(userPasswordLabel);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            Window.get().setPanel(LogIn.get());
        }else if (e.getSource() == signupButton) {
            Window.get().setPanel(SignUp.get());
        }else if (e.getSource() == attachAccountButton) {
            Window.get().setPanel(Attach.get());
        }
    }
}
