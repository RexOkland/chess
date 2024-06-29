package chess.ruleset;

import chess.*;
import java.util.Collection;
import java.util.HashSet;

public class PawnMove implements PieceMove {
    Collection<ChessMove> moves = new HashSet<ChessMove>();
    public Collection<ChessMove> calculateMoves (ChessBoard board, ChessPiece piece, ChessPosition location){
        //attempting to implement 06/29/2024 @ 12:04pm//
        //attempting to implement 6/29/2024 @ 12:13pm//
        moves.clear();
        //assuming that white starts on the bottom (row 2) and black starts on the top (row 7)
        if(piece.teamColor == ChessGame.TeamColor.WHITE){
            //looking to see if the first two spaces in front of pawn are AVAILABLE//
            if (board.boardArray[location.getRow() - 1 + 1][location.getColumn() - 1] == null &&
            location.onBoard() ) {
                if(location.getRow() + 1 == 8){
                    for(ChessPiece.PieceType p : ChessPiece.PieceType.values()){
                        if((p == ChessPiece.PieceType.QUEEN)
                            || (p == ChessPiece.PieceType.ROOK)
                            || (p == ChessPiece.PieceType.BISHOP)
                            || (p == ChessPiece.PieceType.KNIGHT)) {
                            moves.add(
                                    new ChessMove(
                                            location,
                                            new ChessPosition(location.getRow() + 1, location.getColumn()),
                                            //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                            p
                                    )//add the space directly in front as a move//
                            );
                        }
                    }
                }
                else{
                    moves.add(
                            new ChessMove(
                                    location,
                                    new ChessPosition(location.getRow()+1, location.getColumn()),
                                    //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                    null
                            )//add the space directly in front as a move//
                    );
                }
                if(location.getRow() == 2){
                    //piece IS in initial position - check for additional square//
                    if(board.boardArray[location.getRow() - 1 + 2][location.getColumn() - 1] == null){
                        moves.add(
                                new ChessMove(
                                        location,
                                        new ChessPosition(location.getRow()+2, location.getColumn()),
                                        //ChessPiece.PieceType.PAWN... it doesn't want this//
                                        null
                                )//add the space two spots in front as a move//
                        );
                    }
                }
            }
            //now we've got to look at the diagonal squares to see if there's potential moves there//
                //check right//
            if (    (board.boardArray[location.getRow() - 1 + 1][location.getColumn() - 1 + 1] != null)
                    && board.boardArray[location.getRow() - 1 + 1][location.getColumn() - 1 + 1].teamColor == ChessGame.TeamColor.BLACK
                    && location.relativePositiion(1,1).onBoard() ) {//check to see if the diagonal is on the board//
                if(location.getRow() + 1 == 8){
                    for(ChessPiece.PieceType p : ChessPiece.PieceType.values()){
                        if((p == ChessPiece.PieceType.QUEEN)
                                || (p == ChessPiece.PieceType.ROOK)
                                || (p == ChessPiece.PieceType.BISHOP)
                                || (p == ChessPiece.PieceType.KNIGHT)) {
                            moves.add(
                                    new ChessMove(
                                            location,
                                            location.relativePositiion(1,1),
                                            //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                            p
                                    )//add the space directly in front as a move//
                            );
                        }
                    }
                }
                else{
                    moves.add(
                            new ChessMove(
                                    location,
                                    location.relativePositiion(1,1),
                                    //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                    null
                            )//add the space directly in front as a move//
                    );
                }
            };
                //check left//
            if (    (board.boardArray[location.getRow() - 1 + 1][location.getColumn() - 1 - 1] != null)
                    && board.boardArray[location.getRow() - 1 + 1][location.getColumn() - 1 - 1].teamColor == ChessGame.TeamColor.BLACK
                    && location.relativePositiion(1,-1).onBoard() ) {
                if(location.getRow() + 1 == 8){
                    for(ChessPiece.PieceType p : ChessPiece.PieceType.values()){
                        if((p == ChessPiece.PieceType.QUEEN)
                                || (p == ChessPiece.PieceType.ROOK)
                                || (p == ChessPiece.PieceType.BISHOP)
                                || (p == ChessPiece.PieceType.KNIGHT)) {
                            moves.add(
                                    new ChessMove(
                                            location,
                                            location.relativePositiion(1,-1),
                                            //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                            p
                                    )//add the space directly in front as a move//
                            );
                        }
                    }
                }
                else{
                    moves.add(
                            new ChessMove(
                                    location,
                                    location.relativePositiion(1,-1),
                                    //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                    null
                            )//add the space directly in front as a move//
                    );
                }
            };

            //BLACK//

        }else if (piece.teamColor == ChessGame.TeamColor.BLACK) {
            //looking to see if the first two spaces in front of pawn are AVAILABLE//
            if (board.boardArray[location.getRow() - 1 - 1][location.getColumn() - 1] == null &&
                    location.onBoard() ) {
                if(location.getRow() - 1 == 1){
                    for(ChessPiece.PieceType p : ChessPiece.PieceType.values()){
                        if((p == ChessPiece.PieceType.QUEEN)
                                || (p == ChessPiece.PieceType.ROOK)
                                || (p == ChessPiece.PieceType.BISHOP)
                                || (p == ChessPiece.PieceType.KNIGHT)) {
                            moves.add(
                                    new ChessMove(
                                            location,
                                            new ChessPosition(location.getRow() - 1, location.getColumn()),
                                            //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                            p
                                    )//add the space directly in front as a move//
                            );
                        }
                    }
                }
                else{
                    moves.add(
                            new ChessMove(
                                    location,
                                    new ChessPosition(location.getRow() - 1, location.getColumn()),
                                    //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                    null
                            )//add the space directly in front as a move//
                    );
                }
                if(location.getRow() == 7){
                    //piece IS in initial position - check for additional square//
                    if(board.boardArray[location.getRow() - 1 - 2][location.getColumn() - 1] == null){
                        moves.add(
                                new ChessMove(
                                        location,
                                        new ChessPosition(location.getRow()-2, location.getColumn()),
                                        //ChessPiece.PieceType.PAWN... it doesn't want this//
                                        null
                                )//add the space two spots in front as a move//
                        );
                    }
                }
            }
            //now we've got to look at the diagonal squares to see if there's potential moves there//
            //check right//
            if (    (board.boardArray[location.getRow() - 1 - 1][location.getColumn() - 1 + 1] != null)
                    && board.boardArray[location.getRow() - 1 - 1][location.getColumn() - 1 + 1].teamColor == ChessGame.TeamColor.WHITE
                    && location.relativePositiion(-1,1).onBoard() ) {//check to see if the diagonal is on the board//
                if(location.getRow() - 1 == 1){
                    for(ChessPiece.PieceType p : ChessPiece.PieceType.values()){
                        if((p == ChessPiece.PieceType.QUEEN)
                                || (p == ChessPiece.PieceType.ROOK)
                                || (p == ChessPiece.PieceType.BISHOP)
                                || (p == ChessPiece.PieceType.KNIGHT)) {
                            moves.add(
                                    new ChessMove(
                                            location,
                                            location.relativePositiion(-1,1),
                                            //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                            p
                                    )//add the space directly in front as a move//
                            );
                        }
                    }
                }
                else{
                    moves.add(
                            new ChessMove(
                                    location,
                                    location.relativePositiion(-1,1),
                                    //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                    null
                            )//add the space directly in front as a move//
                    );
                }
            };
            //check left//
            if (    (board.boardArray[location.getRow() - 1 - 1][location.getColumn() - 1 - 1] != null)
                    && board.boardArray[location.getRow() - 1 - 1][location.getColumn() - 1 - 1].teamColor == ChessGame.TeamColor.WHITE
                    && location.relativePositiion(-1,-1).onBoard() ) {
                if(location.getRow() - 1 == 1){
                    for(ChessPiece.PieceType p : ChessPiece.PieceType.values()){
                        if((p == ChessPiece.PieceType.QUEEN)
                                || (p == ChessPiece.PieceType.ROOK)
                                || (p == ChessPiece.PieceType.BISHOP)
                                || (p == ChessPiece.PieceType.KNIGHT)) {
                            moves.add(
                                    new ChessMove(
                                            location,
                                            location.relativePositiion(-1,-1),
                                            //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                            p
                                    )//add the space directly in front as a move//
                            );
                        }
                    }
                }
                else{
                    moves.add(
                            new ChessMove(
                                    location,
                                    location.relativePositiion(-1,-1),
                                    //ChessPiece.PieceType.PAWN ... it doesn't want this//
                                    null
                            )//add the space directly in front as a move//
                    );
                }
            };

        }else{
            throw new RuntimeException("something aint right - no team color identified");
        }

        return moves;
    }
}
