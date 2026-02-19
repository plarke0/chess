package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void insertGame(GameData gameData) throws DataAccessException;
    GameData getGame(String gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void updateGame(String gameID, GameData gameData) throws DataAccessException;
    void clear() throws DataAccessException;
}
