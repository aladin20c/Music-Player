package playlist.com.UI.scenes;

import playlist.com.UI.Window;
import playlist.com.data.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * sign up scene class
 */
public class SignUp extends Scene{


    private static SignUp signup;

    private JButton signupButton,back;
    private JTextField userIDField;
    private JPasswordField userPasswordField;
    private JTextField day,month,year;
    private JLabel messageLabel;


    public SignUp() {
        super("SIGNUP");
    }

    public static SignUp get() {
        if (SignUp.signup == null) {
            SignUp.signup = new SignUp();
        }
        return SignUp.signup;
    }


    @Override
    public void initialise() {

        //userid
        JLabel userIDLabel = new JLabel("User id:");
        userIDLabel.setForeground(Color.BLACK);
        userIDLabel.setBounds(350, 200, 75, 25);
        panel.add(userIDLabel);

        back = new JButton("back");
        back.setBounds(50, 10, 75, 25);
        back.setFocusable(false);
        back.addActionListener(this);
        panel.add(back);

        userIDField = new JTextField();
        userIDField.setBounds(450, 200, 200, 25);
        panel.add(userIDField);

        //password
        JLabel userPasswordLabel = new JLabel("Password:");
        userPasswordLabel.setForeground(Color.BLACK);
        userPasswordLabel.setBounds(350, 300, 75, 25);
        panel.add(userPasswordLabel);

        userPasswordField = new JPasswordField();
        userPasswordField.setBounds(450, 300, 200, 25);
        panel.add(userPasswordField);


        //date

        JLabel birthday = new JLabel("Birthday dd/mm/yyyy");
        birthday.setForeground(Color.BLACK);
        birthday.setBounds(200, 400, 150, 25);
        panel.add(birthday);

        day = new JTextField();
        day.setBounds(400, 400, 25, 25);
        panel.add(day);

        month = new JTextField();
        month.setBounds(450, 400, 25, 25);
        panel.add(month);

        year = new JTextField();
        year.setBounds(500, 400, 50, 25);
        panel.add(year);


        //button
        signupButton = new JButton("signUp");
        signupButton.setBounds(300, 500, 100, 25);
        signupButton.setFocusable(false);
        signupButton.addActionListener(this);
        panel.add(signupButton);


        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(500, 500, 300, 25);
        panel.add(messageLabel);



    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == back) {

            playlist.com.UI.Window.get().setPanel(Menu.get());
            userIDField.setText("");
            userPasswordField.setText("");
            messageLabel.setText("");
            day.setText("");
            month.setText("");
            year.setText("");

        }else if(e.getSource()==signupButton){

            String userID = userIDField.getText();
            String password = String.valueOf(userPasswordField.getPassword());
            int dd,mm,yyyy;

            try {
                dd = Integer.parseInt(day.getText());
                mm = Integer.parseInt(month.getText());
                yyyy = Integer.parseInt(year.getText());
            }catch (Exception ex){
                messageLabel.setText("wrong format");return;
            }

            int res = Account.signUp(userID,password,yyyy,mm,dd );
            messageLabel.setText(Account.transcribeSignUpValue(res));

            if (res==0){
                userIDField.setText("");
                userPasswordField.setText("");
                messageLabel.setText("");
                day.setText("");
                month.setText("");
                year.setText("");
                Window.get().setPanel(LogIn.get());
            }


        }

        /*



         */
    }
}
