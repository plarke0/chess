package ui.repl.clients;

import client.ServerFacade;
import model.GameData;
import ui.ChessBoard;

public class GameClient implements Client{

    private final String serverURL;
    private final ServerFacade serverFacade;

    public GameClient(String serverURL) {
        this.serverURL = serverURL;
        this.serverFacade = new ServerFacade(this.serverURL);
    }

    public ClientResponse eval(String line, ClientData currentClientData) {
        return null;
    }

    public String getPromptTitle() {
        return "IN_GAME";
    }

    private ClientResponse help() {
        return null;
    }

    private ClientResponse exit() {
        return null;
    }
    public void drawBoard(ClientData clientData) {
        String user = clientData.getUsername();
        GameData activeGame = clientData.getActiveGame();
        Boolean isWhiteView = !user.equals(activeGame.blackUsername());
        ChessBoard.drawBoard(activeGame.game().getBoard(), isWhiteView);
    }
}