package chess;

import chess.movement.MoveCalculator;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    private static final String WHITE_KING = " ♔ ";
    private static final String WHITE_QUEEN = " ♕ ";
    private static final String WHITE_BISHOP = " ♗ ";
    private static final String WHITE_KNIGHT = " ♘ ";
    private static final String WHITE_ROOK = " ♖ ";
    private static final String WHITE_PAWN = " ♙ ";
    private static final String BLACK_KING = " ♚ ";
    private static final String BLACK_QUEEN = " ♛ ";
    private static final String BLACK_BISHOP = " ♝ ";
    private static final String BLACK_KNIGHT = " ♞ ";
    private static final String BLACK_ROOK = " ♜ ";
    private static final String BLACK_PAWN = " ♟ ";

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var moveCalculator = new MoveCalculator(this, board, myPosition);
        return moveCalculator.calculateMoves();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.getTeamColor() && type == that.getPieceType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        String piece;
        if (this.pieceColor == ChessGame.TeamColor.WHITE) {
            piece = switch (this.type) {
                case PAWN -> WHITE_PAWN;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case BISHOP -> WHITE_BISHOP;
                case QUEEN -> WHITE_QUEEN;
                case KING -> WHITE_KING;
            };
        } else {
            piece = switch (this.type) {
                case PAWN -> BLACK_PAWN;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case BISHOP -> BLACK_BISHOP;
                case QUEEN -> BLACK_QUEEN;
                case KING -> BLACK_KING;
            };
        }
        return piece;
    }
}
