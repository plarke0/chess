package chess.move_calculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class MoveCalculator {
    protected ChessPiece piece;
    protected ChessBoard board;
    protected ChessPosition piecePosition;
    protected enum CaptureRestriction {
        NoCapture,
        CanCapture,
        NeedsCapture
    }

    public MoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        this.piece = piece;
        this.board = board;
        this.piecePosition = piecePosition;
    }

    public ArrayList<ChessMove> calculateMoves() {
        var pieceCalculator = switch (this.piece.getPieceType()) {
            case KING -> new KingMoveCalculator(this.piece, this.board, this.piecePosition);
            case QUEEN -> new QueenMoveCalculator(this.piece, this.board, this.piecePosition);
            case BISHOP -> new BishopMoveCalculator(this.piece, this.board, this.piecePosition);
            case ROOK -> new RookMoveCalculator(this.piece, this.board, this.piecePosition);
            case KNIGHT -> new KnightMoveCalculator(this.piece, this.board, this.piecePosition);
            case PAWN -> new PawnMoveCalculator(this.piece, this.board, this.piecePosition);
        };
        return pieceCalculator.calculateMoves();
    }

    protected ArrayList<ChessMove> validMovesAlongAllOffsets(int[][] movementOffsets, int range, CaptureRestriction captureRestriction) {
        var validMoves = new ArrayList<ChessMove>();
        for (int[] offset : movementOffsets) {
            ArrayList<ChessMove> movesAlongOffset = validMovesAlongOffset(offset, range, captureRestriction);
            validMoves.addAll(movesAlongOffset);
        }
        return validMoves;
    }

    protected ArrayList<ChessMove> validMovesAlongOffset(int[] offset, int range, CaptureRestriction captureRestriction) {
        var validMoves = new ArrayList<ChessMove>();
        ChessPosition newPosition = this.piecePosition;

        loop:
        while (range != 0) {
            newPosition = getUpdatedPosition(newPosition, offset);
            if (newPosition == null) break;

            ChessPiece newPositionPiece = this.board.getPiece(newPosition);
            ChessMove newMove = new ChessMove(this.piecePosition, newPosition, null);
            switch (captureRestriction) {
                case CaptureRestriction.NoCapture -> {
                    if (newPositionPiece != null) break loop;
                }
                case CaptureRestriction.CanCapture -> {
                    if (newPositionPiece != null) {
                        if (newPositionPiece.getTeamColor() != this.piece.getTeamColor()) {
                            validMoves.add(newMove);
                        }
                        break loop;
                    }
                }
                case CaptureRestriction.NeedsCapture -> {
                    if (newPositionPiece != null && newPositionPiece.getTeamColor() != this.piece.getTeamColor()){
                        validMoves.add(newMove);
                    }
                    break loop;
                }
            }
            validMoves.add(newMove);
            range--;
        }
        return validMoves;
    }

    protected ChessPosition getUpdatedPosition(ChessPosition currentPosition, int[] offset) {
        int newRow = currentPosition.getRow() + offset[0];
        int newCol = currentPosition.getColumn() + offset[1];
        if (newRow < 1 || newCol < 1 || newRow > 8 || newCol > 8) {
            return null;
        }
        return new ChessPosition(newRow, newCol);
    }
}
