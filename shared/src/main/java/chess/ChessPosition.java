package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;

    //CONSTRUCTORS//
    public ChessPosition(int row, int col) {
        //attempting to implement 06/29/2024 @ 11:44am//
        this.row = row;
        this.col = col;
    }

    //GETTERS/SETTERS - BASIC FUNCTIONS//

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

    //REX'S FUNCTIONS//
    public boolean onBoard(){ //returns a T/F value if a point is on the board//
        if(getColumn() > 8){return false;}
        else if(getColumn() <= 0){return false;}
        else if(getRow() > 8){return false;}
        else return (getRow() > 0);
    }

    public ChessPosition relativePositiion(int r, int c){
        return new ChessPosition((row + r), (col + c));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
