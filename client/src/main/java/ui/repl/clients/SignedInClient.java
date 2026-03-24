package ui.repl.clients;

import client.ServerFacade;

public class SignedInClient implements Client{

    private final String serverURL;
    private final ServerFacade serverFacade;

    public SignedInClient(String serverURL) {
        this.serverURL = serverURL;
        this.serverFacade = new ServerFacade(this.serverURL);
    }

    public ClientResponse eval(String line) {
        return null;
    }

    public String getPromptTitle() {
        return "LOGGED_IN";
    }

    private ClientResponse help() {
        return null;
    }

    private ClientResponse logout() {
        return null;
    }

    private ClientResponse createGame() {
        return null;
    }

    private ClientResponse listGames() {
        return null;
    }

    private ClientResponse playGame() {
        return null;
    }

    private ClientResponse observeGame() {
        return null;
    }
}