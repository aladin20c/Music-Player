package playlist.com.UI.scenes;

import playlist.com.UI.Window;
import playlist.com.data.Data;
import playlist.com.data.Song;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Search scene class
 */
public class Search extends Scene{


    private static Search search;

    private JButton back,lookup,play;
    private JTextField searchField;
    private DefaultListModel<String> listModel;
    private JList<String> resultList;
    private String selected="";

    private Search() {
        super("SEARCH");
    }

    public static Search get() {
        if (Search.search == null) {
            Search.search = new Search();
        }
        return Search.search;
    }

    @Override
    public void initialise() {
        //login button
        searchField = new JTextField();
        searchField.setBounds(50, 50, 840, 25);
        panel.add(searchField);

        lookup = new  JButton("search");
        lookup.setBounds(890, 50, 75, 25);
        lookup.setFocusable(false);
        lookup.addActionListener(this);
        panel.add(lookup);

        back = new JButton("back");
        back.setBounds(50, 10, 75, 25);
        back.setFocusable(false);
        back.addActionListener(this);
        panel.add(back);


        JLabel label = new JLabel("Search: you can search for songs");
        label.setForeground(Color.BLACK);
        label.setBounds(200, 10, 600, 25);
        panel.add(label);





        listModel = new DefaultListModel<>();
        resultList = new JList<>();
        resultList.setModel(listModel);
        resultList.getSelectionModel().addListSelectionListener(e -> this.selected = resultList.getSelectedValue());
        JScrollPane scroll= new JScrollPane(resultList);
        scroll.setBounds(50,200,900,300);
        panel.add(scroll);


        play = new  JButton("play");
        play.setBounds(50, 600, 75, 25);
        play.setFocusable(false);
        play.addActionListener(this);
        panel.add(play);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {

            searchField.setText("");
            listModel.clear();
            Window.get().setPanel(Home.get());

        }else if(e.getSource() == lookup) {

            listModel.clear();
            String expr = searchField.getText();

            if (expr.length() > 2){
                ArrayList<Song> sb= Data.search(expr,"a",getConnected_account());
                ArrayList<String> st = Data.searchArtists(expr);
                for (Song s : sb) listModel.addElement(s.toString());
                listModel.addAll(st);
            }

        }else if(e.getSource() == play){

            if(selected==null)return;
            int a =selected.indexOf(':');
            int b = selected.lastIndexOf('(');
            int c = selected.lastIndexOf(')');
            if (selected.isBlank() || a < 0 || b < 0 || c < 0) return;
            String title = selected.substring(a+2,b);
            String artist = selected.substring(b+1,c);
            Song s = Data.getSong(title,artist);
            if(s!=null) {

                SongPage.get().setPlaylist(null);
                SongPage.get().setSong(s);
                Window.get().setPanel(SongPage.get());
            }

        }
    }
}
