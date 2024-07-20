package models;

import chess.ChessGame;

public record GameData (int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){

    public GameData setWhite(String user){
        return new GameData(gameID, user, blackUsername, gameName, game);
    }

    public GameData setBlack(String user){
        return new GameData(gameID, whiteUsername, user, gameName, game);
    }
}
