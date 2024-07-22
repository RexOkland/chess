package chess.ruleset;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMove implements PieceMove{

    private Collection<ChessMove> moves = new HashSet<ChessMove>();
    public Collection<ChessMove> calculateMoves (ChessBoard board, ChessPiece piece, ChessPosition location){

        //up//
        int i = 1;
        while(location.relativePositiion(i, 0).onBoard()){
            if(board.getPiece(location.relativePositiion(i,0)) == null){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(i,0),
                                null
                        )
                );
                ++i;
            }
            else if(board.getPiece(location.relativePositiion(i, 0)).teamColor != piece.teamColor){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(i, 0),
                                null
                        )
                );
                break;
            }
            else{
                break;
            }

        };
        //right//
        i = 1;
        while(location.relativePositiion(0, i).onBoard()){
            if(board.getPiece(location.relativePositiion(0, i)) == null){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(0, i),
                                null
                        )
                );
                ++i;
            }
            else if(board.getPiece(location.relativePositiion(0, i)).teamColor != piece.teamColor){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(0, i),
                                null
                        )
                );
                break;
            }
            else{
                break;
            }

        };
        //down//
        i = 1;
        while(location.relativePositiion(-i, 0).onBoard()){
            if(board.getPiece(location.relativePositiion(-i, 0)) == null){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(-i, 0),
                                null
                        )
                );
                ++i;
            }
            else if(board.getPiece(location.relativePositiion(-i, 0)).teamColor != piece.teamColor){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(-i, 0),
                                null
                        )
                );
                break;
            }
            else{
                break;
            }

        };
        //left//
        i = 1;
        while(location.relativePositiion(0, -i).onBoard()){
            if(board.getPiece(location.relativePositiion(0, -i)) == null){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(0, -i),
                                null
                        )
                );
                ++i;
            }
            else if(board.getPiece(location.relativePositiion(0, -i)).teamColor != piece.teamColor){
                moves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(0, -i),
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
