package chess;

import java.util.Collection;
import chess.*;
/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTurn;
    private ChessBoard gameBoard;


    public ChessGame() {
        this.currentTurn = TeamColor.WHITE;
        gameBoard = new ChessBoard();
        gameBoard.resetBoard(); //when we create a chess game, we create a new board right?//
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = gameBoard.getPiece(startPosition);
        return piece.pieceMoves(gameBoard,startPosition); //return moves for the piece found @ startPosition//
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //okay we're going to move a piece from point A to point B//

        ChessPosition from = move.getStartPosition();
        ChessPosition to = move.getEndPosition();
        ChessPiece piece = gameBoard.boardArray[from.getRow()-1][from.getColumn()-1];

        gameBoard.boardArray[from.getRow()-1][from.getColumn()-1] = null;
        //TODO: should I be setting this point in the array equal to null? or just it's piece type//
        //clearing where piece was on the array// point A //


        if(move.getPromotionPiece() != null){
            piece.promote(move.getPromotionPiece());
            gameBoard.addPiece(to, piece);
        }
        else{
            gameBoard.addPiece(to, piece);
        }
        //adding in the piece in its new spot// point B //
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //so we've got to see if any of the opposing team's pieces have the King's position in their moves//
        for(int i = 0; i < 8; ++i){ //CHECKING ALL 64 SPOTS//
            for(int j = 0; j < 8; ++j){
                if(gameBoard.boardArray[i][j] == null){
                    //EMPTY SPOT ON BOARD - no moves to be found//
                    continue;//do nothing//
                }
                else if(gameBoard.boardArray[i][j].getTeamColor() == teamColor){
                    //IT'S OUR OWN TEAM - moves effecting the king don't matter//
                    continue;//do nothing//
                }
                else {
                    //OPPOSING PIECE//
                    Collection<ChessMove> movesFromPoint = gameBoard.boardArray[i][j].pieceMoves(gameBoard, new ChessPosition(i+1, j+1));
                    for(ChessMove m : movesFromPoint){
                        if( (gameBoard.getPiece(m.getEndPosition()).pieceType == ChessPiece.PieceType.KING)
                            && (gameBoard.getPiece(m.getEndPosition()).teamColor == teamColor) ){
                            return true; //if these are both true, the piece has the potential to attack the king... osea check//
                        }
                    }
                }
            }
        }
        return false; //if we check the whole board and nobody can touch the king... it's not check.
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.gameBoard;
    }
}
