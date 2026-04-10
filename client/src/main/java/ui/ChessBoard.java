package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import static ui.EscapeSequences.*;

public class ChessBoard {

    private static PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    private boolean isWhiteView;
    private ChessPosition highlightSource;
    private ArrayList<ChessPosition> highlightedPositions;

    private enum HighlightType {
        NONE,
        SOURCE,
        DESTINATION
    }

    public void drawHighlightedBoard(chess.ChessBoard board, Boolean isWhiteView, ChessPosition source) {
        ChessPiece piece = board.getPiece(source);
        if (piece != null) {
            highlightSource = source;
            highlightedPositions = new ArrayList<>();
            for (ChessMove move : piece.pieceMoves(board, source)) {
                highlightedPositions.add(move.getEndPosition());
            }
        } else {
            highlightSource = null;
            highlightedPositions = null;
        }

        drawBoard(board, isWhiteView);

        highlightSource = null;
        highlightedPositions = null;
    }

    public void drawBoard(chess.ChessBoard board, Boolean isWhiteView) {
        this.isWhiteView = isWhiteView;

        var boardArray = new ChessPiece[8][8];
        for (ChessPosition position : board) {
            int row = position.getRow();
            int col = position.getColumn();
            boardArray[row-1][col-1] = board.getPiece(position);
        }

        out.println();
        drawColumnHeaders();
        drawRows(boardArray);
        drawColumnHeaders();
    }

    private void drawColumnHeaders() {
        drawRowHeaderPadding();
        for (int headerNumber = 0; headerNumber < 8; headerNumber++) {
            drawColumnHeader(headerNumber);
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

    private void drawColumnHeader(int headerNumber) {
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        drawHeader(headerNumber, headers);
    }

    private void drawRows(ChessPiece[][] boardArray) {
        for (int i = 0; i < 8; i++) {
            drawRowHeader(i);
            drawBoardRow(i, boardArray);
            drawRowHeader(i);

            resetColors();
            out.println();
        }
    }

    private void drawRowHeader(int headerNumber) {
        String[] headers = {"8", "7", "6", "5", "4", "3", "2", "1"};
        drawHeader(headerNumber, headers);
    }

    private void drawHeader(int headerNumber, String[] headers) {
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

    private void drawBoardRow(int rowNumber, ChessPiece[][] boardArray) {
       for (int columnNumber = 0; columnNumber < 8; columnNumber++) {
            drawBoardSquare(boardArray, rowNumber, columnNumber);
        }
    }

    private void drawBoardSquare(ChessPiece[][] boardArray, int rowNumber, int columnNumber) {
        int trueRowIndex = 8 - rowNumber;
        int trueColumnIndex = columnNumber + 1;
        if (!isWhiteView) {
            trueRowIndex = rowNumber + 1;
            trueColumnIndex = 8 - columnNumber;
        }
        ChessPosition squarePosition = new ChessPosition(trueRowIndex, trueColumnIndex);
        if (highlightedPositions != null && highlightedPositions.contains(squarePosition)) {
            setSquareColor(rowNumber, columnNumber, HighlightType.DESTINATION);
        } else if (squarePosition.equals(highlightSource)) {
            setSquareColor(rowNumber, columnNumber, HighlightType.SOURCE);
        } else {
            setSquareColor(rowNumber, columnNumber, HighlightType.NONE);
        }

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

    private static void setSquareColor(int rowNumber, int columnNumber, HighlightType highlightType) {
        if (rowNumber % 2 == columnNumber % 2) {
            setWhite(highlightType);
        } else {
            setBlack(highlightType);
        }
    }

    private static void drawChessPiece(ChessPiece piece) {
        out.print(piece);
    }

    private static void resetColors() {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void setWhite(HighlightType highlightType) {
        switch (highlightType) {
            case NONE -> out.print(SET_BG_COLOR_WHITE);
            case SOURCE -> out.print(SET_BG_COLOR_YELLOW);
            case DESTINATION -> out.print(SET_BG_COLOR_GREEN);
        }
        out.print(RESET_TEXT_COLOR);
    }

    private static void setBlack(HighlightType highlightType) {
        switch (highlightType) {
            case NONE -> out.print(SET_BG_COLOR_BLACK);
            case SOURCE -> out.print(SET_BG_COLOR_DARK_YELLOW);
            case DESTINATION -> out.print(SET_BG_COLOR_DARK_GREEN);
        }
        out.print(RESET_TEXT_COLOR);
    }
}
