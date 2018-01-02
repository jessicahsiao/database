package db;

/**
 * Created by jessicahsiao on 3/6/17.
 */
public class NoTableException extends RuntimeException {
    public NoTableException(String message){
        super(message);
    }
}
