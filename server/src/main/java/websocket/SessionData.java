package websocket;

import org.eclipse.jetty.websocket.api.Session;

public record SessionData(Session session, int gameID) {
}
