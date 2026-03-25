package ui.repl.clients;

import client.ServerFacade;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import responses.RegisterResponse;
import responses.ResponseException;

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
                case "login" -> login(params, currentClientData);
                case "register" -> register(params, currentClientData);
                case null, default -> unrecognisedCommand(cmd);
            };
        } catch (IllegalArgumentException | ResponseException ex) {
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

    private ClientResponse login(String[] params, ClientData currentClientData) throws ResponseException {
        validateCommand(params, 2);
        LoginRequest loginRequest = new LoginRequest(params[0], params[1]);
        LoginResponse loginResponse = serverFacade.loginUser(loginRequest);
        ClientData newClientData = new ClientData(loginResponse.username(), loginResponse.authToken(), currentClientData.getActiveGames());
        return new ClientResponse("signedInClient", newClientData, "Logged in as " + loginResponse.username());
    }

    private ClientResponse register(String[] params, ClientData currentClientData) throws ResponseException {
        validateCommand(params, 3);
        RegisterRequest registerRequest = new RegisterRequest(params[0], params[1], params[2]);
        RegisterResponse registerResponse = serverFacade.registerUser(registerRequest);
        ClientData newClientData = new ClientData(registerResponse.username(), registerResponse.authToken(), currentClientData.getActiveGames());
        return new ClientResponse("signedInClient", newClientData, "Logged in as " + registerResponse.username());
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
