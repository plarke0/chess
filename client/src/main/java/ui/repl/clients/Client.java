package ui.repl.clients;

public interface Client {
    ClientResponse eval(String line, ClientData currentClientData);
    String getPromptTitle();
}
