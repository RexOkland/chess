package server.handlers;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
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
import java.util.*;


@WebSocket
public class WebSocketHandler  {
    Map<Integer, Set<Session>> sessionMap;
    Session actingSession;
    String actingSessionUser;
    DatabaseAccess databaseHolder;
    WebService service;


    public WebSocketHandler(DatabaseAccess databaseHolder){
        this.databaseHolder = databaseHolder;
        this.service = new WebService();
    }

    @OnWebSocketMessage
    public void onMessage(Session session,  String input) /*throws Exception*/{
        this.actingSession = session; //whoever sends this message becomes the acting/active session//


        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(input, UserGameCommand.class);

        try{
            //authenticate the Authentication String - throws error on failure//
            service.isValidToken(databaseHolder, command.getAuthToken());
            this.actingSessionUser = service.getUsernameFromAuth(databaseHolder, command.getAuthToken());

            if(command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                MakeMoveCommand moveCommand = gson.fromJson(input, MakeMoveCommand.class);
                this.handleMoveCommand(moveCommand);
            }
            else{
                this.handleCommand(command);
            }

        } catch (Exception ex) {
            this.sendError(ex);
            //todo: currently throwing exceptions that'll be caught here... do we do anything with them?//
        }
    };

    public void handleCommand(UserGameCommand cmd) throws Exception{
        try {
            switch (cmd.getCommandType()) {
                //todo: i think that im supposed to be taking in an auth token somehow...//
                //todo: would have to change onMessage params//
                case CONNECT -> this.handleConnect(cmd.getGameID());
                case RESIGN -> this.handleResign(cmd.getGameID());
                case LEAVE -> this.handleLeave(cmd.getGameID());
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
            //going to find out what team the acting user controls... if they're not an observer//
            GameData gameData = service.findGameData(databaseHolder, gameID);
            ChessGame game = gameData.game();

            //move details
            String start = move.getStartPosition().print();
            String end = move.getEndPosition().print();
            ChessPiece actingPiece = game.getBoard().getPiece(move.getStartPosition());

            //acting piece and acting user need to be the same color, or imma throw hands//
            ChessGame.TeamColor actingColor = actingPiece.teamColor;
            ChessGame.TeamColor userColor = this.getGameRole(this.actingSessionUser, gameData);
            if(actingColor != userColor){ throw new Exception("not authorized to make this move!");}

            //need to prevent this, if the actingSession isn't authorized to make the move//
            game.makeMove(move); //logic is all built out... in theory//
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
            throw new Exception(ex.getMessage()); //passing along any exception we get//
        }
    }

    public void handleConnect(Integer gameID) throws Exception {
        Gson gson = new Gson();
        try{
            //find game... make sure it's real//
            ChessGame game = service.findGameData(databaseHolder, gameID).game();

            //let other users know that this user is joining//
            String joiningUser = "unknown"; //todo: fix this! shouldn't be unknown//
            String notificationString = String.format("%s has joined the game!", joiningUser);
            messageToAllInGame(notificationString, gameID);

            //add the message sender to their game//
            if(sessionMap == null){//totally empty set//
                sessionMap = new HashMap<>();
                sessionMap.put(gameID, new HashSet<Session>());
            }
            else sessionMap.computeIfAbsent(gameID, k -> new HashSet<Session>());//first user to join this game//

            sessionMap.get(gameID).add(actingSession);

            //send them the updated version of the board//
            UpdateBoardMessage updatedBoardObject = new UpdateBoardMessage(game);//will be updated//
            String boardJson = gson.toJson(updatedBoardObject);
            actingSession.getRemote().sendString(boardJson);
        }
        catch (Exception ex){
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

            //let everyone know who resigned//
            String observerMessageString = losingUser + " has resigned, " + winningUser + " wins!";
            messageToAllInGame(observerMessageString, gameID);//todo: potentially customize messages for winner/loser//

        }
        catch(Exception ex){
            throw new Exception(ex.getMessage());
        }

    }

    public void handleLeave(Integer gameID) throws Exception {
        Gson gson = new Gson();
        try{
            //just goes to the acting user//
            String exitGameMessage = "exiting game!";
            NotificationMessage observerMessage = new NotificationMessage(exitGameMessage);
            String notificationJson = gson.toJson(observerMessage);

            actingSession.getRemote().sendString(notificationJson);
            this.sessionMap.get(gameID).remove(actingSession); //removing the guy leaving//

            //let the rest of the game know who left//
            String leavingUser = "unknown";//todo: fix this! shouldn't say unknown//
            String leaveMessage = String.format("%s has left the gane", leavingUser);
            messageToAllInGame(leaveMessage, gameID);
        }
        catch (Exception ex){
            throw new Exception(ex.getMessage());
        }

    }


    //help functions//
    private void sendError(Exception ex) /*throws Exception*/ {
        Gson gson = new Gson();
        try{
            String errorString = String.format("error detected -> %s", ex.getMessage());
            ErrorMessage message = new ErrorMessage(errorString);
            String errorJson = gson.toJson(message);

            actingSession.getRemote().sendString(errorJson);
        }
        catch(IOException io){
            System.out.println("error sending out the error message... tough");
        }
    }

    private void messageToAllInGame(String message, Integer gameID) {
        try{
            if(sessionMap == null){return;}

            Gson gson = new Gson();
            NotificationMessage notificationObject = new NotificationMessage(message);
            String notificationJson = gson.toJson(notificationObject);

            Set<Session> peopleInvolved = sessionMap.get(gameID);
            for(Session s : peopleInvolved){
                //goes to everyone but the acting user, so I'll just send to all before I add the acting to the list//
                s.getRemote().sendString(notificationJson);
            }
        }
        catch (Exception ex){
            //throw new Exception(ex.getMessage());
        }
    }

    private ChessGame.TeamColor getGameRole(String user, GameData game){
        if(Objects.equals(user, game.whiteUsername())){
            return ChessGame.TeamColor.WHITE;
        }
        else if(Objects.equals(user, game.blackUsername())){
            return ChessGame.TeamColor.BLACK;
        }
        else{
            return null; //this b an observer//
        }
    }
}
