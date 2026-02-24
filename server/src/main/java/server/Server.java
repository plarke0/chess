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
import service.requests.RegisterRequest;
import service.responses.RegisterResponse;
import service.responses.ResponseException;

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

    private void responseExceptionHandler(ResponseException e, Context context) {
        context.status(e.httpCode);
        context.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
    }

    private void dataAccessExceptionHandler(DataAccessException e, Context context) {
        context.status(500);
        context.result("{ \"message\": \"Error: " + e.getMessage() + "\" }");
    }
}
