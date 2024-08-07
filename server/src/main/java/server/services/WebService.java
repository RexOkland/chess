package server.services;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseAccess;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.gamesdao.GamesDaoInterface;
import models.AuthData;
import models.GameData;

public class WebService {

    public void isValidToken(DatabaseAccess db, String authString) throws Exception {
        AuthDaoInterface authDao = db.authDAO();
        AuthData foundData = null;
        try{
            foundData = authDao.findAuth(authString);
        }
        catch (DataAccessException DataEx){
            throw new Exception("Authentication token not found");
        }
        if (foundData == null) {
            throw new Exception("Authentication token not found");
        }
    }
    public GameData findGameData(DatabaseAccess db, Integer gameID) throws Exception{
        //find the game the websocket is dealing with//

        GamesDaoInterface gameDao = db.gamesDAO();
        GameData foundGame = null;
        try{
            foundGame = gameDao.findGame(gameID);
        }
        catch (DataAccessException DataEx){
            throw new Exception("Authentication token not found");
        }
        if(foundGame == null){throw new Exception("Authentication token not found");}
        else{ return foundGame; }
    }

}
