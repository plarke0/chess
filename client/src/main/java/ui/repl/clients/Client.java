package ui.repl.clients;

public interface Client {
    ClientResponse eval(String line);
    String getPromptTitle();
}
