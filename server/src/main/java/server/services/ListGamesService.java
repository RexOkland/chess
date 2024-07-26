package server.services;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.gamesdao.GamesDao;
import dataaccess.gamesdao.GamesDaoInterface;
import models.AuthData;
import models.GameData;
import responses.ListGamesResponse;

import java.util.Collection;
import java.util.HashSet;

public class ListGamesService {

    public ListGamesResponse getGames(String authString, DatabaseAccess db){
        HashSet<GameData> responseCollection= null;
        String responseString = null;

        AuthDaoInterface authDao = db.authDAO();
        AuthData foundData;
        try{foundData = authDao.findAuth(authString);}
        catch (DataAccessException ex){
            responseString = "data access error: " + ex.getMessage();
            return new ListGamesResponse(responseCollection, responseString);
        }

        if(foundData == null){
            //no token found//
            responseString = "error: unauthorized";
        }
        else{
            //found it! - they're all good//
            GamesDaoInterface gamesDao = db.gamesDAO();
            try{responseCollection = gamesDao.getAllGames();}
            catch(DataAccessException ex){// 500 type error//
                responseString = "data access error: " + ex.getMessage();
                return new ListGamesResponse(responseCollection, responseString);
            }
        }
        return new ListGamesResponse(responseCollection, responseString);
    }
}
