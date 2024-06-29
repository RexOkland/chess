package chess;

import chess.*;
/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPosition[][] boardArray;
    public ChessBoard() {
        
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
        position.setResidingPiece(piece); //puts given piece on given position//
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
        return position.getResidingPiece(); //returns piece @ given position//
    }

    /**
     * Sets the boa rd to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() { //initializes board too//

        //throw new RuntimeException("Not implemented");
        //attempting to implement 06/29/2024 @ 12:22pm//

        boardArray = new ChessPosition[8][8]; //fresh array//

        //WHITE TEAM://
        initializeWhite();
        //BLACK TEAM://
        initializeBlack();

        System.out.println("PIECES ARE SET");
    }


    public void initializeWhite(){
        //pawns//
        for(int i = 0; i < 8; ++i){ //white pawn on each location in row 2//
            boardArray[1][i].setResidingPiece(
                    new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)
            );
        };
        //rooks o castles//
        boardArray[0][0].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK)
        );
        boardArray[0][7].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK)
        );
        //knights o horses//
        boardArray[0][1].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT)
        );
        boardArray[0][6].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT)
        );
        //bishops//
        boardArray[0][2].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP)
        );
        boardArray[0][5].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP)
        );
        //king y queen//
        boardArray[0][3].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING)
        );
        boardArray[0][4].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN)
        );
    };
    public void initializeBlack(){
        //pawns//
        for(int i = 0; i < 8; ++i){ //black pawn on each location in row 7//
            boardArray[6][i].setResidingPiece(
                    new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)
            );
        };
        //rooks o castles//
        boardArray[7][0].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK)
        );
        boardArray[7][7].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK)
        );
        //knights o horses//
        boardArray[7][1].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT)
        );
        boardArray[7][6].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT)
        );
        //bishops//
        boardArray[7][2].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP)
        );
        boardArray[7][5].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP)
        );
        //king y queen//
        boardArray[7][3].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING)
        );
        boardArray[7][4].setResidingPiece(
                new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN)
        );
    }
}
