package ui.repl.clients;

import model.GameData;

public class ClientData {
    private String username;
    private String authToken;
    private GameData activeGame;

    public ClientData() {}

    public ClientData(String username, String authToken, GameData activeGame) {
        this.username = username;
        this.authToken = authToken;
        this.activeGame = activeGame;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public GameData getActiveGame() {
        return activeGame;
    }

    public void setActiveGame(GameData activeGame) {
        this.activeGame = activeGame;
    }
}
