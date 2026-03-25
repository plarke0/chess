package client;

import requests.*;
import responses.*;

public class ServerFacade {
    private final String serverUrl;
    ClientCommunicator clientCommunicator;

    public ServerFacade(String url) {
        serverUrl = url;
        clientCommunicator = new ClientCommunicator(serverUrl);
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) throws ResponseException{
        String path = "/user";
        return clientCommunicator.makeRequest("POST", path, registerRequest, RegisterResponse.class);
    }

    public LoginResponse loginUser(LoginRequest loginRequest) throws ResponseException{
        String path = "/session";
        return clientCommunicator.makeRequest("POST", path, loginRequest, LoginResponse.class);
    }

    public void logoutUser(String authToken) {
        ;
    }

    public ListGamesResponse listGames(String authToken) {
        return null;
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest, String authToken) {
        return null;
    }

    public void joinGame(JoinGameRequest joinGameRequest, String authToken) {
        ;
    }
}
