package db;

/**
 * Created by Mia on 3/6/17.
 */
public class CreateTableException extends RuntimeException {
    public CreateTableException(String message) {
        super(message);
    }
}