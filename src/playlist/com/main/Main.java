package playlist.com.main;


import playlist.com.UI.Window;
import playlist.com.console.Session;
import playlist.com.data.Data;

/**
 * class Main from which we will launch the program
 * @version 1.0
 * @author alaeddinecheniour
 */
public class Main {

    public static void main(String[] args) {

        //Data.initialise("./src/spotify_millsongdata.csv");


        if(args==null || args.length<1){
            System.out.println("you need to specify at least the path to the csv data file");
        }else if(args.length==1){
            System.out.println("[Main]opening "+args[0] +" ...");
            Data.initialise(args[0]);
            Session.getSession().run();
        }else{
            if(!args[0].equals("-ui")){
                System.out.println("unrecognised argument");
            } else {
                Data.initialise(args[1]);
                System.out.println("[Main]: Starting...");
                Window window = Window.get();
                window.run();
            }
        }



    }


}
