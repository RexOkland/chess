package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;
    private ChessPiece residingPiece;
//so the location will keep track of the piece that's sitting on it//
    public ChessPiece getResidingPiece() {
        return residingPiece;
    }

    public void setResidingPiece(ChessPiece residingPiece) {
        this.residingPiece = residingPiece;
    }



    public ChessPosition(int row, int col) {
        //attempting to implement 06/29/2024 @ 11:44am//
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        //throw new RuntimeException("Not implemented");
        //attempting to implement 06/29/2024 @ 11:44am//
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        //throw new RuntimeException("Not implemented");
        //attempting to implement 06/29/2024 @ 11:44am//
        return this.col;
    }

    @Override
    public String toString() {
        //attempting to implement 06/29/2024 @ 11:48am//
        return String.format("col = %d \nrow = %d \n", row, col);
    }
}
