package chess.movement;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class KingMoveCalculator extends MoveCalculator{
    private static final int[][] MOVEMENT_OFFSETS = {{0,1}, {1,1}, {1,0}, {1,-1},
                                                    {0,-1}, {-1,-1}, {-1,0}, {-1,1}};
    private static final int RANGE = 1;

    public KingMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
    }

    @Override
    public ArrayList<ChessMove> calculateMoves() {
        return validMovesAlongAllOffsets(MOVEMENT_OFFSETS, RANGE, CaptureRestriction.CanCapture);
    }
}
