package client;

import com.google.gson.Gson;
import responses.ResponseException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientCommunicator {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverURL;

    public ClientCommunicator(String serverURL) {
        this.serverURL = serverURL;
    }

    public <T> T makeRequest(String method, String path, Object body, Class<T> responseClass) throws ResponseException {
        try {
            var request = buildRequest(method, path, body);
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return handleResponse(response, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(status, "other failure: " + status);
        }

        if (responseClass == null) {
            return null;
        }

        return new Gson().fromJson(response.body(), responseClass);
    }

    private Boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
