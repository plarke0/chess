package chess.move_calculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class BishopMoveCalculator extends MoveCalculator{
    private static final int[][] movementOffsets = {{1,1}, {1,-1}, {-1,-1}, {-1,1}};
    private static final int range = -1;

    public BishopMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
    }

    public ArrayList<ChessMove> calculateMoves() {
        return validMovesAlongAllOffsets(movementOffsets, range, CaptureRestriction.CanCapture);
    }
}
