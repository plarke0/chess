package chess;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Iterable<ChessPosition>, Cloneable{
    private ChessPiece[][] boardArray;

    public ChessBoard() {
        this.boardArray = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int arrayX = position.getColumn() - 1;
        int arrayY = position.getRow() - 1;
        boardArray[arrayY][arrayX] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int arrayX = position.getColumn() - 1;
        int arrayY = position.getRow() - 1;
        return boardArray[arrayY][arrayX];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.boardArray = new ChessPiece[8][8];
        for (ChessGame.TeamColor color : ChessGame.TeamColor.values()) {
            int mainRow, pawnRow;
            if (color == ChessGame.TeamColor.WHITE) {
                mainRow = 1;
                pawnRow = 2;
            } else {
                mainRow = 8;
                pawnRow = 7;
            }

            //Add pawns to the board
            for (int i = 1; i <= 8; i++) {
                var position = new ChessPosition(pawnRow, i);
                addPiece(position, new ChessPiece(color, ChessPiece.PieceType.PAWN));
            }

            //Add rooks to the board
            addPiece(new ChessPosition(mainRow, 1), new ChessPiece(color, ChessPiece.PieceType.ROOK));
            addPiece(new ChessPosition(mainRow, 8), new ChessPiece(color, ChessPiece.PieceType.ROOK));

            //Add knights to the board
            addPiece(new ChessPosition(mainRow, 2), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
            addPiece(new ChessPosition(mainRow, 7), new ChessPiece(color, ChessPiece.PieceType.KNIGHT));

            //Add bishops to the board
            addPiece(new ChessPosition(mainRow, 3), new ChessPiece(color, ChessPiece.PieceType.BISHOP));
            addPiece(new ChessPosition(mainRow, 6), new ChessPiece(color, ChessPiece.PieceType.BISHOP));

            //Add queen and king to the board
            addPiece(new ChessPosition(mainRow, 4), new ChessPiece(color, ChessPiece.PieceType.QUEEN));
            addPiece(new ChessPosition(mainRow, 5), new ChessPiece(color, ChessPiece.PieceType.KING));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(boardArray, that.boardArray);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(boardArray);
    }

    @Override
    public String toString() {
        String string = "";
        for (ChessPiece[] row : this.boardArray) {
            String rowString = "|";
            for (ChessPiece piece : row) {
                if (piece != null) {
                    rowString += piece.toString();
                } else {
                    rowString += " ";
                }
                rowString += "|";
            }
            rowString += "\n";
            string = rowString + string;
        }
        return string;
    }

    public class BoardIterator implements Iterator<ChessPosition> {
        int row = 0;
        int col = 0;
        int boardHeight = boardArray.length;
        int boardWidth = boardArray[0].length;

        @Override
        public boolean hasNext() {
            return row < boardHeight && col < boardWidth;
        }

        @Override
        public ChessPosition next() {
            ChessPosition currentPosition = new ChessPosition(row, col);
            col++;
            if (col >= boardWidth) {
                col = 0;
                row ++;
            }
            return currentPosition;
        }
    }

    @Override
    public Iterator<ChessPosition> iterator() {
        return new BoardIterator();
    }

    @Override
    public void forEach(Consumer action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<ChessPosition> spliterator() {
        return Iterable.super.spliterator();
    }

    @Override
    protected ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            clone.boardArray = new ChessPiece[8][8];
            for (int i = 0; i < 8; i++) {
                clone.boardArray[i] = Arrays.copyOf(boardArray[i], 8);
            }
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
