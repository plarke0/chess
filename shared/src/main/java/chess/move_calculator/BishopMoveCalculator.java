package chess.move_calculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class BishopMoveCalculator extends MoveCalculator{
    public BishopMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
        this.movementOffsets = new int[][] {{1,1}, {-1,1}, {-1,-1}, {1,-1}};
        this.range = -1;
    }

    public Collection<ChessMove> calculateMoves() {
        return validMovesAlongOffsets();
    }
}
