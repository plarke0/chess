package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.memory.database.GameDB;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    public GameDB gameDB;

    public MemoryGameDAO(GameDB gameDB) {
        this.gameDB = gameDB;
    }

    public void insertGame(GameData gameData) throws DataAccessException {
        this.gameDB.gameDBArray.add(gameData);
    }

    public GameData getGame(String gameID) throws DataAccessException {
        throw new UnsupportedOperationException("Feature not implemented.");
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return this.gameDB.gameDBArray;
    }

    public void updateGame(GameData gameData) throws DataAccessException {
        throw new UnsupportedOperationException("Feature not implemented.");
    }

    public void clear() throws DataAccessException {
        this.gameDB.gameDBArray = new ArrayList<>();
    }
}
