package ui.repl.clients;

import client.ServerFacade;
import model.GameData;
import ui.ChessBoard;

import java.util.Arrays;

public class GameClient implements Client{

    private final String serverURL;
    private final ServerFacade serverFacade;

    public GameClient(String serverURL) {
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
                case "exit" -> exit(currentClientData);
                case null, default -> unrecognisedCommand(cmd);
            };
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            return new ClientResponse(null, null, msg);
        }
    }

    public String getPromptTitle() {
        return "IN_GAME";
    }

    private ClientResponse help() {
        String msg = """
                More to come in Phase 6
                exit - leave the game
                help - list possible commands
                """;
        return new ClientResponse(null, null, msg);
    }

    private ClientResponse exit(ClientData currentClientData) {
        ClientData newClientData = new ClientData(currentClientData.getUsername(), currentClientData.getAuthToken(), null);
        return new ClientResponse("signedInClient", newClientData, "Left '" + currentClientData.getActiveGame().gameName() + "'");
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

    public void drawBoard(ClientData clientData) {
        String user = clientData.getUsername();
        GameData activeGame = clientData.getActiveGame();
        Boolean isWhiteView = !user.equals(activeGame.blackUsername());
        ChessBoard.drawBoard(activeGame.game().getBoard(), isWhiteView);
    }
}