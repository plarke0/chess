package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.sql.SQLException;
import java.util.UUID;

public class DBTests {

    private static UserService userService;
    private static GameService gameService;
    private static ClearService clearService;

    private static UserData existingUser;
    private static int existingUserID = 9999;
    private static String existingUserEncryptedPassword;
    private static UserData newUser;
    private static int newUserID = 10000;
    private static String newUserEncryptedPassword;
    private AuthData existingAuth;
    private GameData existingGameData;

    @BeforeAll
    public static void init() {
        existingUser = new UserData("user1", "12345", "user1@byu.edu");
        newUser = new UserData("user2", "password", "user2@gmail.com");
        existingUserEncryptedPassword = BCrypt.hashpw(existingUser.password(), BCrypt.gensalt());
        newUserEncryptedPassword = BCrypt.hashpw(newUser.password(), BCrypt.gensalt());
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

        userService = new UserService();
        gameService = new GameService();
        clearService = new ClearService();

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

    }

    @Test
    public void clearTestNegative() {

    }

    @Test
    public void insertAuthTestPositive() {

    }

    @Test
    public void insertAuthTestNegative() {

    }

    @Test
    public void getAuthTestPositive() {

    }

    @Test
    public void getAuthTestNegative() {

    }

    @Test
    public void deleteAuthTestPositive() {

    }

    @Test
    public void deleteAuthTestNegative() {

    }

    @Test
    public void insertUserTestPositive() {

    }

    @Test
    public void insertUserTestNegative() {

    }

    @Test
    public void getUserTestPositive() {

    }

    @Test
    public void getUserTestNegative() {

    }

    @Test
    public void insertGameTestPositive() {

    }

    @Test
    public void insertGameTestNegative() {

    }

    @Test
    public void getGameTestPositive() {

    }

    @Test
    public void getGameTestNegative() {

    }

    @Test
    public void listGamesTestPositive() {

    }

    @Test
    public void listGamesTestNegative() {

    }

    @Test
    public void updateGameTestPositive() {

    }

    @Test
    public void updateGameTestNegative() {

    }
}
