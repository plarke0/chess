package ui.repl.clients;

import chess.ChessBoard;
import chess.ChessGame;
import client.ServerFacade;
import model.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import responses.ResponseException;

import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

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
                case "create" -> createGame(params, currentClientData);
                case "list" -> listGames(currentClientData);
                case "join" -> joinGame(params, currentClientData);
                case "observe" -> observeGame(params, currentClientData);
                case null, default -> unrecognisedCommand(cmd);
            };
        } catch (IllegalArgumentException | ResponseException ex) {
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

    private ClientResponse logout(ClientData currentClientData) throws ResponseException {
        serverFacade.logoutUser(currentClientData.getAuthToken());
        ClientData newClientData = new ClientData(null, null, null);
        return new ClientResponse("signedOutClient", newClientData, "Successfully logged out");
    }

    private ClientResponse createGame(String[] params, ClientData currentClientData) throws ResponseException {
        validateCommand(params, 1);
        CreateGameRequest createGameRequest = new CreateGameRequest(params[0]);
        CreateGameResponse createGameResponse = serverFacade.createGame(createGameRequest, currentClientData.getAuthToken());
        return new ClientResponse(null, null, "Created new game named '" + params[0] + "'");
    }

    private ClientResponse listGames(ClientData currentClientData) throws ResponseException {
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

    private ClientResponse joinGame(String[] params, ClientData currentClientData) throws ResponseException {
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
        GameData gameData = getGameData(gameID);
        ClientData newClientData = new ClientData(currentClientData.getUsername(), currentClientData.getAuthToken(), gameData);
        return new ClientResponse("gameClient", newClientData, "Successfully joined game " + gameID + " as player " + params[1]);
    }

    private ClientResponse observeGame(String[] params, ClientData currentClientData) {
        validateCommand(params, 1);
        int gameID;
        try {
            gameID = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The given game ID of '" + params[0] + "' is not an integer");
        }
        GameData gameData = getGameData(gameID);
        ClientData newClientData = new ClientData(currentClientData.getUsername(), currentClientData.getAuthToken(), gameData);
        return new ClientResponse("gameClient", newClientData, "Now observing game " + gameID);
    }

    private ClientResponse unrecognisedCommand(String command) throws IllegalArgumentException {
        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("No command was provided. Type 'help' for a list of available commands");
        } else {
            throw new IllegalArgumentException("'" + command + "' is not a valid command. Type 'help' for a list of available commands");
        }
    }

    private void validateCommand(String[] params, int argCount) throws IllegalArgumentException {
        if (params.length < argCount) {
            throw new IllegalArgumentException(
                    "Only " + params.length
                            + (((params.length == 1)) ? " argument was given when " : " arguments were given when ")
                            + argCount + " "
                            + ((argCount == 1) ? "was" : "were") + " needed"
            );
        }
    }

    private GameData getGameData(int gameID) {
        ChessBoard mockBoard = new ChessBoard();
        mockBoard.resetBoard();
        ChessGame mockGame = new ChessGame();
        mockGame.setBoard(mockBoard);
        return new GameData(
                gameID,
                null,
                null,
                "mock",
                mockGame
        );
    }
}