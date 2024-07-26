package server.services;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.gamesdao.GamesDao;
import dataaccess.gamesdao.GamesDaoInterface;
import models.AuthData;
import models.GameData;
import responses.CreateGameResponse;
import responses.JoinGameResponse;

import java.util.UUID;

public class CreateGameService {

    public CreateGameResponse createGame(String authString, String gameName, DatabaseAccess db){
        //response data//
        Integer responseGameID = null; //not good//
        String responseMessage = null;

        AuthDaoInterface authDao = db.authDAO();
        AuthData foundData;
        try{foundData = authDao.findAuth(authString);}
        catch(DataAccessException ex){ //500 type error//
            responseMessage = "data access error: " + ex.getMessage();
            return new CreateGameResponse(responseGameID, responseMessage);
        }

        if(foundData == null){
            //auth token not found - not logged in//
            responseMessage = "error: unauthorized";
        }
        else{
            GamesDaoInterface gamesDao= db.gamesDAO();
            GameData foundGame;

            try{ foundGame = gamesDao.findGame(gameName);}
            catch (DataAccessException ex){ //500 type error//
                responseMessage = "data access error: " + ex.getMessage();
                return new CreateGameResponse(responseGameID, responseMessage);
            }

            if(foundGame == null){
                //no existing game with that name - all good!
                responseGameID = Math.abs(UUID.randomUUID().hashCode()); //hope this gives us what we want>//

                try{gamesDao.addGame(new GameData(responseGameID, null, null, gameName, new ChessGame()));}
                catch(DataAccessException ex){ //500 type error//
                    responseMessage = "data access error: " + ex.getMessage();
                    return new CreateGameResponse(responseGameID, responseMessage);
                }

            }
            else{
                //found a game that already has that name//
                responseMessage = "error: bad request"; //game name is already taken//
            }
        }
        return new CreateGameResponse(responseGameID, responseMessage);

    }
}
