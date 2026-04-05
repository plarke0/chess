package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage<ChessGame>{
    public LoadGameMessage(ServerMessageType type, ChessGame content) {
        super(type, content);
    }
}
