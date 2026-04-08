package websocket.messages;

public class NotificationMessage extends ServerMessage {

    private String content;

    public NotificationMessage(ServerMessageType type, String content) {
        super(type);
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
