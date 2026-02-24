package service.responses;

public class ResponseException extends Exception{
    public ResponseException(String message) {
        super(message);
    }
    public ResponseException(String message, Throwable ex) {
        super(message, ex);
    }
}
