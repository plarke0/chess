package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
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
        Assertions.assertTrue(true);
    }

    @Test
    public void registerUserNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void loginUserPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void loginUserNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutUserPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutUserNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void listGamesPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void listGamesNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createGamePositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGamePositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameNegative() {
        Assertions.assertTrue(true);
    }
}
