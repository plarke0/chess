package responses;

import com.google.gson.Gson;

import java.util.HashMap;

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
    public static ResponseException fromJson(String json) {
        var map = new Gson().fromJson(json, HashMap.class);
        var status = ((Double)map.get("status")).intValue();
        String message = map.get("message").toString();
        return new ResponseException(status, message);
    }
}
