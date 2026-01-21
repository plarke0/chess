package chess.move_calculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class RookMoveCalculator extends MoveCalculator{
    private static final int[][] movementOffsets = {{0,1}, {1,0}, {0,-1}, {-1,0}};
    private static final int range = -1;

    public RookMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
    }

    public ArrayList<ChessMove> calculateMoves() {
        return validMovesAlongAllOffsets(movementOffsets, range, CaptureRestriction.CanCapture);
    }
}
