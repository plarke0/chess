package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoveCalculator extends MoveCalculator{
    public PawnMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
    }

    private ArrayList<ChessMove> createPawnMoves(ChessPosition newPosition, int endRow) {
        if (newPosition.getRow() == endRow) {
            return new ArrayList<>(List.of(new ChessMove(piecePosition, newPosition, ChessPiece.PieceType.KNIGHT),
                    new ChessMove(piecePosition, newPosition, ChessPiece.PieceType.BISHOP),
                    new ChessMove(piecePosition, newPosition, ChessPiece.PieceType.ROOK),
                    new ChessMove(piecePosition, newPosition, ChessPiece.PieceType.QUEEN)));
        } else {
            return new ArrayList<>(List.of(new ChessMove(piecePosition, newPosition, null)));
        }
    }

    public Collection<ChessMove> calculateMoves() {
        var validMoves = new ArrayList<ChessMove>();
        int[][] possibleMoveOffsets;
        int[][] possibleCaptureOffsets;
        int direction;
        int startRow, endRow;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
            startRow = 2;
            endRow = 8;
        } else {
            direction = -1;
            startRow = 7;
            endRow = 1;
        }
        int row = piecePosition.getRow();
        boolean inStartRow = row == startRow;
        if (inStartRow) {
            possibleMoveOffsets = new int[][] {{0, direction}, {0, 2*direction}};
        } else {
            possibleMoveOffsets = new int[][] {{0, direction}};
        }
        possibleCaptureOffsets = new int[][] {{-1, direction}, {1, direction}};

        for (int[] moveOffset : possibleMoveOffsets) {
            ChessPosition newPosition = getUpdatedPosition(piecePosition, moveOffset);
            if (newPosition == null) {
                continue;
            }

            ChessPiece newPositionPiece = board.getPiece(newPosition);
            if (newPositionPiece != null) {
                break;
            }
            validMoves.addAll(createPawnMoves(newPosition, endRow));
        }

        for (int[] captureOffset : possibleCaptureOffsets) {
            ChessPosition newPosition = getUpdatedPosition(piecePosition, captureOffset);
            if (newPosition == null) {
                continue;
            }

            ChessPiece newPositionPiece = board.getPiece(newPosition);
            if (newPositionPiece != null && newPositionPiece.getTeamColor() != piece.getTeamColor()) {
                validMoves.addAll(createPawnMoves(newPosition, endRow));
            }
        }

        return validMoves;
    }
}
