package exceptions;

/**
 * Created by Anton on 23.09.2017.
 */
public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
