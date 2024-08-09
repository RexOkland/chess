package chess;

import chess.*;
import java.util.Arrays;
import java.util.Collection;

import ui.EscapeSequences;

import static java.lang.Character.toLowerCase;
import static ui.EscapeSequences.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public ChessPiece[][] boardArray = new ChessPiece[8][8];

    public ChessBoard() {
        boardArray = new ChessPiece[8][8];
    }

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
        char c;
        switch (piece.getPieceType()) {
            case PAWN: c = 'P'; break;
            case ROOK: c = 'R'; break;
            case KNIGHT: c = 'N'; break;
            case BISHOP: c = 'B'; break;
            case QUEEN: c = 'Q'; break;
            case KING: c = 'K'; break;
            default: c = '?'; break; //unrecognized piece... not good//
        }
        if(piece.teamColor == ChessGame.TeamColor.WHITE){c = toLowerCase(c);}
        return c;
    };

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

    public boolean available(ChessPosition position, ChessGame.TeamColor color){
        if (onBoard(position)) {return false;}
        if(boardArray[position.getRow()-1][position.getColumn()-1] == null){ //no piece on position... available//
            return true;
        }
        else if(getPiece(position).getTeamColor() != color){ //piece on the position is on opposite team//
            return true;
        }
        else{
            return false;
        }
    }

    public boolean empty(ChessPosition position){
        if (onBoard(position)) {return false;}
        return (boardArray[position.getRow()-1][position.getColumn()-1] == null);
    }

    public boolean onBoard(ChessPosition position) {
        if(position.getRow() > 8){
            return true;
        }
        if(position.getRow() < 0){
            return true;
        }
        if(position.getColumn() > 8){
            return true;
        }
        return position.getColumn() < 0;
    }


    //print functions//
    public void printBlackPerspective(){
        int counter = 1;
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println("    h   g   f  e   d  c   b   a ");
        for (int i = 0; i < 8; i++) {
            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(SET_TEXT_COLOR_WHITE + " " + (i + 1) +" ");
            System.out.print(SET_TEXT_COLOR_BLACK);
            ++counter;
            for (int j = 8; j > 0; j--) {
                ++counter;
                ChessPiece piece = this.getPiece(new ChessPosition(i+1, j));
                printFormattedBoard(counter, piece);
            }
            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(SET_TEXT_COLOR_WHITE + " " + (i + 1) +" ");
            System.out.println();
        }
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println("    h   g   f  e   d  c   b   a ");
    }

    public void printBlackHighlightPerspective(Collection<ChessPosition> markedSpots){
        int counter = 1;
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println("    h   g   f  e   d  c   b   a ");
        for (int i = 0; i < 8; i++) {
            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(SET_TEXT_COLOR_WHITE + " " + (i + 1) +" ");
            System.out.print(SET_TEXT_COLOR_BLACK);
            ++counter;
            for (int j = 8; j > 0; j--) {
                ++counter;
                ChessPosition currentSpot = new ChessPosition(i+1, j);
                ChessPiece piece = this.getPiece(currentSpot);
                if(!markedSpots.contains(currentSpot)){
                    printFormattedBoard(counter, piece);
                }
                else{printMarkedCell(piece);}
            }
            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(SET_TEXT_COLOR_WHITE + " " + (i + 1) +" ");
            System.out.println();
        }
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println("    h   g   f  e   d  c   b   a ");
    }

    public void printFormattedBoard(int counter, ChessPiece piece) {
        if (piece == null) {
            if(counter % 2 == 0){System.out.print(SET_BG_COLOR_DARK_GREEN);}
            else{System.out.print(SET_BG_COLOR_WHITE);}
            System.out.print(EMPTY);
        } else {
            if(counter % 2 == 0){System.out.print(SET_BG_COLOR_DARK_GREEN);}
            else{System.out.print(SET_BG_COLOR_WHITE);}
            System.out.printf(getUnicode(piece));
        }
    }

    public void printMarkedCell(ChessPiece piece) {
        System.out.print(SET_BG_COLOR_GREEN);
        if (piece == null) {
            System.out.print(EMPTY);
        }
        else {
            System.out.printf(getUnicode(piece));
        }
    }

    public void printWhitePerspective(){
        int counter = 1;
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println("    a   b   c  d   e  f   g   h ");
        for (int i = 0; i < 8; i++) {
            ++counter;
            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(SET_TEXT_COLOR_WHITE + " " + (8 - i) +" ");
            System.out.print(SET_TEXT_COLOR_BLACK);
            for (int j = 8; j > 0; j--) {
                ++counter;
                ChessPiece piece = this.getPiece(new ChessPosition(8-i, 9-j));
                printFormattedBoard(counter, piece);
            }
            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(SET_TEXT_COLOR_WHITE + " " + (8 - i) +" ");
            System.out.println();
        }
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println("    a   b   c  d   e  f   g   h ");
    }

    public void printWhiteHighlightPerspective(Collection<ChessPosition> markedSpots){
        int counter = 1;
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println("    a   b   c  d   e  f   g   h ");
        for (int i = 0; i < 8; i++) {
            ++counter;
            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(SET_TEXT_COLOR_WHITE + " " + (8 - i) +" ");
            System.out.print(SET_TEXT_COLOR_BLACK);
            for (int j = 8; j > 0; j--) {
                ++counter;
                ChessPosition currentSpot = new ChessPosition(8-i, 9-j);
                ChessPiece piece = this.getPiece(currentSpot);
                if(!markedSpots.contains(currentSpot)){
                    printFormattedBoard(counter, piece);
                }
                else{printMarkedCell(piece);}
            }
            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print(SET_TEXT_COLOR_WHITE + " " + (8 - i) +" ");
            System.out.println();
        }
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println("    a   b   c  d   e  f   g   h ");
    }


    public String getUnicode(ChessPiece p){
        if(p.teamColor == ChessGame.TeamColor.WHITE){
            return switch (p.getPieceType()) {
                case PAWN -> WHITE_PAWN;
                case ROOK -> WHITE_ROOK;
                case KNIGHT -> WHITE_KNIGHT;
                case BISHOP -> WHITE_BISHOP;
                case QUEEN -> WHITE_QUEEN;
                case KING -> WHITE_KING;
                default -> "something off"; //unrecognized piece//
            };
        }
        else{
            return switch (p.getPieceType()) {
                case PAWN -> BLACK_PAWN;
                case ROOK -> BLACK_ROOK;
                case KNIGHT -> BLACK_KNIGHT;
                case BISHOP -> BLACK_BISHOP;
                case QUEEN -> BLACK_QUEEN;
                case KING -> BLACK_KING;
                default -> "something is off"; //unrecognized piece//
            };
        }
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(boardArray, that.boardArray);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(boardArray);
    }
}
