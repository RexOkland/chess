package server.handlers;

import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@WebSocket
public class WebSocketHandler  {
    Map<Integer, Set<Session>> sessionMap;
    DatabaseAccess databaseHolder;


    public WebSocketHandler(DatabaseAccess databaseHolder){
        this.databaseHolder = databaseHolder;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String input){

    };

    public void handleCommand(UserGameCommand cmd){
        switch (cmd.getCommandType()){
            case MAKE_MOVE -> this.handleMakeMove();
            case CONNECT -> this.handleConnect();
            case RESIGN -> this.handleResign();
            case LEAVE -> this.handleLeave();
        }
    }

    public void handleMakeMove(){
        //todo:this//
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
