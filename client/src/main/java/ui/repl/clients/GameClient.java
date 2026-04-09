package ui.repl.clients;

import client.ServerFacade;
import client.WebSocketFacade;
import model.GameData;
import ui.ChessBoard;

import java.io.IOException;
import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.repl.clients.ClientMethods.*;

public class GameClient implements Client{

    private final String serverURL;
    private final ServerFacade serverFacade;
    private final WebSocketFacade webSocketFacade;
    private ClientData currentClientData;

    public GameClient(String serverURL, WebSocketFacade webSocketFacade) {
        this.serverURL = serverURL;
        this.serverFacade = new ServerFacade(this.serverURL);
        this.webSocketFacade = webSocketFacade;
    }

    public ClientResponse eval(String line, ClientData currentClientData) {
        try {
            this.currentClientData = currentClientData;
            String[] args = line.toLowerCase().split(" ");
            String cmd = (args.length > 0) ? args[0] : null;
            String[] params = Arrays.copyOfRange(args, 1, args.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "highlight" -> highlight();
                case "move" -> move();
                case "leave" -> leave();
                case "resign" -> resign();
                case "help" -> help();
                case null, default -> unrecognisedCommand(cmd);
            };
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage();
            return new ClientResponse(null, null, SET_TEXT_COLOR_RED + msg);
        } catch (IOException ex) {
            return new ClientResponse(null, null, SET_TEXT_COLOR_RED + "Error: There was a problem with your connection");
        }
    }

    public String getPromptTitle() {
        return "IN_GAME";
    }

    private ClientResponse redraw() {
        drawBoard(currentClientData);
        return new ClientResponse(null, null, "\n");
    }

    private ClientResponse highlight() {
        return null;
    }

    private ClientResponse move() {
        return null;
    }

    private ClientResponse leave() {

        ClientData newClientData = new ClientData(currentClientData.getUsername(), currentClientData.getAuthToken(), null);
        String result;
        if (currentClientData.getActiveGame() != null) {
            result = "Left '" + currentClientData.getActiveGame().gameName() + "'";
        } else {
            result = "Left game";
        }
        return new ClientResponse(
                "signedInClient",
                newClientData,
                result
        );
    }

    private ClientResponse resign() {
        return null;
    }

    private ClientResponse help() {
        String msg = """
                redraw - redraws the chess board
                highlight <source> - highlights the available moves of the piece at source
                move <source> <destination> <promotion (optional)> - moves the piece at source to destination
                leave - leave the game
                resign - resign the game
                help - list possible commands""";
        return new ClientResponse(null, null, msg);
    }

    public void drawBoard(ClientData clientData) {
        String user = clientData.getUsername();
        GameData activeGame = clientData.getActiveGame();
        Boolean isWhiteView = !user.equals(activeGame.blackUsername());
        ChessBoard.drawBoard(activeGame.game().getBoard(), isWhiteView);
    }
}