package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {

    private ChessGame content;

    public LoadGameMessage(ServerMessageType type, ChessGame content) {
        super(type);
        this.content = content;
    }
}
