package websocket;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) {
        System.out.println("Websocket connected");
        wsConnectContext.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) {
        try {
            UserGameCommand command = new Gson().fromJson(wsMessageContext.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> {
                    // Send LoadGameMessage to root client
                    // Send a NotificationMessage to all other clients saying the user has joined (observer or player + color)
                }
                case MAKE_MOVE -> {
                    // Verify validity
                    // Update game
                    // Send LoadGameMessage to all clients in the game with updated game
                    // Send NotificationMessage to all other clients with the move just made
                    // If the move results in check, checkmate, or stalemate, sed a NotificationMessage to all clients
                }
                case LEAVE -> {
                    // Update game to remove root client
                    // Send NotificationMessage to all other clients
                }
                case RESIGN -> {
                    // Mark the game as over and update it
                    // Sends a NotificationMessage to all clients
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) {
        System.out.println("Websocket closed");
    }
}
