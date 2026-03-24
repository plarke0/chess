package client;

import responses.ResponseException;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientCommunicator {
    private String serverURL;

    public HttpRequest buildRequest(String method, String path, Object body) {
        return null;
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        return null;
    }

    public HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        return null;
    }

    public <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        return null;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }
}
