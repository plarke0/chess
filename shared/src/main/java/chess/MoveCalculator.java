package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {
    protected ChessPiece piece;
    protected ChessBoard board;
    protected ChessPosition piecePosition;
    protected int[][] movementOffsets;
    protected int range;

    public MoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        this.piece = piece;
        this.board = board;
        this.piecePosition = piecePosition;
    }

    public Collection<ChessMove> calculateMoves() {
        return switch (piece.getPieceType()) {
            case KING -> (ArrayList<ChessMove>) new KingMoveCalculator(piece, board, piecePosition).calculateMoves();
            case QUEEN -> (ArrayList<ChessMove>) new QueenMoveCalculator(piece, board, piecePosition).calculateMoves();
            case BISHOP -> (ArrayList<ChessMove>) new BishopMoveCalculator(piece, board, piecePosition).calculateMoves();
            case ROOK -> (ArrayList<ChessMove>) new RookMoveCalculator(piece, board, piecePosition).calculateMoves();
            case KNIGHT -> (ArrayList<ChessMove>) new KnightMoveCalculator(piece, board, piecePosition).calculateMoves();
            case PAWN -> (ArrayList<ChessMove>) new PawnMoveCalculator(piece, board, piecePosition).calculateMoves();
        };
    }

    protected ChessPosition getUpdatedPosition(ChessPosition currentPosition, int[] offset) {
        int newRow = currentPosition.getRow();
        int newCol = currentPosition.getColumn();
        newRow += offset[1];
        newCol += offset[0];
        if (newRow < 1 || newCol < 1 || newRow > 8 || newCol > 8) {
            return null;
        }
        return new ChessPosition(newRow, newCol);
    }

    public Collection<ChessMove> validMovesAlongOffsets() {
        var validMoves = new ArrayList<ChessMove>();

        for (int[] offset : movementOffsets) {
            int remainingRange = range;
            ChessPosition newPosition = piecePosition;

            while (remainingRange != 0) {
                newPosition = getUpdatedPosition(newPosition, offset);
                if (newPosition == null) {
                    break;
                }

                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (newPositionPiece != null) {
                    if (newPositionPiece.getTeamColor() != piece.getTeamColor()) {
                        validMoves.add(new ChessMove(piecePosition, newPosition, null));
                    }
                    break;
                }
                validMoves.add(new ChessMove(piecePosition, newPosition, null));
                remainingRange--;
            }
        }
        return validMoves;
    }
}
