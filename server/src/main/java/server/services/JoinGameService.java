package server.services;

import chess.ChessGame;
import dataaccess.DatabaseHolder;
import dataaccess.authdao.AuthDao;
import dataaccess.gamesdao.GamesDao;
import models.AuthData;
import models.GameData;
import responses.CreateGameResponse;
import responses.JoinGameResponse;

import java.util.Objects;
import java.util.UUID;

public class JoinGameService {

    public JoinGameResponse joinGame(String authString, String team, int gameID, DatabaseHolder db){
        //response data//
        String responseMessage = null;

        AuthDao authDao = db.AuthDAO();
        AuthData foundAuth = authDao.findAuth(authString);
        if(foundAuth == null){
            //auth token not found - not logged in//
            responseMessage = "error: unauthorized";
        }
        else{
            GamesDao gameDao = db.GamesDAO();
            GameData foundGame = gameDao.findGame(gameID);
            if(foundGame == null){
                //game not found - requesting a game that doesn't exist//
                responseMessage = "error: bad request";
            }
            else{
                if(Objects.equals(team, "WHITE")){
                    //user wants to join as white//
                    if(foundGame.white() != null){
                        responseMessage = "error: already taken";
                    }
                    else{
                        gameDao.updateGame(foundGame.setWhite(foundAuth.username()));
                    }

                }else if(Objects.equals(team, "BLACK")) {
                    //user wants to join as black//
                    if(foundGame.black() != null){
                        responseMessage = "error: already taken";
                    }
                    else{
                        gameDao.updateGame(foundGame.setBlack(foundAuth.username()));
                    }

                }else{
                    responseMessage = "error: bad request";
                }
            }
        }
        return new JoinGameResponse(responseMessage);
    }
}
