package exceptions;

/**
 * Created by Anton on 23.09.2017.
 */
public class PasswordMismatchException extends Exception {

    public PasswordMismatchException() {
    }

    public PasswordMismatchException(String message) {
        super(message);
    }
}
