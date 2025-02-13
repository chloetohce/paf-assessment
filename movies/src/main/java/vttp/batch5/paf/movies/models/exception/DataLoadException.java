package vttp.batch5.paf.movies.models.exception;

public class DataLoadException extends RuntimeException {

    public DataLoadException() {
    }

    public DataLoadException(String message) {
        super(message);
    }

    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    
}
