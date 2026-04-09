package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage {

    private GameData content;

    public LoadGameMessage(ServerMessageType type, GameData content) {
        super(type);
        this.content = content;
    }

    @Override
    public GameData getContent() {
        return content;
    }
}
