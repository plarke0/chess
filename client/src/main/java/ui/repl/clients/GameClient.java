package ui.repl.clients;

import client.ServerFacade;

public class GameClient implements Client{

    private final String serverURL;
    private final ServerFacade serverFacade;

    public GameClient(String serverURL) {
        this.serverURL = serverURL;
        this.serverFacade = new ServerFacade(this.serverURL);
    }

    public ClientResponse eval(String line) {
        return null;
    }

    public String getPromptTitle() {
        return "IN_GAME";
    }
}