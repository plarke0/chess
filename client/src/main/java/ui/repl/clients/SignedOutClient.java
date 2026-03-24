package ui.repl.clients;

public class SignedOutClient implements Client{
    public SignedOutClient(String serverURL) {

    }

    public ClientResponse eval(String line) {
        return null;
    }

    public String getPromptTitle() {
        return "LOGGED_OUT";
    }
}
