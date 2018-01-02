package db;

/**
 * Created by jessicahsiao on 3/6/17.
 */
public class BadOperationException extends RuntimeException {
    public BadOperationException(String message){
        super(message);
    }
}
