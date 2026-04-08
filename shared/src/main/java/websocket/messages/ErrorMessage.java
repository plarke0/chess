package websocket.messages;

public class ErrorMessage extends ServerMessage {

    private String content;

    public ErrorMessage(ServerMessageType type, String content) {
        super(type);
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
