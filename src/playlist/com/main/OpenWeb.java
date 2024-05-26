package playlist.com.main;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * class that has the browse method
 */
public class OpenWeb {


    private static final String GOOGLE_SEARCH_URL = "https://www.google.com/search?hl=en&tbm=vid&q=";

    /**
     * opens a web search page of the song with the desktop (if supported) default navigator
     * @param title
     * @param artist
    @return void
     */
    public static void browse(String title, String artist) {
        // URL of the YouTube video
        String url = GOOGLE_SEARCH_URL+title+","+artist;
        url=url.replaceAll("\\s+","%20");

        // Check if Desktop is supported on the current platform
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                // Open the URL in the default web browser
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Desktop is not supported on this platform.");
        }
    }


}