package ui.repl.clients;

import client.ServerFacade;
import client.WebSocketFacade;
import ui.ChessBoard;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.repl.clients.ClientMethods.unrecognisedCommand;
import static websocket.commands.UserGameCommand.CommandType.LEAVE;
import static websocket.commands.UserGameCommand.CommandType.RESIGN;

public class ResignClient implements Client {

    private final WebSocketFacade webSocketFacade;
    private ClientData currentClientData;

    public ResignClient(WebSocketFacade webSocketFacade) {
        this.webSocketFacade = webSocketFacade;
    }

    @Override
    public ClientResponse eval(String line, ClientData currentClientData) {
        try {
            this.currentClientData = currentClientData;
            String[] args = line.toLowerCase().split(" ");
            String cmd = (args.length > 0) ? args[0] : null;
            String[] params = Arrays.copyOfRange(args, 1, args.length);
            return switch (cmd) {
                case "y" -> confirm();
                case "n" -> cancel();
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

    @Override
    public String getPromptTitle() {
        return "CONFIRM (Y/N)";
    }

    private ClientResponse help() {
        String msg = """
                Y - confirm resigning the game
                N - cancel resigning the game
                help - list possible commands""";
        return new ClientResponse(null, null, msg);
    }

    private ClientResponse confirm() throws  IOException {
        UserGameCommand resignCommand = new UserGameCommand(RESIGN, currentClientData.getAuthToken(), currentClientData.getActiveGame().gameID());
        webSocketFacade.sendCommand(resignCommand);
        return new ClientResponse("gameClient", null, "\n");
    }

    private ClientResponse cancel() {
        return new ClientResponse("gameClient", null, "\n");
    }
}
