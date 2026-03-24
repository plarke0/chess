package ui.repl.clients;

import model.GameData;

import java.util.Collection;

public class ClientData {
    private String username;
    private String authToken;
    private Collection<GameData> activeGames;

    public ClientData() {}

    public ClientData(String username, String authToken, Collection<GameData> activeGames) {
        this.username = username;
        this.authToken = authToken;
        this.activeGames = activeGames;
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

    public Collection<GameData> getActiveGames() {
        return activeGames;
    }

    public void setActiveGames(Collection<GameData> activeGames) {
        this.activeGames = activeGames;
    }
}
