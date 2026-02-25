package service;

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
import service.requests.RegisterRequest;
import service.responses.RegisterResponse;
import service.responses.ResponseException;

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
}
