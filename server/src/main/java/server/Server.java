package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.memory.database.AuthDB;
import dataaccess.memory.database.GameDB;
import dataaccess.memory.database.UserDB;
import io.javalin.*;
import io.javalin.http.Context;
import service.ClearService;
import service.GameService;
import service.UserService;
import service.requests.*;
import service.responses.*;

public class Server {

    private final AuthDB authDB = new AuthDB();
    private final UserDB userDB = new UserDB();
    private final GameDB gameDB = new GameDB();

    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;
    private final Javalin javalin;

    public Server() {
        this.userService = new UserService(userDB, authDB);
        this.gameService = new GameService(gameDB, authDB);
        this.clearService = new ClearService(authDB, userDB, gameDB);
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
        RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
        RegisterResponse registerResponse = userService.register(registerRequest);
        context.result(new Gson().toJson(registerResponse));
    }

    private void loginUser(Context context) throws ResponseException, DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(context.body(), LoginRequest.class);
        LoginResponse loginResponse = userService.login(loginRequest);
        context.result(new Gson().toJson(loginResponse));
    }

    private void logoutUser(Context context) throws ResponseException, DataAccessException {
        String authToken = context.header("authorization");
        userService.logout(authToken);
    }

    private void listGames(Context context) throws ResponseException, DataAccessException {
        String authToken = context.header("authorization");
        ListGamesResponse listGamesResponse = gameService.listGames(authToken);
        context.result(new Gson().toJson(listGamesResponse));
    }

    private void createGame(Context context) throws ResponseException, DataAccessException {
        String authToken = context.header("authorization");
        CreateGameRequest createGameRequest = new Gson().fromJson(context.body(), CreateGameRequest.class);
        CreateGameResponse createGameResponse = gameService.createGame(authToken, createGameRequest);
        context.result(new Gson().toJson(createGameResponse));
    }

    private void joinGame(Context context) throws ResponseException, DataAccessException {
        String authToken = context.header("authorization");
        JoinGameRequest joinGameRequest = new Gson().fromJson(context.body(), JoinGameRequest.class);
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
