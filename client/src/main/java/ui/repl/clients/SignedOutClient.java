package ui.repl.clients;

import client.ServerFacade;

import java.util.Arrays;

public class SignedOutClient implements Client{

    private final String serverURL;
    private final ServerFacade serverFacade;

    public SignedOutClient(String serverURL) {
        this.serverURL = serverURL;
        this.serverFacade = new ServerFacade(this.serverURL);
    }

    public ClientResponse eval(String line, ClientData currentClientData) {
        try {
            String[] args = line.toLowerCase().split(" ");
            String cmd = (args.length > 0) ? args[0] : null;
            String[] params = Arrays.copyOfRange(args, 1, args.length);
            return switch (cmd) {
                case "help" -> help();
                case "quit" -> quit();
                case null, default -> unrecognisedCommand(cmd);
            };
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            return new ClientResponse(null, null, msg);
        }
    }

    public String getPromptTitle() {
        return "LOGGED_OUT";
    }

    private ClientResponse help() {
        String msg = """
                register <USERNAME> <PASSWORD> <EMAIL> - create a new account
                Login <USERNAME> <PASSWORD> - log in to an existing account
                quit - close the chess program
                help - list possible commands
                """;
        return new ClientResponse(null, null, msg);
    }

    private ClientResponse quit() {
        return new ClientResponse("quit", null, "Goodbye!");
    }

    private ClientResponse login() {
        return null;
    }

    private ClientResponse register() {
        return null;
    }

    private ClientResponse unrecognisedCommand(String command) throws IllegalArgumentException {
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("No command was provided. Type 'help' for a list of available commands.");
        } else {
            throw new IllegalArgumentException("'" + command + "' is not a valid command. Type 'help' for a list of available commands.");
        }
    }

    private void validateCommand(String line, int argCount) throws IllegalArgumentException {

    }
}
