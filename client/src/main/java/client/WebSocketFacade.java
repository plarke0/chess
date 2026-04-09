package client;

import com.google.gson.Gson;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import ui.repl.REPL;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebSocketFacade extends Endpoint{
    public Session session;
    private final REPL repl;

    public WebSocketFacade(REPL repl) throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        this.repl = repl;
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String messageJSON) {
                ServerMessage message = new Gson().fromJson(messageJSON, ServerMessage.class);
                ServerMessage.ServerMessageType messageType = message.getServerMessageType();
                switch (messageType) {
                    case NOTIFICATION -> handleNotification(messageJSON);
                    case ERROR -> handleError(messageJSON);
                    case LOAD_GAME -> handleLoadGame(messageJSON);
                }
            }
        });
    }

    private void handleNotification(String messageJSON) {
        NotificationMessage notificationMessage = new Gson().fromJson(messageJSON, NotificationMessage.class);
        repl.evaluateNotificationMessage(notificationMessage);
    }

    private void handleError(String messageJSON) {
        ErrorMessage errorMessage = new Gson().fromJson(messageJSON, ErrorMessage.class);
        repl.evaluateErrorMessage(errorMessage);
    }

    private void handleLoadGame(String messageJSON) {
        LoadGameMessage loadGameMessage = new Gson().fromJson(messageJSON, LoadGameMessage.class);
        repl.evaluateLoadGameMessage(loadGameMessage);
    }

    public void sendCommand(UserGameCommand command) throws IOException {
        session.getBasicRemote().sendText(command.toJSON());
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
