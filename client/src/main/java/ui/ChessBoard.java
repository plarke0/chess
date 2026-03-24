package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.ChessPiece;
import chess.ChessPosition;
import static ui.EscapeSequences.*;

public class ChessBoard {

    private static PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public static void drawBoard(chess.ChessBoard board, Boolean isWhiteView) {
        var boardArray = new ChessPiece[8][8];
        for (ChessPosition position : board) {
            int row = position.getRow();
            int col = position.getColumn();
            boardArray[row-1][col-1] = board.getPiece(position);
        }

        drawColumnHeaders(isWhiteView);
        drawRows(isWhiteView);
        drawColumnHeaders(isWhiteView);
    }

    private static void drawColumnHeaders(Boolean isWhiteView) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(EMPTY);
        for (int headerNumber = 0; headerNumber < 8; headerNumber++) {
            drawColumnHeader(headerNumber, isWhiteView);
        }
        out.print(EMPTY);

        resetColors();
        out.println();
    }

    private static void drawColumnHeader(int headerNumber, Boolean isWhiteView) {
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        drawHeader(headerNumber, isWhiteView, headers);
    }

    private static void drawRows(Boolean isWhiteView) {
        for (int i = 0; i < 8; i++) {
            drawRowHeader(i, isWhiteView);
            drawBoardRow();
            drawRowHeader(i, isWhiteView);

            resetColors();
            out.println();
        }
    }

    private static void drawRowHeader(int headerNumber, Boolean isWhiteView) {
        String[] headers = {"8", "7", "6", "5", "4", "3", "2", "1"};
        drawHeader(headerNumber, isWhiteView, headers);
    }

    private static void drawHeader(int headerNumber, Boolean isWhiteView, String[] headers) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        int headerIndex = headerNumber;
        if (!isWhiteView) {
            headerIndex = 7 - headerNumber;
        }

        printHeaderText(headers[headerIndex]);
    }

    private static void printHeaderText(String headerValue) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print("\u2003" + headerValue + " ");
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
