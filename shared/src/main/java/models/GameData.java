package models;

import chess.ChessGame;

public record GameData (int id, String white, String black, String gameName, ChessGame game){

    public GameData setWhite(String user){
        return new GameData(id, user, black, gameName, game);
    }

    public GameData setBlack(String user){
        return new GameData(id, white, user, gameName, game);
    }
}
