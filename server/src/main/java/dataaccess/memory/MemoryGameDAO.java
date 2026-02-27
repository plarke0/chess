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

    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : this.gameDB.gameDBArray) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return this.gameDB.gameDBArray;
    }

    public void updateGame(GameData gameData) throws DataAccessException {
        int gameID = gameData.gameID();
        this.gameDB.gameDBArray.removeIf(game -> game.gameID() == gameID);
        this.gameDB.gameDBArray.add(gameData);
    }

    public void clear() throws DataAccessException {
        this.gameDB.gameDBArray = new ArrayList<>();
    }
}
