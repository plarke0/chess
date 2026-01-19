package chess;

import java.util.Collection;

public class QueenMoveCalculator extends MoveCalculator{
    public QueenMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition piecePosition) {
        super(piece, board, piecePosition);
        this.movementOffsets = new int[][] {{-1,-1}, {0,-1}, {1,-1}, {-1,0},
                                            {1,0}, {-1,1}, {0,1}, {1,1}};
        this.range = -1;
    }

    public Collection<ChessMove> calculateMoves() {
        return validMovesAlongOffsets();
    }
}
