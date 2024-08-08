package server.services;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
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
            throw new Exception("Game not found");
        }
        if(foundGame == null){throw new Exception("Game not found");}
        else{ return foundGame; }
    }

    public String getUsernameFromAuth(DatabaseAccess db, String auth) throws Exception{
        AuthData foundAuth = null;
        try{
            AuthDaoInterface authDao = db.authDAO();
            foundAuth = authDao.findAuth(auth);
        }
        catch (Exception ex){
            throw new Exception("Invalid Authentication Token"); //this should never happen//
        }
        if (foundAuth == null){
            throw new Exception("Invalid Authentication Token");
        }
        else{
            return foundAuth.username();
        }

    }

    public void endGameForReals(DatabaseAccess db, Integer gameID) throws Exception {
        try{
            GamesDaoInterface gameDao = db.gamesDAO();
            GameData foundGame = gameDao.findGame(gameID);
            foundGame.game().endGame(); //set that thing to ENDED//
            gameDao.updateGame(foundGame); //put that thing back in with new data//
        } catch (DataAccessException e) {
            throw new Exception("We're cooked");
        }


    }

}
