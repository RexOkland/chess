package server.handlers;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.mysql.cj.conf.ConnectionUrlParser;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.services.WebService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@WebSocket
public class WebSocketHandler  {
    Map<Integer, Set<Session>> sessionMap;
    DatabaseAccess databaseHolder;
    WebService service;


    public WebSocketHandler(DatabaseAccess databaseHolder){
        this.databaseHolder = databaseHolder;
        this.service = new WebService();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String input){

        Gson gson = new Gson();
        UserGameCommand command = gson.fromJson(input, UserGameCommand.class);
        MakeMoveCommand moveCommand = null;
        if(command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
            moveCommand = gson.fromJson(input, MakeMoveCommand.class);
        }


        /*WHEN A MESSAGE IS RECEIVED?
        guessing the input string will have the command / move info */



    };

    public void handleCommand(UserGameCommand cmd, ChessMove move) throws Exception{
        try {
            switch (cmd.getCommandType()) {
                case MAKE_MOVE -> this.handleMakeMove(cmd.getGameID(), null);
                case CONNECT -> this.handleConnect();
                case RESIGN -> this.handleResign();
                case LEAVE -> this.handleLeave();
            }
        }
        catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
    }

    public void handleMakeMove(Integer gameID, ChessMove move) throws Exception {
        //todo:this//

        try{
            ChessGame game = service.findGameData(databaseHolder, gameID);
            game.makeMove(move); //logic is all built out//
            //suponiendo que no hubo una exception//

            Set<Session> peopleInvolved = sessionMap.get(gameID);
            for(Session s : peopleInvolved){
                Gson gson = new Gson();

                /*send message notifying observers/opponent of the move that was made*/
                String start = move.getStartPosition().print();
                String end = move.getEndPosition().print();
                String notificationString = String.format("move made: %s -> %s", start, end);

                NotificationMessage notificationObject = new NotificationMessage(notificationString);
                String json = gson.toJson(notificationObject);
                s.getRemote().sendString(json);
            }
        }
        catch (Exception ex) {
            throw new Exception(ex.getMessage()); //passing along any exception we get//
        }

    }

    public void handleConnect(){
        //todo: this//
    }

    public void handleResign(){
        //todo: this//
    }

    public void handleLeave(){
        //todo: this//
    }
}
