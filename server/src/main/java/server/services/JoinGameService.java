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

import javax.xml.crypto.Data;
import java.util.Objects;
import java.util.UUID;

public class JoinGameService {

    public JoinGameResponse joinGame(String authString, String team, int gameID, DatabaseAccess db){
        //response data//
        String responseMessage = null;

        AuthDaoInterface authDao = db.authDAO();
        AuthData foundAuth;

        try{foundAuth = authDao.findAuth(authString);}
        catch (DataAccessException ex){ //500 type error//
            responseMessage = "data access error: " + ex.getMessage();
            return new JoinGameResponse(responseMessage);
        }

        if(foundAuth == null){
            //auth token not found - not logged in//
            responseMessage = "error: unauthorized";
        }
        else{
            GamesDaoInterface gameDao = db.gamesDAO();
            GameData foundGame;
            try{foundGame = gameDao.findGame(gameID);}
            catch(DataAccessException ex){ // 500 type error//
                responseMessage = "data access error: " + ex.getMessage();
                return new JoinGameResponse(responseMessage);
            }

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
                        try{gameDao.updateGame(foundGame.setWhite(foundAuth.username()));}
                        catch(DataAccessException ex){ // 500 type error//
                            responseMessage = "data access error: " + ex.getMessage();
                            return new JoinGameResponse(responseMessage);
                        }
                    }

                }else if(Objects.equals(team, "BLACK")) {
                    //user wants to join as black//
                    if(foundGame.blackUsername() != null){
                        responseMessage = "error: already taken";
                    }
                    else{
                        try{gameDao.updateGame(foundGame.setBlack(foundAuth.username()));}
                        catch(DataAccessException ex){
                            responseMessage = "data access error: " + ex.getMessage();
                            return new JoinGameResponse(responseMessage);
                        }
                    }

                }else{
                    responseMessage = "error: bad request";
                }
            }
        }
        return new JoinGameResponse(responseMessage);
    }
}
