package ui.repl.clients;

import client.ServerFacade;

public class SignedOutClient implements Client{

    private final String serverURL;
    private final ServerFacade serverFacade;

    public SignedOutClient(String serverURL) {
        this.serverURL = serverURL;
        this.serverFacade = new ServerFacade(this.serverURL);
    }

    public ClientResponse eval(String line) {
        return null;
    }

    public String getPromptTitle() {
        return "LOGGED_OUT";
    }

    private ClientResponse help() {
        return null;
    }

    private ClientResponse quit() {
        return null;
    }

    private ClientResponse login() {
        return null;
    }

    private ClientResponse register() {
        return null;
    }
}
