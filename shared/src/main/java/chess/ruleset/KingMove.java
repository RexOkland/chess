package chess.ruleset;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KingMove implements PieceMove{
    Collection<ChessMove> moves = new HashSet<ChessMove>();
    public Collection<ChessMove> calculateMoves (ChessBoard board, ChessPiece piece, ChessPosition location){
        int col_index = location.getColumn() -1;
        int row_index = location.getRow() -1;
        if(row_index > 0){ //bottom three//
            for(int i = -1; i < 2; ++i){
                if(   location.relativePositiion(-1,i).onBoard()
                    && ( (board.boardArray[row_index - 1][col_index + i] == null)
                    || (board.boardArray[row_index - 1][col_index + i].teamColor != piece.teamColor) ) ){
                    moves.add(new ChessMove(location, location.relativePositiion(-1, i), null));
                }
            }
        };
        if(row_index < 7){ //top three//
            for(int i = -1; i < 2; ++i){
                if( (   location.relativePositiion(1,i).onBoard()
                        && (board.boardArray[row_index + 1][col_index + i] == null)
                        || (board.boardArray[row_index + 1][col_index + i].teamColor != piece.teamColor)) ){
                    moves.add(new ChessMove(location, location.relativePositiion(1, i), null));
                }
            }
        };
        if(col_index > 0){
            if( location.relativePositiion(0,-1).onBoard()
                    && ( (board.boardArray[row_index][col_index - 1] == null)
                    || (board.boardArray[row_index][col_index - 1].teamColor != piece.teamColor)) ){
                moves.add(new ChessMove(location, location.relativePositiion(0, -1), null));
            }
        };
        if(col_index < 7){
            if( location.relativePositiion(0,1).onBoard()
                    && ( (board.boardArray[row_index][col_index + 1] == null)
                    || (board.boardArray[row_index][col_index + 1].teamColor != piece.teamColor)) ){
                moves.add(new ChessMove(location, location.relativePositiion(0, 1), null));
            }
        };

        return moves;
    };
}
