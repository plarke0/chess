package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MoveCalculator {
    private ChessPiece piece;
    private ChessBoard board;
    private ChessPosition piecePosition;

    public MoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        this.piece = piece;
        this.board = board;
        this.piecePosition = piecePosition;
    }

    public Collection<ChessMove> validMovesAlongSteps(int xStep, int yStep, int range) {
        var validMoves = new ArrayList<ChessMove>();
        int newX = piecePosition.getColumn();
        int newY = piecePosition.getRow();

        while (range != 0) {
            newX += xStep;
            newY += yStep;
            if (newX < 1 || newY < 1 || newX > 8 || newY > 8) {
                break;
            }

            var newPosition = new ChessPosition(newX, newY);
            ChessPiece newPositionPiece = board.getPiece(newPosition);
            if (newPositionPiece != null) {
                if (newPositionPiece.getTeamColor() != piece.getTeamColor()) {
                    validMoves.add(new ChessMove(piecePosition, newPosition, null));
                }
                break;
            }
            validMoves.add(new ChessMove(piecePosition, newPosition, null));
            range--;
        }
        return validMoves;
    }
}
