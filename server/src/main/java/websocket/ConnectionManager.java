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

    public void broadcastToGame(int gameID, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        for (SessionData connectionData : connections.values()) {
            Session session = connectionData.session();
            int connectionGameID = connectionData.gameID();
            if (session.isOpen()) {
                if (connectionGameID == gameID) {
                    session.getRemote().sendString(msg);
                }
            }
        }
    }

    public void broadcastToGameExclusive(Session excludedSession, int gameID, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        for (SessionData connectionData : connections.values()) {
            Session session = connectionData.session();
            int connectionGameID = connectionData.gameID();
            if (session.isOpen()) {
                if (!session.equals(excludedSession) && connectionGameID == gameID) {
                    session.getRemote().sendString(msg);
                }
            }
        }
    }
}
