package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveCalculator extends MoveCalculator{
    private final int endRow;
    private final int[][] possibleForwardOffsets;
    private final int forwardRange;
    private final int[][] possibleCaptureOffsets;
    private static final int CAPTURE_RANGE = 1;

    public PawnMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
        int direction, startRow;
        if (this.piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
            startRow = 2;
            this.endRow = 8;
        } else {
            direction = -1;
            startRow = 7;
            this.endRow = 1;
        }
        if (this.piecePosition.getRow() == startRow) {
            this.forwardRange = 2;
        } else {
            this.forwardRange = 1;
        }
        possibleForwardOffsets = new int[][] {{direction, 0}};
        possibleCaptureOffsets = new int[][] {{direction, 1}, {direction, -1}};
    }

    private ArrayList<ChessMove> createPawnMove(ChessPosition endPosition) {
        if (endPosition.getRow() == this.endRow) {
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

    @Override
    public ArrayList<ChessMove> calculateMoves() {
        var validForwardMoves = validMovesAlongAllOffsets(
                this.possibleForwardOffsets, forwardRange, CaptureRestriction.NoCapture);
        var validCaptureMoves = validMovesAlongAllOffsets(
                this.possibleCaptureOffsets, CAPTURE_RANGE, CaptureRestriction.NeedsCapture);
        return mergePawnMoves(validForwardMoves, validCaptureMoves);
    }
}
