package chess.move_calculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class KnightMoveCalculator extends MoveCalculator{
    public KnightMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
        this.movementOffsets = new int[][] {{2,1}, {1,2}, {-1,2}, {-2,1},
                                            {-2,-1}, {-1,-2}, {1,-2}, {2,-1}};
        this.range = 1;
    }

    public Collection<ChessMove> calculateMoves() {
        return validMovesAlongOffsets();
    }
}
