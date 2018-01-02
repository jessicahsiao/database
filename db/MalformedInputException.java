package db;

/**
 * Created by jessicahsiao on 3/6/17.
 */
public class MalformedInputException extends RuntimeException{
    public MalformedInputException(String message){
        super(message);
    }
}
