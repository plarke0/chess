package client;

import requests.*;
import responses.*;

import java.util.HashMap;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;
    HttpCommunicator httpCommunicator;

    public ServerFacade(String url) {
        serverUrl = url;
        httpCommunicator = new HttpCommunicator(serverUrl);
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) throws ResponseException{
        String path = "/user";
        return httpCommunicator.makeRequest("POST", path, registerRequest, null, RegisterResponse.class);
    }

    public LoginResponse loginUser(LoginRequest loginRequest) throws ResponseException{
        String path = "/session";
        return httpCommunicator.makeRequest("POST", path, loginRequest, null, LoginResponse.class);
    }

    public void logoutUser(String authToken) throws ResponseException {
        String path = "/session";
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", authToken);
        httpCommunicator.makeRequest("DELETE", path, null, headers, null);
    }

    public ListGamesResponse listGames(String authToken) throws ResponseException {
        String path = "/game";
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", authToken);
        return httpCommunicator.makeRequest("GET", path, null, headers, ListGamesResponse.class);
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest, String authToken) throws ResponseException {
        String path = "/game";
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", authToken);
        return httpCommunicator.makeRequest("POST", path, createGameRequest, headers, CreateGameResponse.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest, String authToken) throws ResponseException {
        String path = "/game";
        Map<String, String> headers = new HashMap<>();
        headers.put("authorization", authToken);
        httpCommunicator.makeRequest("PUT", path, joinGameRequest, headers, null);
    }

    public void clear() throws ResponseException {
        String path = "/db";
        httpCommunicator.makeRequest("DELETE", path, null, null, null);
    }
}
