package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import service.ClearService;
import service.GameService;
import service.UserService;
import service.requests.RegisterRequest;
import service.responses.RegisterResponse;

public class Server {

    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;
    private final Javalin javalin;

    public Server() {
        this.userService = new UserService();
        this.gameService = new GameService();
        this.clearService = new ClearService();
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", this::registerUser);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void registerUser(Context context) throws Exception {
        RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
        RegisterResponse registerResponse = userService.register(registerRequest);
        context.result(new Gson().toJson(registerResponse));
    }
}
