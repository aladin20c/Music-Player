package playlist.com.console;


/**
 * interface that defines a State
 */
public interface State {
    /**
     * handles user input
     * @return the next state based on the user input
     */
    public State handle();
}
