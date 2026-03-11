package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;

public class MySQLUserDAO implements UserDAO {
    public void insertUser(UserData userData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            int userID;
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO users (username, email) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, userData.email());
                preparedStatement.executeUpdate();

                var resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    userID = resultSet.getInt(1);
                } else {
                    throw new DataAccessException("New user id unable to be generated");
                }
            }
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO passwords (user, password) VALUES (?, ?)")) {
                preparedStatement.setInt(1, userID);
                preparedStatement.setString(2, userData.password());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            int userID;
            String email;
            try (var preparedStatement = conn.prepareStatement(
                    "SELECT id, email FROM users WHERE username=?")) {
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    userID = rs.getInt(1);
                    email = rs.getString(2);
                } else {
                    return null;
                }
            }
            try (var preparedStatement = conn.prepareStatement(
                    "SELECT password FROM passwords WHERE user=?")) {
                preparedStatement.setInt(1, userID);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String password = rs.getString(1);
                    return new UserData(username, password, email);
                } else {
                    throw new DataAccessException("Failed to find password associated with user");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
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
