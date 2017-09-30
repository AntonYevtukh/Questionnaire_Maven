package exceptions;

/**
 * Created by Anton on 23.09.2017.
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
