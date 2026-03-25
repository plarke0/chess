package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.ChessGame;
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

        out.println();
        drawColumnHeaders(isWhiteView);
        drawRows(isWhiteView, boardArray);
        drawColumnHeaders(isWhiteView);
    }

    private static void drawColumnHeaders(Boolean isWhiteView) {
        drawRowHeaderPadding();
        for (int headerNumber = 0; headerNumber < 8; headerNumber++) {
            drawColumnHeader(headerNumber, isWhiteView);
        }
        drawRowHeaderPadding();

        resetColors();
        out.println();
    }

    private static void drawRowHeaderPadding() {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(EMPTY);
    }

    private static void drawColumnHeader(int headerNumber, Boolean isWhiteView) {
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        drawHeader(headerNumber, isWhiteView, headers);
    }

    private static void drawRows(Boolean isWhiteView, ChessPiece[][] boardArray) {
        for (int i = 0; i < 8; i++) {
            drawRowHeader(i, isWhiteView);
            drawBoardRow(i, isWhiteView, boardArray);
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

    private static void drawBoardRow(int rowNumber, Boolean isWhiteView, ChessPiece[][] boardArray) {
       for (int columnNumber = 0; columnNumber < 8; columnNumber++) {
            drawBoardSquare(boardArray, rowNumber, columnNumber, isWhiteView);
        }
    }

    private static void drawBoardSquare(ChessPiece[][] boardArray, int rowNumber, int columnNumber, Boolean isWhiteView) {
        setSquareColor(rowNumber, columnNumber);

        int rowIndex = rowNumber;
        int columnIndex = columnNumber;
        if (!isWhiteView) {
            rowIndex = 7 - rowNumber;
            columnIndex = 7 - columnIndex;
        }

        ChessPiece piece = boardArray[rowIndex][columnIndex];

        if (piece == null) {
            out.print(EMPTY);
        } else {
            drawChessPiece(piece);
        }
    }

    private static void setSquareColor(int rowNumber, int columnNumber) {
        if (rowNumber % 2 == columnNumber % 2) {
            setWhite();
        } else {
            setBlack();
        }
    }

    private static void drawChessPiece(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            switch (piece.getPieceType()) {
                case PAWN -> out.print(WHITE_PAWN);
                case KNIGHT -> out.print(WHITE_KNIGHT);
                case ROOK -> out.print(WHITE_ROOK);
                case BISHOP -> out.print(WHITE_BISHOP);
                case QUEEN -> out.print(WHITE_QUEEN);
                case KING -> out.print(WHITE_KING);
            }
        } else {
            switch (piece.getPieceType()) {
                case PAWN -> out.print(BLACK_PAWN);
                case KNIGHT -> out.print(BLACK_KNIGHT);
                case ROOK -> out.print(BLACK_ROOK);
                case BISHOP -> out.print(BLACK_BISHOP);
                case QUEEN -> out.print(BLACK_QUEEN);
                case KING -> out.print(BLACK_KING);
            }
        }
    }

    private static void resetColors() {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void setWhite() {
        out.print(SET_BG_COLOR_WHITE);
        out.print(RESET_TEXT_COLOR);
    }

    private static void setBlack() {
        out.print(SET_BG_COLOR_BLACK);
        out.print(RESET_TEXT_COLOR);
    }
}
