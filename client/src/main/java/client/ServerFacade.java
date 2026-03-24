package client;

import requests.*;
import responses.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        return null;
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        return null;
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
