package service.responses;

public class ResponseException extends Exception {

    public int httpCode = 500;

    public ResponseException(String message) {
        super(message);
    }
    public ResponseException(String message, Throwable ex) {
        super(message, ex);
    }
    public ResponseException(int httpCode, String message) {
        super(message);
        this.httpCode = httpCode;
    }
}
