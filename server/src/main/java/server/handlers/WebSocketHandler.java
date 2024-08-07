package server.handlers;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.mysql.cj.conf.ConnectionUrlParser;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import models.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.services.WebService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.messages.UpdateBoardMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@WebSocket
public class WebSocketHandler  {
    Map<Integer, Set<Session>> sessionMap;
    Session actingSession;
    DatabaseAccess databaseHolder;
    WebService service;


    public WebSocketHandler(DatabaseAccess databaseHolder){
        this.databaseHolder = databaseHolder;
        this.service = new WebService();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String input) throws Exception {
        this.actingSession = session; //whoever sends this message becomes the acting/active session//

        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(input, UserGameCommand.class);
        try{
            if(command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                MakeMoveCommand moveCommand = gson.fromJson(input, MakeMoveCommand.class);
                this.handleMoveCommand(moveCommand);
            }
            else{
                this.handleCommand(command);
            }

        } catch (Exception ex) {
            //todo: currently throwing exceptions that'll be caught here... do we do anything with them?//
            throw new Exception(ex.getMessage());
        }


    };

    public void handleCommand(UserGameCommand cmd) throws Exception{
        try {
            switch (cmd.getCommandType()) {
                //todo: i think that im supposed to be taking in an auth token somehow...//
                //todo: would have to change onMessage params//
                case CONNECT -> this.handleConnect(cmd.getGameID());
                case RESIGN -> this.handleResign(cmd.getGameID());
                case LEAVE -> this.handleLeave();
            }
        }
        catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }
    public void handleMoveCommand(MakeMoveCommand cmd) throws Exception{
        try{
            //todo: i think that im supposed to be taking in an auth token somehow...//
            Integer gameID = cmd.getGameID();
            ChessMove move = cmd.getMove();
            this.handleMakeMove(gameID, move);
        }
        catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void handleMakeMove(Integer gameID, ChessMove move) throws Exception {
        Gson gson = new Gson();
        try{
            ChessGame game = service.findGameData(databaseHolder, gameID).game();
            game.makeMove(move); //logic is all built out... in theory//

            String start = move.getStartPosition().print();
            String end = move.getEndPosition().print();
            String notificationString = String.format("move made: %s -> %s", start, end);

            NotificationMessage notificationObject = new NotificationMessage(notificationString);
            String notificationJson = gson.toJson(notificationObject);

            UpdateBoardMessage updatedBoardObject = new UpdateBoardMessage(game);//will be updated//
            String boardJson = gson.toJson(updatedBoardObject);

            Set<Session> peopleInvolved = sessionMap.get(gameID);
            for(Session s : peopleInvolved){
                //acting session doesn't get the notification of their own move//
                if(s != actingSession){ s.getRemote().sendString(notificationJson); }
                s.getRemote().sendString(boardJson);
            }
        }
        catch (Exception ex) {
            this.sendError(ex);
            //todo: do we need to throw these still?//
            throw new Exception(ex.getMessage()); //passing along any exception we get//
        }
    }

    public void handleConnect(Integer gameID) throws Exception {
        Gson gson = new Gson();
        try{
            //find game... make sure it's real//
            ChessGame game = service.findGameData(databaseHolder, gameID).game();

            //add the message sender to their game//
            sessionMap.get(gameID).add(actingSession);

            //send them the updated version of the board//
            UpdateBoardMessage updatedBoardObject = new UpdateBoardMessage(game);//will be updated//
            String boardJson = gson.toJson(updatedBoardObject);
            actingSession.getRemote().sendString(boardJson);
        }
        catch (Exception ex){
            this.sendError(ex);
            //todo: do we need to throw these still?//
            throw new Exception(ex.getMessage());
        }
    }

    public void handleResign(Integer gameID) throws Exception {
        Gson gson = new Gson();
        try{
            GameData gameData = service.findGameData(databaseHolder, gameID);
            ChessGame game = gameData.game();
            ChessGame.TeamColor losingColor = game.getTeamTurn(); //the resigning team loses//

            String losingUser = gameData.blackUsername();
            String winningUser = gameData.whiteUsername();
            if(losingColor == ChessGame.TeamColor.WHITE){
                losingUser = gameData.whiteUsername();
                winningUser = gameData.blackUsername();
            }

            String observerMessageString = losingUser + " has resigned, " + winningUser + " wins!";
            NotificationMessage observerMessage = new NotificationMessage(observerMessageString);
            String notificationJson = gson.toJson(observerMessage);

            //observer message going to everyone for now// todo: customize messages for winner/loser/observer//
            Set<Session> peopleInvolved = sessionMap.get(gameID);
            for(Session s : peopleInvolved){
                //acting session doesn't get the notification of their own move//
                s.getRemote().sendString(notificationJson);
            }

        }
        catch(Exception ex){
            this.sendError(ex);
            //todo: do we need to throw these still?//
            throw new Exception(ex.getMessage());
        }

    }

    public void handleLeave() throws Exception {
        Gson gson = new Gson();
        //todo: this//
        try{
            //just goes to the acting user//
            String exitGameMessage = "exiting game!";
            NotificationMessage observerMessage = new NotificationMessage(exitGameMessage);
            String notificationJson = gson.toJson(observerMessage);

            actingSession.getRemote().sendString(notificationJson);
        }
        catch (Exception ex){
            this.sendError(ex);
        }

    }


    //help functions//
    private void sendError(Exception ex) throws Exception {
        Gson gson = new Gson();
        try{
            ErrorMessage message = new ErrorMessage(ex.getMessage());
            String errorJson = gson.toJson(message);

            actingSession.getRemote().sendString(errorJson);
        }
        catch(IOException io){
            throw new Exception(io.getMessage());
        }
    }
}
