package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class RookMoveCalculator extends MoveCalculator{
    private static final int[][] MOVEMENT_OFFSETS = {{0,1}, {1,0}, {0,-1}, {-1,0}};
    private static final int RANGE = -1;

    public RookMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
    }

    public ArrayList<ChessMove> calculateMoves() {
        return validMovesAlongAllOffsets(MOVEMENT_OFFSETS, RANGE, CaptureRestriction.CanCapture);
    }
}
