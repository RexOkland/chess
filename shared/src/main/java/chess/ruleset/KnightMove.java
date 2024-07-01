package chess.ruleset;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMove implements PieceMove{
    Collection<ChessMove> moves = new HashSet<ChessMove>();
    public Collection<ChessMove> calculateMoves (ChessBoard board, ChessPiece piece, ChessPosition location){
        int row = location.getRow() - 1;
        int col = location.getColumn() - 1;
        if(location.relativePositiion(1, 2).onBoard()
                && ( (board.boardArray[row + 1][col + 2] == null)
                || (board.boardArray[row + 1][col + 2].teamColor != piece.teamColor))
        ){
            moves.add(new ChessMove(location, location.relativePositiion(1,2), null));
        }
        if(location.relativePositiion(-1, 2).onBoard()
                && ( (board.boardArray[row - 1][col + 2] == null)
                ||  (board.boardArray[row - 1][col + 2].teamColor != piece.teamColor))
        ){
            moves.add(new ChessMove(location, location.relativePositiion(-1,2), null));
        }
        if(location.relativePositiion(2, 1).onBoard()
                && ( (board.boardArray[row + 2][col + 1] == null)
                || (board.boardArray[row + 2][col + 1].teamColor != piece.teamColor))
        ){
            moves.add(new ChessMove(location, location.relativePositiion(2,1), null));
        }
        if(location.relativePositiion(-2, 1).onBoard()
                && ( (board.boardArray[row - 2][col + 1] == null)
                || (board.boardArray[row - 2][col + 1].teamColor != piece.teamColor))
        ){
            moves.add(new ChessMove(location, location.relativePositiion(-2,1), null));
        }


        if(location.relativePositiion(1, -2).onBoard()
                && ( (board.boardArray[row + 1][col - 2] == null)
                || (board.boardArray[row + 1][col - 2].teamColor != piece.teamColor))
        ){
            moves.add(new ChessMove(location, location.relativePositiion(1,-2), null));
        }
        if(location.relativePositiion(-1, -2).onBoard()
                && ( (board.boardArray[row - 1][col - 2] == null)
                || (board.boardArray[row - 1][col - 2].teamColor != piece.teamColor))
        ){
            moves.add(new ChessMove(location, location.relativePositiion(-1,-2), null));
        }
        if(location.relativePositiion(2, -1).onBoard()
                && ( (board.boardArray[row + 2][col - 1] == null)
                || (board.boardArray[row + 2][col - 1].teamColor != piece.teamColor))
        ){
            moves.add(new ChessMove(location, location.relativePositiion(2,-1), null));
        }
        if(location.relativePositiion(-2, -1).onBoard()
                && ( (board.boardArray[row - 2][col - 1] == null)
                || (board.boardArray[row - 2][col - 1].teamColor != piece.teamColor))
        ){
            moves.add(new ChessMove(location, location.relativePositiion(-2,-1), null));
        }





        return moves;
    };
}
