package playlist.com.UI;


import playlist.com.UI.scenes.Menu;
import playlist.com.UI.scenes.Scene;
import playlist.com.data.Data;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * class that defines a window
 */
public class Window implements Runnable{

    private static Window window = null;
    private int width, height;
    private String title;
    private JFrame frame;
    private JPanel panel;

    ArrayList<JPanel> panels = new ArrayList<>();



    private Window() {
        this.width = 1000;
        this.height = 700;
        this.title = "Media player";
        this.frame = new JFrame(this.title);
        this.panel =new JPanel(new CardLayout());
    }


    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void setPanel(Scene sc) {
        if (!this.panels.contains(sc.panel)){
            this.panels.add(sc.panel);
            this.panel.add(sc.panel,sc.NAME);
        }
        CardLayout cl = (CardLayout)(this.panel.getLayout());
        cl.show(this.panel,sc.NAME);

    }



    public void run() {
        System.out.println("[Window][init]: Creating window...");
        this.setPanel(Menu.get());
        this.frame.add(panel);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setBounds(100, 50, this.width, this.height);
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        this.panel.setFocusable(true);
        this.panel.requestFocusInWindow();
    }





}
