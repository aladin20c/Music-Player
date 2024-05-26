package playlist.com.console;

import playlist.com.client.notifs.BlindTest;
import playlist.com.client.pl.CollaborativePlaylist;
import playlist.com.client.pl.Playlist;
import playlist.com.data.Account;
import playlist.com.data.Data;
import playlist.com.data.MainAccount;
import playlist.com.data.Song;
import playlist.com.main.Error;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Conatains the different states that the user go through while navigating the app
 * Menu, Sign in, log in, search, song , etc....
 * each state handles the user input and returns the next state to go to
 */

public class StateHandler {

    /** session's connected accunt */
    private static Account connected_account = null;
    /** session's current playlist */
    private static Playlist current_playlist = null;
    /** session's current song */
    private static Song current_song = null;
    /** session's current test */
    private static BlindTest current_test = null;

    /** the menu state */
    public static final State MENU = () -> {
        System.out.println("**********************************************************************");
        System.out.println("Menu: Enter 'a' to sign up.\nMenu: Enter 'b' to attach a new account to your account.\nMenu: Enter 'c' to sign in.\nMenu: Enter 'd' to exit");
        System.out.println("**********************************************************************");
        Scanner sc = new Scanner(System.in);
        String input;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("a")) return StateHandler.SIGNUP;
            if(input.equals("b")) return StateHandler.SIGNUP_ATTACHED;
            if(input.equals("c")) return StateHandler.SIGNIN;
            if(input.equals("d")) return null;
        }
    };


    /** the sign up state */
    public static final State SIGNUP = () -> {
        System.out.println("**********************************************************************");
        System.out.println("SIGNUP: Enter your pseudo password dayOfBirth MonthOfBirth YearOfBirth like this for example:  Dostoevsky  *********  11  11  1821 ");
        System.out.println("SIGNUP: Enter 'c' to return to Menu");
        System.out.println("**********************************************************************");
        Scanner sc = new Scanner(System.in);
        String input;
        String[] data;
        int day,month,year;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("c")) return StateHandler.MENU;
            data = input.split("\\s+");
            if(data.length!=5){System.out.println("wrong format");continue;}

            try {
                day = Integer.parseInt(data[2]);
                month = Integer.parseInt(data[3]);
                year = Integer.parseInt(data[4]);
            }catch (Exception e){
                System.out.println("wrong format");
                continue;
            }

            int res = Account.signUp(data[0],data[1],year,month,day );
            System.out.println(Account.transcribeSignUpValue(res));
            if (res==0){
                System.out.println("now sign in");
                return StateHandler.SIGNIN;
            }
        }
    };


    /** the sign an attached account state */
    public static final State SIGNUP_ATTACHED = () -> {
        System.out.println("**********************************************************************");
        System.out.println("SIGNUP_ATTACHED: Enter 'c' to return to Menu");
        System.out.println("SIGNUP_ATTACHED: Enter your pseudo and password to your main account like this for example:  Dostoevsky *********  ");
        System.out.println("**********************************************************************");
        Scanner sc = new Scanner(System.in);
        String input;
        String[] data;
        Account account;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("c")) return StateHandler.MENU;
            data = input.split("\\s+");
            if(data.length!=2){System.out.println("wrong format");continue;}
            account = Account.signIn(data[0],data[1]);
            if(account==null){System.out.println("unable to find this pseudo with this password, try again");continue;}
            if(! (account instanceof MainAccount)){System.out.println("you're account is already attached to another account, try with another account");}
            else break;
        }

        System.out.println("SIGNUP: Enter the 'pseudonym password dayOfBirth MonthOfBirth YearOfBirth' for the new account you want to attach to your main account like this for example:  Dostoevsky  *********  11  11  1821 ");
        int day,month,year;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("c")) return StateHandler.MENU;
            data = input.split("\\s+");
            if(data.length!=5){System.out.println("wrong format");continue;}

            try {
                day = Integer.parseInt(data[2]);
                month = Integer.parseInt(data[3]);
                year = Integer.parseInt(data[4]);
            }catch (Exception e){
                System.out.println("wrong format");
                continue;
            }

            int res = ((MainAccount) account).attachAFamilyMember(data[0],data[1],year,month,day );
            System.out.println(Account.transcribeSignUpValue(res));
            if (res==0){
                System.out.println("account is attached successfully");
                return StateHandler.SIGNIN;
            }
        }
    };


    /** the sign in state */
    public static final State SIGNIN = () -> {
        System.out.println("**********************************************************************");
        System.out.println("SIGNIN: Enter your pseudo password like this for example:  Dostoevsky *********  ");
        System.out.println("SIGNIN: Enter 'c' to return to Menu");
        System.out.println("**********************************************************************");
        Scanner sc = new Scanner(System.in);
        String input;
        String[] data;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("c")) return StateHandler.MENU;
            data = input.split("\\s+");
            if(data.length!=2){System.out.println("wrong format");continue;}
            Account acc = Account.signIn(data[0],data[1]);
            if(acc==null){System.out.println("unable to find this pseudo with this password, try again");continue;}
            connected_account = acc;
            return StateHandler.HOME;
        }
    };


    /** the home state */
    public static final State HOME = () -> {
        System.out.println("**********************************************************************");
        System.out.println("Menu: Enter 'a' to go to Notifications section");
        System.out.println("Menu: Enter 'b' to go to friends section");
        System.out.println("Menu: Enter 'c' to the playlist section");
        System.out.println("Menu: Enter 'd' to the search section");
        System.out.println("Menu: Enter 'e' to delete account");
        System.out.println("Menu: Enter 'f' to disconnect");
        System.out.println("**********************************************************************");
        Scanner sc = new Scanner(System.in);
        String input;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("a"))return StateHandler.NOTIFICATIONS_SECTION;
            if(input.equals("b")) return StateHandler.FRIENDS_SECTION;
            if(input.equals("c")) return StateHandler.PLAYLIST_SECTION;
            if(input.equals("d")) return StateHandler.SEARCH;
            if(input.equals("e")){
                connected_account.deleteAccount();
                connected_account=null;
                return StateHandler.MENU;
            }if(input.equals("f")) {
                connected_account=null;
                return StateHandler.MENU;
            }
        }
    };


    /** the friends state */
    public static final State FRIENDS_SECTION = () -> {
        System.out.println("**********************************************************************");
        System.out.println("FRIENDS_SECTION: Enter 'a' to show all your friends");
        System.out.println("FRIENDS_SECTION: Enter 'b name' to add a friend with a pseudo name");
        System.out.println("FRIENDS_SECTION: Enter 'c name' to delete a friend");
        System.out.println("FRIENDS_SECTION: Enter 'search infix' to launch a search for accounts that contains the infix");
        System.out.println("FRIENDS_SECTION: Enter 'e' to go to home Page");
        System.out.println("**********************************************************************");
        Scanner sc = new Scanner(System.in);
        String[] data;
        String input;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("a")){
                ArrayList<String> list = connected_account.showFriends();
                for (String st:list) System.out.println(st);
                continue;
            }
            if(input.equals("e")){return StateHandler.HOME;}

            data = input.split("\\s+");
            if(data.length!=2) continue;

            if(data[0].equals("b")){
                System.out.println(
                        Error.Error[connected_account.addFriend(data[1])]
                );
            }else if(data[0].equals("c")){
                System.out.println(
                        Error.Error[connected_account.deleteFriend(data[1])]
                );
            }else if(data[0].equals("search")){

                ArrayList<String> st = Data.searchAccounts(data[1]);
                if(st.isEmpty()) System.out.println("empty search");
                else for (String s : st) System.out.println(s);

            }
        }


    };


    /** the playlist section state */
    public static final State PLAYLIST_SECTION = () -> {
        System.out.println("**********************************************************************");
        System.out.println("PLAYLIST_SECTION: Enter 'a' to show all your playlists");
        System.out.println("PLAYLIST_SECTION: Enter 'b name' to create a private playlist named 'name'");
        System.out.println("PLAYLIST_SECTION: Enter 'c name' to create a collaborative playlist named 'name'");
        System.out.println("PLAYLIST_SECTION: Enter 'd index' to delete a playlist at a certain index");
        System.out.println("PLAYLIST_SECTION: Enter 'e index' to unsubscribe from a playlist at a certain index");
        System.out.println("PLAYLIST_SECTION: Enter 'f index' to log into the playlist at the given index");
        System.out.println("PLAYLIST_SECTION: Enter 'h' to go to home Page");
        System.out.println("**********************************************************************");
        Scanner sc = new Scanner(System.in);
        String[] data;
        String input;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("a")){
                ArrayList<String> list = connected_account.showPlaylists();
                for (String st:list) System.out.println(st);
                continue;
            }
            if(input.equals("h")){return StateHandler.HOME;}
            data = input.split("\\s+");

            if(data.length!=2) continue;

            if (data[0].equals("b")) {

                if (connected_account.makePrivatePlaylist(data[1])) {
                    System.out.println("playlist created!");
                } else {
                    System.out.println("illegal name");
                }

            } else if (data[0].equals("c")) {

                if (connected_account.makeCollaborativePlaylist(data[1])) {
                    System.out.println("done");
                } else {
                    System.out.println("illegal name");
                }

            } else if (data[0].equals("d")) {

                System.out.println(Error.Error[connected_account.deletePlaylist(data[1])]);

            } else if (data[0].equals("e")) {

                System.out.println(Error.Error[connected_account.unsubscribeFromAPlaylist(data[1])]);

            } else if (data[0].equals("f")) {

                current_playlist = connected_account.getPlaylistAtIndex(data[1]);
                if (current_playlist == null) System.out.println(Error.Error[8]);
                else return StateHandler.PLAYLIST;
            }

        }
    };



    /**
     * extract a title and the artists from a string that looks like this '[a-z] <$title$> <$name$>'
     * @param input : user input
     * @return String[2] array
     * */
    private static String[] extractTitleAndArtist(String input) {
        //the regex pattern
        String regex = ".\\s+<\\$(.*?)\\$>\\s+<\\$(.*?)\\$>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        // Array to hold the title and artist
        String[] result = new String[2];

        // Extract title and artist if the pattern matches
        if (matcher.find()) {
            result[0] = matcher.group(1); // Title
            result[1] = matcher.group(2); // Artist
        } else {
            // Handle the case where the pattern does not match
            return null;
        }

        return result;
    }

    /** the playlist section state */
    public static final State PLAYLIST = () -> {
        System.out.println("**********************************************************************");
        System.out.println("PLAYLIST: Enter 'a' to show the songs in  the playlist");
        System.out.println("PLAYLIST: Enter 'b' to show the members of the collaborative playlist");
        System.out.println("PLAYLIST: Enter 'c name' to add a person to the collaborative playlist");
        System.out.println("PLAYLIST: Enter 'd <$title$> <$artist$>' to add a song to the playlist");
        System.out.println("PLAYLIST: Enter 'e <$title$> <$artist$>' to delete a song from the playlist");
        System.out.println("PLAYLIST: Enter 'f <$title$> <$artist$>' to select a song from the playlist");
        System.out.println("PLAYLIST: Enter 'g' to return to the playlist section");
        System.out.println("**********************************************************************");
        Scanner sc = new Scanner(System.in);
        String[] data;
        String input;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("a")){

                current_playlist.showSongs();
                continue;

            } else if(input.equals("b")){

                if(current_playlist instanceof CollaborativePlaylist){
                    ((CollaborativePlaylist)current_playlist).showMembers();
                }else{
                    System.out.println(Error.Error[10]);
                }
                continue;

            } else if(input.equals("g")){
                current_playlist = null;
                return StateHandler.PLAYLIST_SECTION;
            }

            data = input.split("\\s+");

            if (data.length==2 && data[0].equals("c")) {
                System.out.println(Error.Error[ connected_account.addFriendToACollaborativeList(data[1],current_playlist) ]);
            }

            data  = extractTitleAndArtist(input);
            if(data==null) {System.out.println("wrong format");continue;}

            if (input.charAt(0)=='d') {

                Song s = Data.getSong(data[0],data[1]);
                if(s==null){
                    System.out.println(Error.Error[14]);
                }else {
                    if(current_playlist.addSong(s)){
                        System.out.println(Error.Error[0]);
                    }else{
                        System.out.println(Error.Error[15]);
                    }
                }


            } else if (input.charAt(0)=='e') {

                Song s = Data.getSong(data[0],data[1]);
                if(s==null){
                    System.out.println(Error.Error[14]);
                }else {
                    if(current_playlist.removeSong(s)){
                        System.out.println(Error.Error[0]);
                    }else{
                        System.out.println(Error.Error[14]);
                    }
                }


            } else if (input.charAt(0)=='f') {
                Song s = Data.getSong(data[0],data[1]);
                if(s==null || !current_playlist.containsSong(s)){
                    System.out.println(Error.Error[14]);
                }else{
                    current_song = s;
                    return StateHandler.SONG;
                }

            }

        }
    };


    /** the song section state */

    public static final State SONG = () -> {
        System.out.println("**********************************************************************");
        System.out.println(current_song);
        System.out.println("SONG: Enter 'a' to play the song");
        System.out.println("SONG: Enter 'b name' to recommend song to a friend");
        System.out.println("SONG: Enter 'c' to go back");
        System.out.println("**********************************************************************");
        Scanner sc = new Scanner(System.in);
        String[] data;
        String input;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("a")){
                connected_account.play(current_song);
            }else if(input.equals("c")){
                current_song=null;
                if(current_playlist!=null) return StateHandler.PLAYLIST_SECTION;
                return StateHandler.HOME;
            }
            data = input.split("\\s+");
            if (data.length==2 && data[0].equals("b")) {
                System.out.println(Error.Error[connected_account.recommendSongToFriend(data[1],current_song)]);
            }

        }
    };




    /** the notification section state */
    public static final State NOTIFICATIONS_SECTION = () -> {
        System.out.println("**********************************************************************");
        System.out.println("NOTIFICATIONS_SECTION: Enter 'a' to show only new notifications(less than 24hours)");
        System.out.println("NOTIFICATIONS_SECTION: Enter 'b' to show all notifications");
        System.out.println("NOTIFICATIONS_SECTION: Enter 'c' to make a blind test");
        System.out.println("NOTIFICATIONS_SECTION: Enter 'd id' to check or respond to a blind test at your notifications holding 'id' as an id ");
        System.out.println("NOTIFICATIONS_SECTION: Enter 'e id' to check on your test as an admin ");
        System.out.println("NOTIFICATIONS_SECTION: Enter 'f' to go to home Page");
        System.out.println("**********************************************************************");

        Scanner sc = new Scanner(System.in);
        String[] data;
        String input;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("a")){
                ArrayList<String> list = connected_account.showNewNotifications();
                for (String st:list) System.out.println(st);
                continue;
            }else if(input.equals("b")){
                ArrayList<String> list = connected_account.showNotifications();
                for (String st:list) System.out.println(st);
                continue;
            }else if(input.equals("c")){
                return StateHandler.MAKE_TEST;
            }else if(input.equals("f")){
                return StateHandler.HOME;
            }
            data = input.split("\\s+");
            if(data.length==2 && data[0].equals("d")) {
                current_test = connected_account.getMyTest(data[1]);
                if(current_test!=null){System.out.println("you're the creator of the test you can't respond to it, you can check it with e id");continue;}
                current_test = connected_account.getBlindTest(data[1]);
                if(current_test==null){
                    System.out.println("test not found");continue;
                }
                return StateHandler.TAKE_TEST;
            }if(data.length==2 && data[0].equals("e")) {
                current_test = connected_account.getMyTest(data[1]);
                if(current_test==null){System.out.println("test not found in the blind tests that you created");continue;}
                return StateHandler.MAKE_TEST;
            }

        }
    };




    /** the make test section state */
    public static final State MAKE_TEST = () -> {
        System.out.println("**********************************************************************");
        System.out.println("MAKE_TEST: Enter 'a mode' to make a new blind test with a mode (1,2,3)");
        System.out.println("MAKE_TEST: Enter 'b' to get the status of the blind test");
        System.out.println("MAKE_TEST: Enter 'c lyric' to add a one guess question, then enter 'c <$title$> <$artist$>' for the solution");
        System.out.println("MAKE_TEST: Enter 'd' to launch the bling test");
        System.out.println("MAKE_TEST: Enter 'e name' to invite a friend to take bling test");
        System.out.println("MAKE_TEST: Enter 'f' to close the blind test");
        System.out.println("SONG: Enter 'e' to go to Notification Page");
        System.out.println("**********************************************************************");

        Scanner sc = new Scanner(System.in);
        String[] data;
        String input;
        while (true){
            input = sc.nextLine().strip();
            if(input.equals("b")){

                if(current_test==null){System.out.println("no current test created");continue;}
                current_test.status();
                continue;

            }else if(input.equals("d")){

                if(current_test==null){System.out.println("no current test created");continue;}
                else if(!current_test.isBeingBuilt()){System.out.println(Error.Error[18]);}
                else if(current_test.isEmpty()){System.out.println(Error.Error[16]);}
                else if(current_test.isClosed()){System.out.println(Error.Error[20]);}
                else {current_test.launch();System.out.println("launched!!!!");}
                continue;

            }else if(input.equals("f")){

                if(current_test==null){System.out.println("no current test created");continue;}
                else if(current_test.isBeingBuilt()){System.out.println(Error.Error[17]);}
                else if(current_test.isClosed()){System.out.println(Error.Error[20]);}
                else {current_test.close(connected_account);System.out.println("test closed !!!!");}
                continue;

            }else if(input.equals("e")){
                current_test=null;
                return StateHandler.NOTIFICATIONS_SECTION;
            }

            data = input.split("\\s+");
            if(data.length<2) continue;

            if(data[0].equals("a")){
                int mode;
                try {
                    mode = Integer.parseInt(data[1]);
                }catch (Exception e){
                    System.out.println("not right format");continue;
                }
                if(mode<1 || mode>3){System.out.println("not right format");continue;}
                current_test = new BlindTest(connected_account,mode);
                System.out.println("test created");

            }else if (data[0].equals("e")) {
                if(current_test==null){System.out.println("no current test created");continue;}
                int a = connected_account.addFriendToBlindTest(data[1],current_test);
                System.out.println(Error.Error[a]);

            }else if(data[0].equals("c")) {
                if(current_test==null){System.out.println("no current test created");continue;}
                String lyr = input.substring(1).strip();
                System.out.println("   Enter the solution to the lyric above");
                System.out.println("   c <$title$> <$artist$>");
                data = extractTitleAndArtist(sc.nextLine().strip());
                if(data==null){System.out.println("wrong format");continue;}
                System.out.println(Error.Error[current_test.addLyricQuestion(lyr,data[0],data[1])]);
            }

        }




    };




    /** the respond to test section state */
    public static final State TAKE_TEST = () -> {
        System.out.println("**********************************************************************");
        current_test.status();
        System.out.println("TAKE_TEST: Enter 'b' to get the status of the blind test");
        System.out.println("TAKE_TEST: Enter 'c response' to respond to the test");
        current_test.responseShape();
        System.out.println("TAKE_TEST: Enter 'e' to go to Notification Page");
        System.out.println("**********************************************************************");

        Scanner sc = new Scanner(System.in);
        String[] data;
        String input;
        while (true) {
            input = sc.nextLine().strip();
            if (input.equals("b")) {
                if (current_test == null) {
                    System.out.println("no current test created");
                }
                current_test.status();
                continue;
            }else if(input.equals("e")){
                current_test=null;
                return StateHandler.NOTIFICATIONS_SECTION;
            }

            data = input.split("\\s+");
            if( data.length==2 &&  data[0].equals("c")) {
                input = input.substring(1).strip();
                System.out.println(input);
                System.out.println(Error.Error[current_test.respond(connected_account,input)]);
            }

        }
    };



    /** the search section state */

    public static final State SEARCH = () -> {
        System.out.println("**********************************************************************");
        System.out.println("SEARCH:   search  param  expr   to search a song by title and artist");
        System.out.println("SEARCH:   param = a : sorted by alphabetically");
        System.out.println("SEARCH:   param = l : sorted by the number of time you listened to the song");
        System.out.println("SEARCH:   param = r : sorted by the number of recommendations ");
        System.out.println("SEARCH:   press a <$title$> <$artist$> to play a song");
        System.out.println("SEARCH:   press c to return to Home page ");
        System.out.println("**********************************************************************");

        Scanner sc = new Scanner(System.in);
        String[] data;
        String input;
        while (true) {
            input = sc.nextLine().strip();
            if (input.equals("c")) {
                return StateHandler.HOME;
            }

            data = input.split("\\s+");
            if (data.length > 2 && data[0].equals("search") && data[1].matches("[alr]")) {

                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < data.length; i++) sb.append(data[i]);


                ArrayList<Song> res = Data.search(sb.toString(), data[1], connected_account);
                ArrayList<String> art = Data.searchArtists(sb.toString());

                System.out.println("Songs:");
                if (res.isEmpty()) System.out.println("no songs found");
                else for (Song s : res) System.out.println(s.toString());

                System.out.println("Artists:");
                if (art.isEmpty()) System.out.println("no artist found");
                else for (String a : art) System.out.println(a);

            }else if (data[0].equals("a")){
                String[] TA = extractTitleAndArtist(input);
                if(TA == null){System.out.println("wrong format!!");
                } else{
                    Song s = Data.getSong(TA[0],TA[1]);
                    if(s==null){System.out.println(Error.Error[14]);continue;}
                    connected_account.play(s);
                }
            }else {
                System.out.println("wrong format!!!!!");
            }

        }
    };





}
