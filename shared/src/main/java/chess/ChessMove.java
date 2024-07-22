package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition start;
    private ChessPosition end;
    private ChessPiece.PieceType pieceType;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        //attempting to implement 06/29/2024 @ 11:51am//
        this.start = startPosition;
        this.end = endPosition;
        this.pieceType = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        //throw new RuntimeException("Not implemented");
        //attempting to implement 06/29/2024 @ 11:52am//
        return start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        //throw new RuntimeException("Not implemented");
        //attempting to implement 06/29/2024 @ 11:52am//
        return end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        //throw new RuntimeException("Not implemented");
        //attempting to implement 06/29/2024 @ 11:52am//
        return this.pieceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(start, chessMove.start) && Objects.equals(end, chessMove.end) && pieceType == chessMove.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, pieceType);
    }

    @Override
    public String toString() {
        return "ChessMove{" +
                "start=" + start +
                ", end=" + end +
                ", pieceType=" + pieceType +
                '}' + "\n";
    }
}
