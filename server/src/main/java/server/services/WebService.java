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

    public void removeUserFromGame(DatabaseAccess db, Integer gameID, ChessGame.TeamColor color) throws Exception {
        try{
            GamesDaoInterface gameDao = db.gamesDAO();
            GameData foundGame = gameDao.findGame(gameID);
            GameData newGame;
            if(color == ChessGame.TeamColor.WHITE){
                //duplicate of the game that has the White's spot empty//
                newGame = new GameData(foundGame.gameID(), null, foundGame.blackUsername(), foundGame.gameName(), foundGame.game());
            }
            else{
                //duplicate of the game that has the White's spot empty//
                newGame = new GameData(foundGame.gameID(), foundGame.whiteUsername(), null, foundGame.gameName(), foundGame.game());
            }
             //set that thing to ENDED//
            gameDao.updateGame(newGame); //put that thing back in with new data//
        } catch (DataAccessException e) {
            throw new Exception("We're super cooked");
        }
    }

    public void setGameTurn(DatabaseAccess db, Integer gameID, ChessGame.TeamColor whosTurn) throws Exception {
        try{
            GamesDaoInterface gameDao = db.gamesDAO();
            GameData gd = gameDao.findGame(gameID);
            ChessGame update = gd.game();

            update.setTeamTurn(whosTurn); //switch the turn//

            GameData updatedData = new GameData(gd.gameID(), gd.whiteUsername(), gd.blackUsername(), gd.gameName(), update);
            gameDao.updateGame(updatedData);

        } catch (DataAccessException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void updateBoardForReals(DatabaseAccess db, Integer gameID, ChessGame game) throws Exception {
        try{
            GamesDaoInterface gameDao = db.gamesDAO();
            GameData fg = gameDao.findGame(gameID);
            ChessGame updated = fg.game();

            updated.setBoard(game.getBoard());

            GameData updatedData = new GameData(fg.gameID(), fg.whiteUsername(), fg.blackUsername(), fg.gameName(), updated);
            gameDao.updateGame(updatedData);

        } catch (DataAccessException e) {
            throw new Exception(e.getMessage());
        }
    }

}
