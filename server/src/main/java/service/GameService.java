package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.database.AuthDB;
import dataaccess.memory.database.GameDB;
import model.AuthData;
import model.GameData;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.responses.CreateGameResponse;
import service.responses.ListGamesResponse;
import service.responses.ResponseException;

import java.util.Collection;
import java.util.UUID;

public class GameService {

    private final MemoryGameDAO gameDAO;
    private final MemoryAuthDAO authDAO;
    private int currentGameID;

    public GameService(GameDB gameDB, AuthDB authDB) {
        this.gameDAO = new MemoryGameDAO(gameDB);
        this.authDAO = new MemoryAuthDAO(authDB);
        this.currentGameID = 1;
    }

    public CreateGameResponse createGame(String authToken, CreateGameRequest createGameRequest) throws ResponseException, DataAccessException {
        if (createGameRequest.gameName() == null) {
            throw new ResponseException(400, "bad request");
        }

        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new ResponseException(401, "unauthorized");
        }

        int newGameID = this.currentGameID;
        GameData gameData = new GameData(
                newGameID,
                null,
                null,
                createGameRequest.gameName(),
                new ChessGame()
        );
        gameDAO.insertGame(gameData);
        this.currentGameID++;
        return new CreateGameResponse(newGameID);
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws ResponseException, DataAccessException {
        if (joinGameRequest.gameID() == 0 ||
            joinGameRequest.playerColor() == null
        ) {
            throw new ResponseException(400, "bad request");
        }

        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new ResponseException(401, "unauthorized");
        }

        GameData gameData = gameDAO.getGame(joinGameRequest.gameID());
        if (gameData == null) {
            throw new ResponseException(400, "bad request");
        }

        GameData updatedGameData;
        switch (joinGameRequest.playerColor()) {
            case "WHITE" -> {
                if (gameData.whiteUsername() != null) {
                    throw new ResponseException(403, "already taken");
                }
                updatedGameData = new GameData(
                        gameData.gameID(),
                        authData.username(),
                        gameData.blackUsername(),
                        gameData.gameName(),
                        gameData.game()
                );
            }
            case "BLACK" -> {
                if (gameData.blackUsername() != null) {
                    throw new ResponseException(403, "already taken");
                }
                updatedGameData = new GameData(
                        gameData.gameID(),
                        gameData.whiteUsername(),
                        authData.username(),
                        gameData.gameName(),
                        gameData.game()
                );
            }
            default -> {
                throw new ResponseException(400, "bad request");
            }
        }

        gameDAO.updateGame(updatedGameData);
    }

    public ListGamesResponse listGames(String authToken) throws ResponseException, DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new ResponseException(401, "unauthorized");
        }

        Collection<GameData> gameList = gameDAO.listGames();
        return new ListGamesResponse(gameList);
    }
}
