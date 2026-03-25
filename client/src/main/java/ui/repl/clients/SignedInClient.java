package ui.repl.clients;

import client.ClientCommunicator;
import client.ServerFacade;
import responses.ResponseException;

import java.util.Arrays;

public class SignedInClient implements Client{

    private final String serverURL;
    private final ServerFacade serverFacade;

    public SignedInClient(String serverURL) {
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
                case "logout" -> logout(currentClientData);
                case null, default -> unrecognisedCommand(cmd);
            };
        } catch (IllegalArgumentException | ResponseException ex) {
            String msg = ex.getMessage();
            return new ClientResponse(null, null, msg);
        }
    }

    public String getPromptTitle() {
        return "LOGGED_IN";
    }

    private ClientResponse help() {
        String msg = """
                create <NAME> - create a new game
                list - get a list of all games
                join <ID> [WHITE|BLACK] - join a game
                observe <ID> - observe a game
                logout - log out of the current account
                help - list possible commands
                """;
        return new ClientResponse(null, null, msg);
    }

    private ClientResponse logout(ClientData currentClientData) throws ResponseException {
        serverFacade.logoutUser(currentClientData.getAuthToken());
        ClientData newClientData = new ClientData(null, null, currentClientData.getActiveGames());
        return new ClientResponse("signedOutClient", newClientData, "Successfully logged out");
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

    private ClientResponse unrecognisedCommand(String command) throws IllegalArgumentException {
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("No command was provided. Type 'help' for a list of available commands.");
        } else {
            throw new IllegalArgumentException("'" + command + "' is not a valid command. Type 'help' for a list of available commands.");
        }
    }

    private void validateCommand(String[] params, int argCount) throws IllegalArgumentException {
        if (params.length < argCount) {
            throw new IllegalArgumentException("Only " + params.length + " arguments were given when " + argCount + " was/were needed.");
        }
    }
}