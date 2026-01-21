package chess.move_calculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class KnightMoveCalculator extends MoveCalculator{
    private static final int[][] movementOffsets = {{1,2}, {2,1}, {2,-1}, {1,-2},
                                                    {-1,-2}, {-2,-1}, {-2,1}, {-1,2}};
    private static final int range = 1;

    public KnightMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
    }

    public ArrayList<ChessMove> calculateMoves() {
        return validMovesAlongAllOffsets(movementOffsets, range, CaptureRestriction.CanCapture);
    }
}
