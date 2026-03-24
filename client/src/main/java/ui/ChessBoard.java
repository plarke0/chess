package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.ChessPiece;
import chess.ChessPosition;
import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARACTERS = 3;

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

    private static void drawColumnHeaders(Boolean isWhiteView) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARACTERS));
        for (int headerNumber = 0; headerNumber < BOARD_SIZE_IN_SQUARES; headerNumber++) {
            drawHeader(headerNumber, isWhiteView);
        }

        out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARACTERS));
        resetColors();

        out.println();
    }

    private static void drawHeader(int headerNumber, Boolean isWhiteView) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        int headerIndex = headerNumber;
        if (!isWhiteView) {
            headerIndex = 7 - headerNumber;
        }
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        int prefixLength = SQUARE_SIZE_IN_CHARACTERS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARACTERS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(headers[headerIndex]);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void drawRows() {

    }

    private static void drawRowHeader() {

    }

    private static void printHeaderText(String headerValue) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(headerValue);
    }

    private static void drawBoardRow() {

    }

    private static void drawBoardSquare() {

    }

    private static void setSquareColor() {

    }

    private static void drawChessPiece() {

    }

    private static void resetColors() {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }
}
