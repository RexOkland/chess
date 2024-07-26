package server.services;

import chess.ChessGame;
import dataaccess.DatabaseHolder;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.gamesdao.GamesDao;
import dataaccess.gamesdao.GamesDaoInterface;
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

        AuthDaoInterface authDao = db.authDAO();
        AuthData foundAuth = authDao.findAuth(authString);
        if(foundAuth == null){
            //auth token not found - not logged in//
            responseMessage = "error: unauthorized";
        }
        else{
            GamesDaoInterface gameDao = db.gamesDAO();
            GameData foundGame = gameDao.findGame(gameID);
            if(foundGame == null){
                //game not found - requesting a game that doesn't exist//
                responseMessage = "error: bad request";
            }
            else{
                if(Objects.equals(team, "WHITE")){
                    //user wants to join as white//
                    if(foundGame.whiteUsername() != null){
                        responseMessage = "error: already taken";
                    }
                    else{
                        gameDao.updateGame(foundGame.setWhite(foundAuth.username()));
                    }

                }else if(Objects.equals(team, "BLACK")) {
                    //user wants to join as black//
                    if(foundGame.blackUsername() != null){
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
