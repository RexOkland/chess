package chess;

import chess.*;

import java.util.Arrays;

import static java.lang.Character.toLowerCase;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public ChessPiece[][] boardArray = new ChessPiece[8][8];

    /*public ChessBoard() {
        boardArray = new ChessPiece[8][8];
    }*/

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //throw new RuntimeException("Not implemented");
        //attempting to implement 06/29/2024 @ 12:21pm//
        boardArray[position.getRow() - 1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        //throw new RuntimeException("Not implemented");
        //attempting to implement 06/29/2024 @ 12:20pm//

        return boardArray[position.getRow()-1][position.getColumn()-1]; //returns piece @ given position//
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() { //initializes board too//
        boardArray = new ChessPiece[8][8]; //fresh array//

        //WHITE TEAM://
        initializeWhite();
        //BLACK TEAM://
        initializeBlack();

    }

    public void printBoard() {
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray.length; j++) {
                ChessPiece piece = boardArray[i][j];
                if (piece == null) {
                    System.out.print("| ");
                } else {
                    char pieceChar = getPieceChar(piece); //turning the enum into single letter pieces//
                    System.out.print( String.format("|%c",pieceChar) );
                }
            }
            System.out.print("|");
            System.out.println();
        }
    }

    private char getPieceChar(ChessPiece piece) {
        //we're just making the types into single letter values//
        //attempting to implement 06/29/2024 @ 1:15pm//
        char c;
        switch (piece.getPieceType()) {
            case PAWN:
                c = 'P'; break;
            case ROOK:
                c = 'R'; break;
            case KNIGHT:
                c = 'N'; break;
            case BISHOP:
                c = 'B'; break;
            case QUEEN:
                c = 'Q'; break;
            case KING:
                c = 'K'; break;
            default:
                c = '?'; break; //unrecognized piece... not good//
        }
        if(piece.teamColor == ChessGame.TeamColor.WHITE){c = toLowerCase(c);}
        return c;
    }

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        board.printBoard();
    }

    public void initializeWhite(){
        //pawns//
        for(int i = 0; i < 8; ++i){ //white pawn on each location in row 2//
            boardArray[1][i]= new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        };
        //rooks o castles//
        boardArray[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        boardArray[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        //knights o horses//
        boardArray[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        boardArray[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        //bishops//
        boardArray[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        boardArray[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        //king y queen//
        boardArray[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        boardArray[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
    };
    public void initializeBlack(){
        //pawns//
        for(int i = 0; i < 8; ++i){ //black pawn on each location in row 7//
            boardArray[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        };
        //rooks o castles//
        boardArray[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        boardArray[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        //knights o horses//
        boardArray[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        boardArray[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        //bishops//
        boardArray[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        boardArray[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        //king y queen//
        boardArray[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        boardArray[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(boardArray, that.boardArray);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(boardArray);
    }
}
