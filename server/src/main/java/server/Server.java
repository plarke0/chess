package server;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import io.javalin.*;
import io.javalin.http.Context;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import websocket.WebSocketHandler;

public class Server {

    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();
    private final ClearService clearService = new ClearService();
    private final WebSocketHandler webSocketHandler = new WebSocketHandler();
    private final Javalin javalin;

    public Server() {
        try {
            DatabaseManager.initializeDatabase();
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception.getMessage());
        }
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.delete("/db", this::clear);
        javalin.post("/user", this::registerUser);
        javalin.post("/session", this::loginUser);
        javalin.delete("/session", this::logoutUser);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);

        javalin.exception(ResponseException.class, this::responseExceptionHandler);
        javalin.exception(DataAccessException.class, this::dataAccessExceptionHandler);

        javalin.ws("/ws", ws -> {
                    ws.onConnect(webSocketHandler);
                    ws.onMessage(webSocketHandler);
                    ws.onClose(webSocketHandler);
                });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void clear(Context context) throws DataAccessException {
        clearService.clear();
    }

    private void registerUser(Context context) throws ResponseException, DataAccessException {
        RegisterRequest registerRequest = Serializer.deserialize(context.body(), RegisterRequest.class);
        RegisterResponse registerResponse = userService.register(registerRequest);
        context.result(Serializer.serialize(registerResponse));
    }

    private void loginUser(Context context) throws ResponseException, DataAccessException {
        LoginRequest loginRequest = Serializer.deserialize(context.body(), LoginRequest.class);
        LoginResponse loginResponse = userService.login(loginRequest);
        context.result(Serializer.serialize(loginResponse));
    }

    private void logoutUser(Context context) throws ResponseException, DataAccessException {
        String authToken = context.header("authorization");
        userService.logout(authToken);
    }

    private void listGames(Context context) throws ResponseException, DataAccessException {
        String authToken = context.header("authorization");
        ListGamesResponse listGamesResponse = gameService.listGames(authToken);
        context.result(Serializer.serialize(listGamesResponse));
    }

    private void createGame(Context context) throws ResponseException, DataAccessException {
        String authToken = context.header("authorization");
        CreateGameRequest createGameRequest = Serializer.deserialize(context.body(), CreateGameRequest.class);
        CreateGameResponse createGameResponse = gameService.createGame(authToken, createGameRequest);
        context.result(Serializer.serialize(createGameResponse));
    }

    private void joinGame(Context context) throws ResponseException, DataAccessException {
        String authToken = context.header("authorization");
        JoinGameRequest joinGameRequest = Serializer.deserialize(context.body(), JoinGameRequest.class);
        gameService.joinGame(authToken, joinGameRequest);
    }

    private void responseExceptionHandler(ResponseException e, Context context) {
        context.status(e.httpCode);
        context.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
    }

    private void dataAccessExceptionHandler(DataAccessException e, Context context) {
        context.status(500);
        context.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
    }
}
