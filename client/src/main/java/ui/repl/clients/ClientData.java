package ui.repl.clients;

import model.GameData;

import java.util.Collection;

public class ClientData {
    private String username;
    private String authToken;

    public ClientData() {}

    public ClientData(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
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
}
