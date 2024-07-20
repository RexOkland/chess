package server.services;

import dataaccess.DatabaseHolder;
import dataaccess.authdao.AuthDao;
import dataaccess.gamesdao.GamesDao;
import models.AuthData;
import models.GameData;
import responses.ListGamesResponse;

import java.util.Collection;
import java.util.HashSet;

public class ListGamesService {

    public ListGamesResponse getGames(String authString, DatabaseHolder db){
        HashSet<GameData> responseCollection= null;
        String responseString = null;

        AuthDao authDao = db.AuthDAO();
        AuthData foundData = authDao.findAuth(authString);
        if(foundData == null){
            //no token found//
            responseString = "error: unauthorized";
        }
        else{
            //found it! - they're all good//
            responseCollection = db.GamesDAO().getAllGames();
        }
        return new ListGamesResponse(responseCollection, responseString);
    }
}
