package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.mysql.MySQLAuthDAO;
import dataaccess.mysql.MySQLGameDAO;
import dataaccess.mysql.MySQLUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.UUID;

public class DBTests {

    private static MySQLAuthDAO mySQLAuthDAO = new MySQLAuthDAO();
    private static MySQLGameDAO mySQLGameDAO = new MySQLGameDAO();
    private static MySQLUserDAO mySQLUserDAO = new MySQLUserDAO();

    private static UserData existingUser;
    private static int existingUserID = 9999;
    private static String existingUserEncryptedPassword;
    private static UserData newUser;
    private AuthData existingAuth;
    private GameData existingGameData;

    @BeforeAll
    public static void init() {
        existingUser = new UserData("user1", "12345", "user1@byu.edu");
        newUser = new UserData("user2", "password", "user2@gmail.com");
        existingUserEncryptedPassword = BCrypt.hashpw(existingUser.password(), BCrypt.gensalt());
    }

    @BeforeEach
    public void setup() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP DATABASE chess")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            DatabaseManager.initializeDatabase();
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception.getMessage());
        }

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO users (id, username, email) VALUES (?, ?, ?)")) {
                preparedStatement.setInt(1, existingUserID);
                preparedStatement.setString(2, existingUser.username());
                preparedStatement.setString(3, existingUser.email());
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement("INSERT INTO passwords (user, password) VALUES (?, ?)")) {
                preparedStatement.setInt(1, existingUserID);
                preparedStatement.setString(2, existingUserEncryptedPassword);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }

        String authToken = UUID.randomUUID().toString();
        this.existingAuth = new AuthData(authToken, existingUser.username());
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auths (user, auth_token) VALUES (?, ?)")) {
                preparedStatement.setInt(1, existingUserID);
                preparedStatement.setString(2, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }

        existingGameData = new GameData(
                99999,
                null,
                null,
                "Test Game",
                new ChessGame()
        );
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO games (id, game_name, white_user, black_user, board) VALUES (?, ?, ?, ?, ?)")) {
                preparedStatement.setInt(1, existingGameData.gameID());
                preparedStatement.setString(2, existingGameData.gameName());
                preparedStatement.setString(3, existingGameData.whiteUsername());
                preparedStatement.setString(4, existingGameData.blackUsername());
                preparedStatement.setString(5, new Gson().toJson(existingGameData.game()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void clearTestPositive() {
        Assertions.assertDoesNotThrow(() -> mySQLAuthDAO.clear());
        Assertions.assertDoesNotThrow(() -> mySQLGameDAO.clear());
        Assertions.assertDoesNotThrow(() -> mySQLUserDAO.clear());
    }

    @Test
    public void clearTestNegative() {
        Assertions.assertThrows(DataAccessException.class,() -> mySQLUserDAO.clear());
    }

    @Test
    public void insertAuthTestPositive() {
        String authToken = UUID.randomUUID().toString();
        Assertions.assertDoesNotThrow(() -> mySQLAuthDAO.insertAuth(new AuthData(authToken, existingUser.username())));
    }

    @Test
    public void insertAuthTestNegative() {
        Assertions.assertThrows(DataAccessException.class,() -> mySQLAuthDAO.insertAuth(new AuthData("1234", "DNE")));
    }

    @Test
    public void getAuthTestPositive() throws DataAccessException {
        AuthData authData = mySQLAuthDAO.getAuth(existingAuth.authToken());
        Assertions.assertEquals(existingAuth, authData);
    }

    @Test
    public void getAuthTestNegative() throws DataAccessException {
        AuthData authData = mySQLAuthDAO.getAuth("1234");
        Assertions.assertNull(authData);
    }

    @Test
    public void deleteAuthTestPositive() {
        Assertions.assertDoesNotThrow(() -> mySQLAuthDAO.deleteAuth(existingAuth));
    }

    @Test
    public void deleteAuthTestNegative() {
        Assertions.assertDoesNotThrow(() -> mySQLAuthDAO.deleteAuth(new AuthData(null, "DNE")));
    }

    @Test
    public void insertUserTestPositive() {
        Assertions.assertDoesNotThrow(() -> mySQLUserDAO.insertUser(newUser));
    }

    @Test
    public void insertUserTestNegative() {
        Assertions.assertDoesNotThrow(() -> mySQLUserDAO.insertUser(existingUser));
    }

    @Test
    public void getUserTestPositive() throws DataAccessException {
        UserData userData = mySQLUserDAO.getUser(existingUser.username());
        Assertions.assertEquals(existingUser.username(), userData.username());
        Assertions.assertTrue(BCrypt.checkpw(existingUser.password(), userData.password()));
        Assertions.assertEquals(existingUser.email(), userData.email());
    }

    @Test
    public void getUserTestNegative() throws DataAccessException {
        UserData userData = mySQLUserDAO.getUser(newUser.username());
        Assertions.assertNull(userData);
    }

    @Test
    public void insertGameTestPositive() throws DataAccessException {
        GameData gameData = new GameData(
                1,
                null,
                null,
                "New Game",
                new ChessGame()
        );
        int gameID = mySQLGameDAO.insertGame(gameData);
        Assertions.assertEquals(existingGameData.gameID()+1, gameID);
    }

    @Test
    public void insertGameTestNegative() throws DataAccessException {
        int gameID = mySQLGameDAO.insertGame(existingGameData);
        Assertions.assertEquals(existingGameData.gameID()+1, gameID);
    }

    @Test
    public void getGameTestPositive() throws DataAccessException {
        GameData gameData = mySQLGameDAO.getGame(existingGameData.gameID());
        Assertions.assertEquals(existingGameData, gameData);
    }

    @Test
    public void getGameTestNegative() {
        Assertions.assertThrows(DataAccessException.class, () -> mySQLGameDAO.getGame(1));
    }

    @Test
    public void listGamesTestPositive() {
        Assertions.assertDoesNotThrow(() -> mySQLGameDAO.listGames());
    }

    @Test
    public void updateGameTestPositive() {
        GameData gameData = new GameData(
                existingGameData.gameID(),
                null,
                null,
                "New Game",
                new ChessGame()
        );
        Assertions.assertDoesNotThrow(() -> mySQLGameDAO.updateGame(gameData));
    }

    @Test
    public void updateGameTestNegative() {
        GameData gameData = new GameData(
                1,
                null,
                null,
                "New Game",
                new ChessGame()
        );
        Assertions.assertDoesNotThrow(() -> mySQLGameDAO.updateGame(gameData));
    }
}
