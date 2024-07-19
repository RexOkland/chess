package models;

import chess.ChessGame;

public record GameData (){
    static int gameID;
    static String whiteUser;
    static String blackUser;
    static String gameName;
    static ChessGame chessGame;
}
