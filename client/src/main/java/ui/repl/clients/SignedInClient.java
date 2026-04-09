package ui.repl.clients;

import chess.ChessBoard;
import chess.ChessGame;
import client.ServerFacade;
import client.WebSocketFacade;
import model.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.ResponseException;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.repl.clients.ClientMethods.*;

public class SignedInClient implements Client{

    private final String serverURL;
    private final ServerFacade serverFacade;
    private final WebSocketFacade webSocketFacade;
    private ClientData currentClientData;

    public SignedInClient(String serverURL, WebSocketFacade webSocketFacade) {
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
                case "help" -> help();
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case null, default -> unrecognisedCommand(cmd);
            };
        } catch (IllegalArgumentException | ResponseException | IOException ex) {
            String msg = ex.getMessage();
            return new ClientResponse(null, null, SET_TEXT_COLOR_RED + msg);
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
                help - list possible commands""";
        return new ClientResponse(null, null, msg);
    }

    private ClientResponse logout() throws ResponseException {
        serverFacade.logoutUser(currentClientData.getAuthToken());
        ClientData newClientData = new ClientData(null, null, null);
        return new ClientResponse("signedOutClient", newClientData, "Successfully logged out");
    }

    private ClientResponse createGame(String[] params) throws ResponseException {
        validateCommand(params, 1);
        CreateGameRequest createGameRequest = new CreateGameRequest(params[0]);
        CreateGameResponse createGameResponse = serverFacade.createGame(createGameRequest, currentClientData.getAuthToken());
        return new ClientResponse(null, null, "Created new game named '" + params[0] + "'");
    }

    private ClientResponse listGames() throws ResponseException {
        ListGamesResponse listGamesResponse = serverFacade.listGames(currentClientData.getAuthToken());
        ClientData newClientData = new ClientData(currentClientData.getUsername(), currentClientData.getAuthToken(), null);

        StringBuilder result = new StringBuilder("The following games are active:");
        for (GameData gameData : listGamesResponse.games()) {
            int gameID = gameData.gameID();
            String gameName = gameData.gameName();
            String whiteUser = gameData.whiteUsername();
            String blackUser = gameData.blackUsername();
            result.append("\n").append(gameID).append(". Game Name: ").append(gameName);
            result.append(" | White: ").append(whiteUser).append(" | Black: ").append(blackUser);
        }
        return new ClientResponse(null, newClientData, result.toString());
    }

    private ClientResponse joinGame(String[] params) throws ResponseException, IOException {
        validateCommand(params, 2);
        int gameID;
        try {
            gameID = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The given game ID of '" + params[0] + "' is not an integer");
        }
        String playerColor = params[1].toUpperCase();

        JoinGameRequest joinGameRequest = new JoinGameRequest(gameID, playerColor);
        serverFacade.joinGame(joinGameRequest, currentClientData.getAuthToken());
        UserGameCommand joinCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, currentClientData.getAuthToken(), gameID);
        webSocketFacade.sendCommand(joinCommand);
        return new ClientResponse("gameClient", null, "Successfully joined game " + gameID + " as player " + params[1]);
    }

    private ClientResponse observeGame(String[] params) throws IOException {
        validateCommand(params, 1);
        int gameID;
        try {
            gameID = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The given game ID of '" + params[0] + "' is not an integer");
        }
        UserGameCommand joinCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, currentClientData.getAuthToken(), gameID);
        webSocketFacade.sendCommand(joinCommand);
        return new ClientResponse("gameClient", null, "Now observing game " + gameID);
    }
}