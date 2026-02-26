package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.memory.database.AuthDB;
import dataaccess.memory.database.GameDB;
import dataaccess.memory.database.UserDB;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.*;
import service.responses.*;

import java.util.ArrayList;
import java.util.UUID;

public class ServiceTests {

    private AuthDB authDB;
    private UserDB userDB;
    private GameDB gameDB;
    private static UserService userService;
    private static GameService gameService;
    private static ClearService clearService;

    private static UserData existingUser;
    private static UserData newUser;
    private AuthData existingAuth;

    @BeforeAll
    public static void init() {
        existingUser = new UserData("user1", "12345", "user1@byu.edu");
        newUser = new UserData("user2", "password", "user2@gmail.com");
    }

    @BeforeEach
    public void setup() {
        authDB = new AuthDB();
        userDB = new UserDB();
        gameDB = new GameDB();

        userService = new UserService(userDB, authDB);
        gameService = new GameService(gameDB, authDB);
        clearService = new ClearService(authDB, userDB, gameDB);

        userDB.userDBArray.add(existingUser);

        String authToken = UUID.randomUUID().toString();
        existingAuth = new AuthData(authToken, existingUser.username());
        authDB.authDBArray.add(existingAuth);
    }

    @Test
    public void clear() throws DataAccessException {
        clearService.clear();
        Assertions.assertEquals(new ArrayList<AuthData>(), authDB.authDBArray);
        Assertions.assertEquals(new ArrayList<UserData>(), userDB.userDBArray);
        Assertions.assertEquals(new ArrayList<GameData>(), gameDB.gameDBArray);
    }

    @Test
    public void registerNewUser() throws DataAccessException, ResponseException {
        RegisterRequest registerRequest = new RegisterRequest(
                newUser.username(),
                newUser.password(),
                newUser.email()
        );
        RegisterResponse registerResponse = userService.register(registerRequest);
        Assertions.assertTrue(userDB.userDBArray.contains(newUser));

        AuthData newAuthData = new AuthData(registerResponse.authToken(), newUser.username());
        Assertions.assertTrue(authDB.authDBArray.contains(newAuthData));
    }

    @Test
    public void registerExistingUser() {
        RegisterRequest registerRequest = new RegisterRequest(
                existingUser.username(),
                existingUser.password(),
                existingUser.email()
        );

        Assertions.assertThrows(ResponseException.class, () -> userService.register(registerRequest));
    }

    @Test
    public void registerBadRequest() {
        RegisterRequest registerRequest = new RegisterRequest(
                existingUser.username(),
                existingUser.password(),
                null
        );

        Assertions.assertThrows(ResponseException.class, () -> userService.register(registerRequest));
    }

    @Test
    public void loginExistingUser() throws DataAccessException, ResponseException {
        LoginRequest loginRequest = new LoginRequest(
                existingUser.username(),
                existingUser.password()
        );

        LoginResponse loginResponse = userService.login(loginRequest);
        AuthData authData = new AuthData(loginResponse.authToken(), loginResponse.username());
        Assertions.assertTrue(authDB.authDBArray.contains(authData));
    }

    @Test
    public void loginUserDoesNotExist() {
        LoginRequest loginRequest = new LoginRequest(
                newUser.username(),
                newUser.password()
        );

        Assertions.assertThrows(ResponseException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void loginIncorrectPassword() {
        LoginRequest loginRequest = new LoginRequest(
                existingUser.username(),
                "NOTTHEPASSWORD"
        );

        Assertions.assertThrows(ResponseException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void loginBadRequest() {
        LoginRequest loginRequest = new LoginRequest(
                newUser.username(),
                null
        );

        Assertions.assertThrows(ResponseException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void logoutSignedInUser() throws ResponseException, DataAccessException {
        userService.logout(existingAuth.authToken());

        AuthData existingAuthData = new AuthData(existingAuth.authToken(), existingAuth.username());
        Assertions.assertFalse(authDB.authDBArray.contains(existingAuthData));
    }

    @Test
    public void logoutInvalidAuth() {
        Assertions.assertThrows(ResponseException.class, () -> userService.logout("NOTANAUTHTOKEN"));
    }

    @Test
    public void listGamesEmpty() throws ResponseException, DataAccessException {
        ListGamesResponse listGamesResponse = gameService.listGames(existingAuth.authToken());
        Assertions.assertEquals(listGamesResponse.games(), gameDB.gameDBArray);
    }

    @Test
    public void listGamesWithGames() throws ResponseException, DataAccessException {
        GameData gameData = new GameData(
                12345,
                "White",
                "Black",
                "Test Game",
                new ChessGame()
        );
        gameDB.gameDBArray.add(gameData);

        ListGamesResponse listGamesResponse = gameService.listGames(existingAuth.authToken());
        Assertions.assertEquals(listGamesResponse.games(), gameDB.gameDBArray);
        Assertions.assertTrue(listGamesResponse.games().contains(gameData));
    }

    @Test
    public void listGamesInvalidAuth() {
        Assertions.assertThrows(ResponseException.class, () -> gameService.listGames("NOTANAUTHTOKEN"));
    }

    @Test
    public void createGame() throws ResponseException, DataAccessException {
        String gameName = "Test Game";
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);

        CreateGameResponse createGameResponse = gameService.createGame(existingAuth.authToken(), createGameRequest);

        GameData gameData = new GameData(
                createGameResponse.gameID(),
                null,
                null,
                gameName,
                new ChessGame()
        );
        Assertions.assertTrue(gameDB.gameDBArray.contains(gameData));
    }

    @Test
    public void createGameInvalidAuth() {
        String gameName = "Test Game";
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName);

        Assertions.assertThrows(
                ResponseException.class,
                () -> gameService.createGame("NOTANAUTHTOKEN", createGameRequest)
        );
    }

    @Test
    public void createGameBadRequest() {
        CreateGameRequest createGameRequest = new CreateGameRequest(null);

        Assertions.assertThrows(
                ResponseException.class,
                () -> gameService.createGame(existingAuth.authToken(), createGameRequest)
        );
    }
}
