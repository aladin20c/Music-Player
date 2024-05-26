package playlist.com.UI.scenes;

import playlist.com.UI.Window;
import playlist.com.data.Account;
import playlist.com.data.MainAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * attach scene class
 */
public class Attach extends Scene{


    private static Attach attach;

    private JButton signupButton,back;
    private JTextField userIDField,yourId;
    private JPasswordField userPasswordField,yourPass;

    private JTextField day,month,year;

    private JLabel messageLabel;

    public Attach() {
        super("ATTACH");
    }

    public static Attach get() {
        if (Attach.attach == null) {
            Attach.attach = new Attach();
        }
        return Attach.attach;
    }


    @Override
    public void initialise() {

        //your account
        JLabel yourAccount = new JLabel("connect with your account, type your id and your password and then the information of the person you want to attach to your account");
        yourAccount.setForeground(Color.BLACK);
        yourAccount.setBounds(50, 50, 900, 25);
        panel.add(yourAccount);

        back = new JButton("back");
        back.setBounds(50, 10, 75, 25);
        back.setFocusable(false);
        back.addActionListener(this);
        panel.add(back);


        yourId = new JTextField("your id");
        yourId.setBounds(250, 125, 200, 25);
        panel.add(yourId);

        yourPass = new JPasswordField("your password");
        yourPass.setBounds(550, 125, 200, 25);
        panel.add(yourPass);


        //userid
        JLabel userIDLabel = new JLabel("Person's id:");
        userIDLabel.setForeground(Color.BLACK);
        userIDLabel.setBounds(325, 200, 100, 25);
        panel.add(userIDLabel);

        userIDField = new JTextField();
        userIDField.setBounds(450, 200, 200, 25);
        panel.add(userIDField);

        //password
        JLabel userPasswordLabel = new JLabel("New Password:");
        userPasswordLabel.setForeground(Color.BLACK);
        userPasswordLabel.setBounds(325, 300, 100, 25);
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
        messageLabel.setForeground(Color.BLACK);
        messageLabel.setBounds(500, 500, 300, 25);
        panel.add(messageLabel);



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==back){

            userIDField.setText("");
            userPasswordField.setText("");
            messageLabel.setText("");
            day.setText("");
            month.setText("");
            year.setText("");
            yourId.setText("Your Id");
            yourPass.setText("Your Password");
            Window.get().setPanel(Menu.get());


        }else if(e.getSource()==signupButton){

            int dd,mm,yyyy;
            try {
                dd = Integer.parseInt(day.getText());
                mm = Integer.parseInt(month.getText());
                yyyy = Integer.parseInt(year.getText());
            }catch (Exception ex){
                messageLabel.setText("wrong date format");return;
            }




            String userID = yourId.getText();
            String password = String.valueOf(yourPass.getPassword());
            Account account = Account.signIn(userID,password);


            String ID = userIDField.getText();
            String pass= String.valueOf(userPasswordField.getPassword());

            int res=-1;
            if((account instanceof MainAccount)) {
                res = ((MainAccount) account).attachAFamilyMember(ID, pass, yyyy, mm, dd);
                messageLabel.setText(Account.transcribeSignUpValue(res));
            }

            if (res==0){
                messageLabel.setText("account is attached successfully");
                userIDField.setText("");
                userPasswordField.setText("");
                messageLabel.setText("");
                day.setText("");
                month.setText("");
                year.setText("");
                yourId.setText("Your Id");
                yourPass.setText("Your Password");
                Window.get().setPanel(Menu.get());
            }

            if(account==null){
                messageLabel.setText("unable to find your account, try again");
            }else if(!(account instanceof MainAccount)){
                messageLabel.setText("your account is already attached to another account, try with another account");}
            }

        }


}

