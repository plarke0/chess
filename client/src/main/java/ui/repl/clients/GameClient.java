package ui.repl.clients;

import client.ServerFacade;
import model.GameData;
import ui.ChessBoard;

import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.repl.clients.ClientMethods.*;

public class GameClient implements Client{

    private final String serverURL;
    private final ServerFacade serverFacade;
    private ClientData currentClientData;

    public GameClient(String serverURL) {
        this.serverURL = serverURL;
        this.serverFacade = new ServerFacade(this.serverURL);
    }

    public ClientResponse eval(String line, ClientData currentClientData) {
        try {
            this.currentClientData = currentClientData;
            String[] args = line.toLowerCase().split(" ");
            String cmd = (args.length > 0) ? args[0] : null;
            String[] params = Arrays.copyOfRange(args, 1, args.length);
            return switch (cmd) {
                case "help" -> help();
                case "exit" -> exit();
                case null, default -> unrecognisedCommand(cmd);
            };
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            return new ClientResponse(null, null, SET_TEXT_COLOR_RED + msg);
        }
    }

    public String getPromptTitle() {
        return "IN_GAME";
    }

    private ClientResponse help() {
        String msg = """
                exit - leave the game
                help - list possible commands
                More to come in Phase 6""";
        return new ClientResponse(null, null, msg);
    }

    private ClientResponse exit() {
        ClientData newClientData = new ClientData(currentClientData.getUsername(), currentClientData.getAuthToken(), null);
        // TODO: Add check for if gameName is null
        return new ClientResponse("signedInClient", newClientData, "Left '" + currentClientData.getActiveGame().gameName() + "'");
    }

    public void drawBoard(ClientData clientData) {
        String user = clientData.getUsername();
        GameData activeGame = clientData.getActiveGame();
        Boolean isWhiteView = !user.equals(activeGame.blackUsername());
        ChessBoard.drawBoard(activeGame.game().getBoard(), isWhiteView);
    }
}