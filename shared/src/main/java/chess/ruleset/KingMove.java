package chess.ruleset;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ruleset.PieceMove;

import java.util.Collection;
import java.util.HashSet;

public class KingMove implements PieceMove {
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPiece piece, ChessPosition pos){
        Collection<ChessMove> foundMoves = new HashSet<ChessMove>();
        //we're going to check all eight spots that a King could potentially go//
        boolean topLeft = true;
        boolean topCenter = true;
        boolean topRight = true;

        boolean midLeft = true;
        boolean midRight = true;

        boolean bottomLeft = true;
        boolean bottomCenter = true;
        boolean bottomRight = true;

        //finding potential landing spots, removing unneccesary ones//
        if(pos.getRow() ==1){//bottom row//
            bottomLeft = false; bottomCenter = false; bottomRight = false;
        }
        else if(pos.getRow() ==8){//top row//
            topLeft = false; topCenter = false; topRight = false;
        }

        if(pos.getColumn() ==1){//far-left column//
            topLeft = false; midLeft = false; bottomLeft = false;
        }
        else if(pos.getColumn() ==8){//far-right column//
            topRight = false; midRight = false; bottomRight = false;
        }
        //checking each potential spot now//
        //tops//
        if(topLeft){
            ChessPosition newSpot = new ChessPosition(pos.getRow()+1, pos.getColumn()-1);
            boolean available = board.available(newSpot, piece.getTeamColor());
            if(available){
                ChessMove moveToAdd = new ChessMove(pos, newSpot, null);
                foundMoves.add(moveToAdd);
            }
        }
        if(topCenter){
            ChessPosition newSpot = new ChessPosition(pos.getRow()+1, pos.getColumn());
            boolean available = board.available(newSpot, piece.getTeamColor());
            if(available){
                ChessMove moveToAdd = new ChessMove(pos, newSpot, null);
                foundMoves.add(moveToAdd);
            }
        }
        if(topRight){
            ChessPosition newSpot = new ChessPosition(pos.getRow()+1, pos.getColumn()+1);
            boolean available = board.available(newSpot, piece.getTeamColor());
            if(available){
                ChessMove moveToAdd = new ChessMove(pos, newSpot, null);
                foundMoves.add(moveToAdd);
            }
        }
        //mids//
        if(topLeft){
            ChessPosition newSpot = new ChessPosition(pos.getRow(), pos.getColumn()-1);
            boolean available = board.available(newSpot, piece.getTeamColor());
            if(available){
                ChessMove moveToAdd = new ChessMove(pos, newSpot, null);
                foundMoves.add(moveToAdd);
            }
        }
        if(topLeft){
            ChessPosition newSpot = new ChessPosition(pos.getRow(), pos.getColumn()+1);
            boolean available = board.available(newSpot, piece.getTeamColor());
            if(available){
                ChessMove moveToAdd = new ChessMove(pos, newSpot, null);
                foundMoves.add(moveToAdd);
            }
        }
        //bottoms//
        if(bottomLeft){
            ChessPosition newSpot = new ChessPosition(pos.getRow()-1, pos.getColumn()-1);
            boolean available = board.available(newSpot, piece.getTeamColor());
            if(available){
                ChessMove moveToAdd = new ChessMove(pos, newSpot, null);
                foundMoves.add(moveToAdd);
            }
        }
        if(bottomCenter){
            ChessPosition newSpot = new ChessPosition(pos.getRow()-1, pos.getColumn());
            boolean available = board.available(newSpot, piece.getTeamColor());
            if(available){
                ChessMove moveToAdd = new ChessMove(pos, newSpot, null);
                foundMoves.add(moveToAdd);
            }
        }
        if(bottomRight){
            ChessPosition newSpot = new ChessPosition(pos.getRow()-1, pos.getColumn()+1);
            boolean available = board.available(newSpot, piece.getTeamColor());
            if(available){
                ChessMove moveToAdd = new ChessMove(pos, newSpot, null);
                foundMoves.add(moveToAdd);
            }
        }
        return foundMoves;
    }
}
