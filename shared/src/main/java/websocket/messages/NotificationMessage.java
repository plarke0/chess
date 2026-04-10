package websocket.messages;

public class NotificationMessage extends ServerMessage {

    private String message;

    public NotificationMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    @Override
    public String getContent() {
        return message;
    }
}
