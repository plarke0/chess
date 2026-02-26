package service;

import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.database.AuthDB;
import dataaccess.memory.database.GameDB;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.requests.ListGamesRequest;
import service.responses.CreateGameResponse;
import service.responses.ListGamesResponse;

public class GameService {

    private final MemoryGameDAO gameDAO;
    private final MemoryAuthDAO authDAO;

    public GameService(GameDB gameDB, AuthDB authDB) {
        this.gameDAO = new MemoryGameDAO(gameDB);
        this.authDAO = new MemoryAuthDAO(authDB);
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) {
        throw new UnsupportedOperationException("Feature not implemented.");
    }

    public void joinGame(JoinGameRequest joinGameRequest) {
        throw new UnsupportedOperationException("Feature not implemented.");
    }

    public ListGamesResponse listGames(ListGamesRequest listGamesRequest) {
        throw new UnsupportedOperationException("Feature not implemented.");
    }
}
