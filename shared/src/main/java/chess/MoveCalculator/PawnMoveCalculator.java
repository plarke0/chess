package chess.MoveCalculator;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveCalculator extends MoveCalculator{
    private final int END_ROW;
    private final int[][] POSSIBLE_FORWARD_OFFSETS;
    private final int FORWARD_RANGE;
    private final int[][] POSSIBLE_CAPTURE_OFFSETS;
    private static final int CAPTURE_RANGE = 1;

    public PawnMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
        int direction, startRow;
        if (this.piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
            startRow = 2;
            this.END_ROW = 8;
        } else {
            direction = -1;
            startRow = 7;
            this.END_ROW = 1;
        }
        if (this.piecePosition.getRow() == startRow) {
            this.FORWARD_RANGE = 2;
        } else {
            this.FORWARD_RANGE = 1;
        }
        POSSIBLE_FORWARD_OFFSETS = new int[][] {{direction, 0}};
        POSSIBLE_CAPTURE_OFFSETS = new int[][] {{direction, 1}, {direction, -1}};
    }

    private ArrayList<ChessMove> createPawnMove(ChessPosition endPosition) {
        if (endPosition.getRow() == this.END_ROW) {
            return new ArrayList<>(List.of(new ChessMove(piecePosition, endPosition, ChessPiece.PieceType.KNIGHT),
                    new ChessMove(piecePosition, endPosition, ChessPiece.PieceType.BISHOP),
                    new ChessMove(piecePosition, endPosition, ChessPiece.PieceType.ROOK),
                    new ChessMove(piecePosition, endPosition, ChessPiece.PieceType.QUEEN)));
        } else {
            return new ArrayList<>(List.of(new ChessMove(piecePosition, endPosition, null)));
        }
    }

    private ArrayList<ChessMove> mergePawnMoves(ArrayList<ChessMove> forwardMoves,
                                                ArrayList<ChessMove> captureMoves) {
        var mergedMoves = new ArrayList<ChessMove>();
        for (ChessMove move : forwardMoves) {
            ChessPosition endPosition = move.getEndPosition();
            mergedMoves.addAll(createPawnMove(endPosition));
        }
        for (ChessMove move : captureMoves) {
            ChessPosition endPosition = move.getEndPosition();
            mergedMoves.addAll(createPawnMove(endPosition));
        }
        return mergedMoves;
    }

    public ArrayList<ChessMove> calculateMoves() {
        var validForwardMoves = validMovesAlongAllOffsets(
                this.POSSIBLE_FORWARD_OFFSETS, FORWARD_RANGE, CaptureRestriction.NoCapture);
        var validCaptureMoves = validMovesAlongAllOffsets(
                this.POSSIBLE_CAPTURE_OFFSETS, CAPTURE_RANGE, CaptureRestriction.NeedsCapture);
        return mergePawnMoves(validForwardMoves, validCaptureMoves);
    }
}
