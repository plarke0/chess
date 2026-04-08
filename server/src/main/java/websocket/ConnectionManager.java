package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, SessionData> connections = new ConcurrentHashMap<>();

    public void add(Session session, int gameID) {
        connections.put(session, new SessionData(session, gameID));
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcastToGame(int connectionGameID, ServerMessage notification) throws IOException {
        broadcastToGameWithCondition(
                notification,
                (session, gameID) -> gameID == connectionGameID
        );
    }

    public void broadcastToGameExclusive(Session excludedSession, int connectionGameID, ServerMessage notification) throws IOException {
        broadcastToGameWithCondition(
                notification,
                (session, gameID) -> !session.equals(excludedSession) && gameID == connectionGameID
        );
    }

    public void broadcastToGameIndividual(Session chosenSession, int chosenGameID, ServerMessage notification) throws IOException {
        broadcastToGameWithCondition(
                notification,
                (session, gameID) -> session.equals(chosenSession) && gameID == chosenGameID
        );
    }

    @FunctionalInterface
    private interface Condition {
        Boolean check(Session session, int gameID);
    }

    private void broadcastToGameWithCondition(ServerMessage notification, Condition condition) throws IOException {
        String msg = notification.toString();
        for (SessionData connectionData : connections.values()) {
            Session session = connectionData.session();
            int gameID = connectionData.gameID();
            if (session.isOpen()) {
                if (condition.check(session, gameID)) {
                    session.getRemote().sendString(msg);
                }
            }
        }
    }
}
