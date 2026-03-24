package ui.repl.clients;

public class SignedInClient implements Client{
    public ClientResponse eval(String line) {
        return null;
    }

    public String getPromptTitle() {
        return "LOGGED_IN";
    }
}