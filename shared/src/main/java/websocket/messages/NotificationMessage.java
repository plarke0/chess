package websocket.messages;

public class NotificationMessage extends ServerMessage<String>{
    public NotificationMessage(ServerMessageType type, String content) {
        super(type, content);
    }
}
