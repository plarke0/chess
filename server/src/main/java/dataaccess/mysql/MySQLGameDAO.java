package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class MySQLGameDAO implements GameDAO {
    public void insertGame(GameData gameData) throws DataAccessException {

    }

    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    public void updateGame(GameData gameData) throws DataAccessException {

    }

    public void clear() throws DataAccessException {

    }
}
