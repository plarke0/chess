package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences.*;

public class ChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARACTERS = 1;

    private static PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public static void drawBoard(chess.ChessBoard board, Boolean isWhiteView) {
        var boardArray = new ChessPiece[8][8];
        for (ChessPosition position : board) {
            int row = position.getRow();
            int col = position.getColumn();
            boardArray[row-1][col-1] = board.getPiece(position);
        }

        drawColumnHeaders(isWhiteView);
        drawRows();
        drawColumnHeaders(isWhiteView);
    }

    private static void drawColumnHeaders() {

    }

    private static void drawRowHeaders() {

    }

    private static void drawBoardRow() {

    }

    private static void drawBoardSquare() {

    }

    private static void setSquareColor() {

    }

    private static void drawChessPiece() {

    }
}
