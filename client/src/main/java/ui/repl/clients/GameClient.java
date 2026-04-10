package ui.repl.clients;

import chess.ChessPosition;
import client.ServerFacade;
import client.WebSocketFacade;
import model.GameData;
import ui.ChessBoard;
import websocket.commands.UserGameCommand;
import static websocket.commands.UserGameCommand.CommandType.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.repl.clients.ClientMethods.*;

public class GameClient implements Client{

    private final String serverURL;
    private final ServerFacade serverFacade;
    private final WebSocketFacade webSocketFacade;
    private ClientData currentClientData;

    private final ChessBoard chessBoard;

    public GameClient(String serverURL, WebSocketFacade webSocketFacade) {
        this.serverURL = serverURL;
        this.serverFacade = new ServerFacade(this.serverURL);
        this.webSocketFacade = webSocketFacade;
        this.chessBoard = new ChessBoard();
    }

    public ClientResponse eval(String line, ClientData currentClientData) {
        try {
            this.currentClientData = currentClientData;
            String[] args = line.toLowerCase().split(" ");
            String cmd = (args.length > 0) ? args[0] : null;
            String[] params = Arrays.copyOfRange(args, 1, args.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "highlight" -> highlight(params);
                case "move" -> move();
                case "leave" -> leave();
                case "resign" -> resign();
                case "help" -> help();
                case null, default -> unrecognisedCommand(cmd);
            };
        } catch (IllegalArgumentException | IllegalStateException ex) {
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

    private ClientResponse highlight(String[] params) throws IllegalArgumentException{
        validateCommand(params, 1);
        String sourceString = params[0];
        validatePositionString(sourceString);

        String user = currentClientData.getUsername();
        GameData activeGame = currentClientData.getActiveGame();
        Boolean isWhiteView = !user.equals(activeGame.blackUsername());

        ChessPosition sourcePosition = decryptChessPosition(sourceString);

        chessBoard.drawHighlightedBoard(activeGame.game().getBoard(), isWhiteView, sourcePosition);
        return new ClientResponse(null, null, "\n");
    }

    private ClientResponse move() {
        return null;
    }

    private ClientResponse leave() throws IOException, IllegalStateException {
        if (currentClientData.getActiveGame() == null) {
            throw new IllegalStateException("Error: Not currently in a game");
        }

        UserGameCommand leaveCommand = new UserGameCommand(LEAVE, currentClientData.getAuthToken(), currentClientData.getActiveGame().gameID());
        webSocketFacade.sendCommand(leaveCommand);

        ClientData newClientData = new ClientData(currentClientData.getUsername(), currentClientData.getAuthToken(), null);
        return new ClientResponse(
                "signedInClient",
                newClientData,
                "Left '" + currentClientData.getActiveGame().gameName() + "'"
        );
    }

    private ClientResponse resign() throws IOException {
        if (currentClientData.getActiveGame() == null) {
            throw new IllegalStateException("Error: Not currently in a game");
        }

        return new ClientResponse("resignClient", null, "\n");
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
        chessBoard.drawBoard(activeGame.game().getBoard(), isWhiteView);
    }

    private ChessPosition decryptChessPosition(String position) {
        char rowChar = position.charAt(1);
        char columnChar = position.charAt(0);

        int rowInt = rowChar - '1' + 1;
        int columnInt = columnChar - 'a' + 1;

        return new ChessPosition(rowInt, columnInt);
    }

    private static void validatePositionString(String position) throws IllegalArgumentException {
        if (position.length() != 2) {
            throw new IllegalArgumentException(
                    "'" + position + "' is not a valid chess position"
            );
        }

        Pattern pattern = Pattern.compile("[abcdefgh][12345678]");
        Matcher matcher = pattern.matcher(position);
        if (!matcher.find()) {
            throw new IllegalArgumentException(
                    "'" + position + "' is not a valid chess position"
            );
        }
    }
}