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
        var validMoves = new ArrayList<ChessMove>();
        switch (piece.getPieceType()) {
            case KING:
                validMoves = (ArrayList<ChessMove>) new KingMoveCalculator(piece, board, piecePosition).calculateMoves();
                break;
            case QUEEN:
                //validMoves = (ArrayList<ChessMove>) new QueenMoveCalculator(piece, board, piecePosition).calculateMoves();
                break;
            case BISHOP:
                validMoves = (ArrayList<ChessMove>) new BishopMoveCalculator(piece, board, piecePosition).calculateMoves();
                break;
            case ROOK:
                validMoves = (ArrayList<ChessMove>) new RookMoveCalculator(piece, board, piecePosition).calculateMoves();
                break;
            case KNIGHT:
                //validMoves = (ArrayList<ChessMove>) new KnightMoveCalculator(piece, board, piecePosition).calculateMoves();
                break;
            case PAWN:
                //validMoves = (ArrayList<ChessMove>) new PawnMoveCalculator(piece, board, piecePosition).calculateMoves();
                break;

        }
        return validMoves;
    }

    public Collection<ChessMove> validMovesAlongOffsets() {
        var validMoves = new ArrayList<ChessMove>();

        for (int[] offset : movementOffsets) {
            int remainingRange = range;
            int newRow = piecePosition.getRow();
            int newCol = piecePosition.getColumn();
            while (remainingRange != 0) {
                newCol += offset[0];
                newRow += offset[1];
                if (newCol < 1 || newRow < 1 || newCol > 8 || newRow > 8) {
                    break;
                }

                var newPosition = new ChessPosition(newRow, newCol);
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
