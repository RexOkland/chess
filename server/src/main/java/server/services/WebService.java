package server.services;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseAccess;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.gamesdao.GamesDaoInterface;
import models.AuthData;
import models.GameData;

public class WebService {

    public boolean isValidToken(DatabaseAccess db, String authString) throws Exception {
        AuthDaoInterface authDao = db.authDAO();
        AuthData foundData = null;
        try{
            foundData = authDao.findAuth(authString);
        }
        catch (DataAccessException DataEx){
            throw new Exception("Authentication token not found");
        }
        return (foundData != null);
    }
    public ChessGame findGameData(DatabaseAccess db, Integer gameID) throws Exception{
        //find the game the websocket is dealing with//

        GamesDaoInterface gameDao = db.gamesDAO();
        GameData foundGame = null;
        try{
            foundGame = gameDao.findGame(gameID);
        }
        catch (DataAccessException DataEx){
            throw new Exception("Authentication token not found");
        }
        return foundGame.game();
    }

}
