package chess.ruleset;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMove implements PieceMove{
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPiece piece, ChessPosition position) {
        Collection<ChessMove> foundMoves = new HashSet<ChessMove>();

        ChessPosition newSpot = new ChessPosition(position.getRow()+2, position.getColumn());
        //pawns can land in 4 different spots//


        //jumping two//
        ChessPosition middleSpot;
        int specialRow = 2;
        if(piece.getTeamColor() == ChessGame.TeamColor.BLACK){
            specialRow = 7;
        }
        if(position.getRow() == specialRow){
            if(specialRow == 2){
                middleSpot = new ChessPosition(position.getRow()+1, position.getColumn());
                newSpot = new ChessPosition(position.getRow()+2, position.getColumn());
            }
            else{
                middleSpot = new ChessPosition(position.getRow()-1, position.getColumn());
                newSpot = new ChessPosition(position.getRow()-2, position.getColumn());
            }
            boolean empty_a = board.empty(newSpot);
            boolean empty_b = board.empty(middleSpot);
            if(empty_a && empty_b){
                ChessMove moveToAdd = new ChessMove(position, newSpot, null);
                foundMoves.add(moveToAdd);
            }
        }

        //jumping one//
        if((position.getRow() != 8) && (position.getRow() != 1)){ //cannot be run on top/bottom edges//
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                newSpot = new ChessPosition(position.getRow()+1, position.getColumn());
            }
            else{
                newSpot = new ChessPosition(position.getRow()-1, position.getColumn());
            }
            boolean empty = board.empty(newSpot);
            if(empty){
                if((newSpot.getRow() == 1) || (newSpot.getRow() ==8)){ //touchdowns//
                    ChessMove moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.QUEEN);
                    foundMoves.add(moveToAdd);
                    moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.BISHOP);
                    foundMoves.add(moveToAdd);
                    moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.KNIGHT);
                    foundMoves.add(moveToAdd);
                    moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.ROOK);
                    foundMoves.add(moveToAdd);
                }
                else{
                    ChessMove moveToAdd = new ChessMove(position, newSpot, null);
                    foundMoves.add(moveToAdd);
                }
            }
        }


        //attack right//
        if((position.getRow() != 8) && (position.getRow() != 1) && (position.getColumn() != 8)){
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                newSpot = new ChessPosition(position.getRow()+1, position.getColumn()+1);
            }
            else{
                newSpot = new ChessPosition(position.getRow()-1, position.getColumn()+1);
            }

            if((board.available(newSpot, piece.getTeamColor())) && (board.empty(newSpot) == false)){
                if((newSpot.getRow() == 1) || (newSpot.getRow() ==8)){ //touchdowns//
                    ChessMove moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.QUEEN);
                    foundMoves.add(moveToAdd);
                    moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.BISHOP);
                    foundMoves.add(moveToAdd);
                    moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.KNIGHT);
                    foundMoves.add(moveToAdd);
                    moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.ROOK);
                    foundMoves.add(moveToAdd);
                }
                else{
                    ChessMove moveToAdd = new ChessMove(position, newSpot, null);
                    foundMoves.add(moveToAdd);
                }
            }
        }


        //attack left//
        if((position.getRow() != 8) && (position.getRow() != 1) && (position.getColumn() != 1)){
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                newSpot = new ChessPosition(position.getRow()+1, position.getColumn()-1);
            }
            else{
                newSpot = new ChessPosition(position.getRow()-1, position.getColumn()-1);
            }

            if((board.available(newSpot, piece.getTeamColor())) && (board.empty(newSpot) == false)){
                if((newSpot.getRow() == 1) || (newSpot.getRow() ==8)){ //touchdowns//
                    ChessMove moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.QUEEN);
                    foundMoves.add(moveToAdd);
                    moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.BISHOP);
                    foundMoves.add(moveToAdd);
                    moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.KNIGHT);
                    foundMoves.add(moveToAdd);
                    moveToAdd = new ChessMove(position, newSpot, ChessPiece.PieceType.ROOK);
                    foundMoves.add(moveToAdd);
                }
                else{
                    ChessMove moveToAdd = new ChessMove(position, newSpot, null);
                    foundMoves.add(moveToAdd);
                }

            }
        }

        return foundMoves;
    }
}
