package chess.ruleset;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMove implements PieceMove{

    Collection<ChessMove> moves = new HashSet<ChessMove>();
    int i = 1;
    public Collection<ChessMove> calculateMoves (ChessBoard board, ChessPiece piece, ChessPosition location){
       RookMove fromRook = new RookMove();
       fromRook.calculateMoves(board,piece,location);

       BishopMove fromBishop = new BishopMove();
       fromBishop.calculateMoves(board,piece,location);

       moves.addAll(fromRook.getMoves());
       moves.addAll(fromBishop.getMoves());

        return moves;
    };
}
