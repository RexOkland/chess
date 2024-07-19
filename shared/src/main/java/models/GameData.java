package models;

import chess.ChessGame;

public record GameData (int id, String white, String black, String gameName, ChessGame game){}
