package chess.ruleset;

import chess.*;
import java.util.Collection;

public class PawnMove implements PieceMove {
    //attempting to implement 6/29/2024 @ 12:13pm//
    private Collection<ChessMove> findMoves (ChessBoard board, ChessPiece piece, ChessPosition location){
        //attempting to implement 06/29/2024 @ 12:04//
        Collection<ChessMove> calculatedMoves = null;
        //assuming that white starts on the bottom (column 2) and black starts on the top (column 7)
        if(piece.teamColor == ChessGame.TeamColor.WHITE){
            //check to see if it's in initial position//
            if(location.getRow() == 2){
                //piece IS in initial position//

            }else{
                //piece is NOT in initial position//

            }
        }else if (piece.teamColor == ChessGame.TeamColor.BLACK) {
            //check to see if it's in initial position//
            if(location.getRow() == 7){
                //piece IS in initial position//

            }else{
                //piece is NOT in initial position//

            }

        }else{
            throw new RuntimeException("something aint right - no team color identified");
        }
        //TODO: this//
        throw new RuntimeException("not done yet");
    }
}
