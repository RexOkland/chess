package chess.ruleset;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMove implements PieceMove{

    private Collection<ChessMove> moves = new HashSet<ChessMove>();
    public Collection<ChessMove> calculateMoves (ChessBoard board, ChessPiece piece, ChessPosition location){

        //up-left//
        int i = 1;
        while(location.relativePositiion(i,-i).onBoard()){
            if(board.getPiece(location.relativePositiion(i,-i)) == null){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(i,-i),
                                null
                        )
                );
                ++i;
            }
            else if(board.getPiece(location.relativePositiion(i,-i)).teamColor != piece.teamColor){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(i,-i),
                                null
                        )
                );
                break;
            }
            else{
                break;
            }

        };
        //up-right//
        i = 1;
        while(location.relativePositiion(i,i).onBoard()){
            if(board.getPiece(location.relativePositiion(i,i)) == null){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(i,i),
                                null
                        )
                );
                ++i;
            }
            else if(board.getPiece(location.relativePositiion(i,i)).teamColor != piece.teamColor){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(i,i),
                                null
                        )
                );
                break;
            }
            else{
                break;
            }

        };
        //down-right//
        i = 1;
        while(location.relativePositiion(-i,i).onBoard()){
            if(board.getPiece(location.relativePositiion(-i,i)) == null){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(-i,i),
                                null
                        )
                );
                ++i;
            }
            else if(board.getPiece(location.relativePositiion(-i,i)).teamColor != piece.teamColor){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(-i,i),
                                null
                        )
                );
                break;
            }
            else{
                break;
            }

        };
        //down-left//
        i = 1;
        while(location.relativePositiion(-i,-i).onBoard()){
            if(board.getPiece(location.relativePositiion(-i,-i)) == null){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(-i,-i),
                                null
                        )
                );
                ++i;
            }
            else if(board.getPiece(location.relativePositiion(-i,-i)).teamColor != piece.teamColor){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(-i,-i),
                                null
                        )
                );
                break;
            }
            else{
                break;
            }

        };


        return moves;
    };

    public Collection<ChessMove> getMoves(){
        return this.moves;
    }
}
