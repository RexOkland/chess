package chess.ruleset;

import chess.*;
import java.util.Collection;

public class PawnMove implements PieceMove {
    //attempting to implement 6/29/2024 @ 12:13pm//
    private Collection<ChessMove> findMoves (ChessBoard board, ChessPiece piece, ChessPosition location){
        //attempting to implement 06/29/2024 @ 12:04//

        Collection<ChessMove> calculatedMoves = null;
        //assuming that white starts on the bottom (row 2) and black starts on the top (row 7)
        if(piece.teamColor == ChessGame.TeamColor.WHITE){
            //looking to see if the first two spaces in front of pawn are AVAILABLE//
            if (board.boardArray[location.getRow() + 1][location.getColumn()] == null &&
            location.onBoard() ) {
                calculatedMoves.add(
                        new ChessMove(
                                location,
                                new ChessPosition(location.getRow()+1, location.getColumn()),
                                ChessPiece.PieceType.PAWN
                        )//add the space directly in front as a move//
                );
                if(location.getRow() == 2){
                    //piece IS in initial position - check for additional square//
                    if(board.boardArray[location.getRow() + 2][location.getColumn()] == null){
                        calculatedMoves.add(
                                new ChessMove(
                                        location,
                                        new ChessPosition(location.getRow()+2, location.getColumn()),
                                        ChessPiece.PieceType.PAWN
                                )//add the space two spots in front as a move//
                        );
                    }
                }
            }
            //now we've got to look at the diagonal squares to see if there's potential moves there//
                //check right//
            if (board.boardArray[location.getRow() + 1][location.getColumn() + 1].teamColor == ChessGame.TeamColor.BLACK &&
                    location.relativePositiion(1,1).onBoard() ) {//check to see if the diagonal is on the board//
                calculatedMoves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(1,1),
                                ChessPiece.PieceType.PAWN
                        )//add the space two spots in front as a move//
                );
            };
                //check left//
            if (board.boardArray[location.getRow() + 1][location.getColumn() - 1].teamColor == ChessGame.TeamColor.BLACK &&
                    location.relativePositiion(1,-1).onBoard() ) {
                calculatedMoves.add(
                        new ChessMove(
                                location,
                                location.relativePositiion(1,-1),
                                ChessPiece.PieceType.PAWN
                        )//add the space two spots in front as a move//
                );
            };

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

        return calculatedMoves;
    }
}
