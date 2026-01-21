package chess.MoveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class BishopMoveCalculator extends MoveCalculator{
    private static final int[][] MOVEMENT_OFFSETS = {{1,1}, {1,-1}, {-1,-1}, {-1,1}};
    private static final int RANGE = -1;

    public BishopMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
    }

    public ArrayList<ChessMove> calculateMoves() {
        return validMovesAlongAllOffsets(MOVEMENT_OFFSETS, RANGE, CaptureRestriction.CanCapture);
    }
}
