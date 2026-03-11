package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MySQLGameDAO implements GameDAO {
    public int insertGame(GameData gameData) throws DataAccessException {

        return 0;
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
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP TABLE IF EXISTS games")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
