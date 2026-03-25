package client;

import model.UserData;
import org.junit.jupiter.api.*;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.RegisterResponse;
import responses.ResponseException;
import server.Server;


public class ServerFacadeTests {

    private static UserData existingUser;
    private static UserData newUser;
    private static String existingAuthToken;

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);

        existingUser = new UserData("user1", "12345", "user1@byu.edu");
        newUser = new UserData("user2", "password", "user2@gmail.com");
    }

    @BeforeEach
    public void reset() throws ResponseException {
        serverFacade.clear();
        RegisterResponse registerResponse = serverFacade.registerUser(
                        new RegisterRequest(existingUser.username(),
                        existingUser.password(),
                        existingUser.email())
        );
        existingAuthToken = registerResponse.authToken();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void clearPositive() {
        Assertions.assertDoesNotThrow(() -> serverFacade.clear());
    }

    @Test
    public void registerUserPositive() {
        Assertions.assertDoesNotThrow(() ->
                serverFacade.registerUser(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()))
        );
    }

    @Test
    public void registerUserNegative() {
        Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.registerUser(new RegisterRequest(existingUser.username(), existingUser.password(), existingUser.email()))
        );
    }

    @Test
    public void loginUserPositive() {
        Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.loginUser(new LoginRequest(newUser.username(), newUser.password()))
        );
    }

    @Test
    public void loginUserNegative() {
        Assertions.assertDoesNotThrow(() ->
                serverFacade.loginUser(new LoginRequest(existingUser.username(), existingUser.password()))
        );
    }

    @Test
    public void logoutUserPositive() {
        Assertions.assertDoesNotThrow(() ->
                serverFacade.logoutUser(existingAuthToken)
        );
    }

    @Test
    public void listGamesPositive() {
        Assertions.assertDoesNotThrow(() ->
            serverFacade.listGames(existingAuthToken)
        );
    }

    @Test
    public void listGamesNegative() {
        Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.listGames("FAKEAUTH")
        );
    }

    @Test
    public void createGamePositive() {
        Assertions.assertDoesNotThrow(() ->
                serverFacade.createGame(new CreateGameRequest("Test Game"), existingAuthToken)
        );
    }

    @Test
    public void createGameNegative() {
        Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.createGame(new CreateGameRequest("Test Game"), "FAKEAUTH")
        );
    }

    @Test
    public void joinGamePositive() throws ResponseException {
        serverFacade.createGame(new CreateGameRequest("Test Game"), existingAuthToken);
        Assertions.assertDoesNotThrow(() ->
                serverFacade.joinGame(new JoinGameRequest(1, "WHITE"), existingAuthToken)
        );
    }

    @Test
    public void joinGameNegativeDoesNotExist() {
        Assertions.assertThrows(ResponseException.class, () ->
            serverFacade.joinGame(new JoinGameRequest(1, "WHITE"), existingAuthToken)
        );
    }

    @Test
    public void joinGameNegativeInvalidAuth() throws ResponseException {
        serverFacade.createGame(new CreateGameRequest("Test Game"), existingAuthToken);
        Assertions.assertThrows(ResponseException.class, () ->
            serverFacade.joinGame(new JoinGameRequest(1, "WHITE"), "FAKEAUTH")
        );
    }
}
