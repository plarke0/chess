package chess.move_calculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class RookMoveCalculator extends MoveCalculator{
    public RookMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
        this.movementOffsets = new int[][] {{1,0}, {0,1}, {-1,0}, {0,-1}};
        this.range = -1;
    }

    public Collection<ChessMove> calculateMoves() {
        return validMovesAlongOffsets();
    }
}
