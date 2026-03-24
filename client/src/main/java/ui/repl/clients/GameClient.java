package ui.repl.clients;

public class GameClient implements Client{
    public GameClient(String serverURL) {

    }

    public ClientResponse eval(String line) {
        return null;
    }

    public String getPromptTitle() {
        return "IN_GAME";
    }
}