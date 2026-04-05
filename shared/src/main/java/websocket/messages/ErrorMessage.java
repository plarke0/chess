package websocket.messages;

public class ErrorMessage extends ServerMessage<String>{
    public ErrorMessage(ServerMessageType type, String content) {
        super(type, content);
    }
}
