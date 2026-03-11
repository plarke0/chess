package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {
    public void insertUser(UserData userData) throws DataAccessException {

    }

    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP TABLE IF EXISTS passwords")) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement("DROP TABLE IF EXISTS users")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
