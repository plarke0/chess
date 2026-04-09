package client;

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
        this.session.addMessageHandler(new MessageHandler.Whole<ServerMessage>() {
            public void onMessage(ServerMessage message) {
                ServerMessage.ServerMessageType messageType = message.getServerMessageType();
                switch (messageType) {
                    case NOTIFICATION -> handleNotification(message);
                    case ERROR -> handleError(message);
                    case LOAD_GAME -> handleLoadGame(message);
                }
            }
        });
    }

    private void handleNotification(ServerMessage message) {
        NotificationMessage notificationMessage = (NotificationMessage) message;
        repl.evaluateNotificationMessage(notificationMessage);
    }

    private void handleError(ServerMessage message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
        repl.evaluateErrorMessage(errorMessage);
    }

    private void handleLoadGame(ServerMessage message) {
        LoadGameMessage loadGameMessage = (LoadGameMessage) message;
        repl.evaluateLoadGameMessage(loadGameMessage);
    }

    public void sendCommand(UserGameCommand command) throws IOException {
        session.getBasicRemote().sendText(command.toString());
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
