package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.mysql.MySQLAuthDAO;
import dataaccess.mysql.MySQLGameDAO;
import model.AuthData;
import model.GameData;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.responses.CreateGameResponse;
import service.responses.ListGamesResponse;
import service.responses.ResponseException;

import java.util.Collection;

public class GameService {

    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameService() {
        this.gameDAO = new MySQLGameDAO();
        this.authDAO = new MySQLAuthDAO();
    }

    public CreateGameResponse createGame(String authToken, CreateGameRequest createGameRequest) throws ResponseException, DataAccessException {
        if (createGameRequest.gameName() == null) {
            throw new ResponseException(400, "bad request");
        }

        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new ResponseException(401, "unauthorized");
        }

        GameData gameData = new GameData(
                -1,
                null,
                null,
                createGameRequest.gameName(),
                new ChessGame()
        );
        int newGameID = gameDAO.insertGame(gameData);
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
