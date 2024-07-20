package server.services;

import chess.ChessGame;
import dataaccess.DatabaseHolder;
import dataaccess.authdao.AuthDao;
import dataaccess.gamesdao.GamesDao;
import models.AuthData;
import models.GameData;
import responses.CreateGameResponse;

import java.util.UUID;

public class CreateGameService {

    public CreateGameResponse createGame(String authString, String gameName, DatabaseHolder db){
        //response data//
        Integer responseGameID = null; //not good//
        String responseMessage = null;

        AuthDao authDao = db.AuthDAO();
        AuthData foundData = authDao.findAuth(authString);
        if(foundData == null){
            //auth token not found - not logged in//
            responseMessage = "error: unauthorized";
        }
        else{
            GamesDao gamesDao= db.GamesDAO();
            GameData foundGame = gamesDao.findGame(gameName);
            if(foundGame == null){
                //no existing game with that name - all good!
                responseGameID = Math.abs(UUID.randomUUID().hashCode()); //hope this gives us what we want>//
                gamesDao.addGame(new GameData(responseGameID, null, null, gameName, new ChessGame()));

            }
            else{
                //found a game that already has that name//
                responseMessage = "error: bad request"; //game name is already taken//
            }
        }
        return new CreateGameResponse(responseGameID, responseMessage);

    }
}
