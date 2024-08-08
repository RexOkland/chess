package client.websocket;

import chess.ChessGame;
import client.ChessClient;
import com.google.gson.Gson;
import models.GameData;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.messages.UpdateBoardMessage;

import javax.websocket.*;
import java.net.URI;
import java.net.http.WebSocket;
import java.util.Objects;

public class WebSocketFacade extends Endpoint {
    private final String serverUrl;
    private URI uri;
    private WebSocketContainer socketContainer;
    private Session session;
    private String sessionAuth = null; //null until someone logs in//
    private String sessionUser = null;


    //pretty much the same constructors as the serverFacade//
    public WebSocketFacade(String url){
        serverUrl = url;
    }
    public WebSocketFacade(int port){
        //bring in data//
        serverUrl = "ws://localhost:" + port + "/ws"; //add the webSock on the end//
        try{
            uri = new URI(serverUrl);
            socketContainer = ContainerProvider.getWebSocketContainer();
            session = socketContainer.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String json) {
                    Gson gson = new Gson();
                    ServerMessage initialMessageRead = gson.fromJson(json, ServerMessage.class);

                    ServerMessage.ServerMessageType type = initialMessageRead.getServerMessageType();
                    switch (type){
                        case ERROR -> this.receivedError(json);
                        case NOTIFICATION -> this.receivedNotification(json);
                        case LOAD_GAME -> this.receivedLoadGame(json);
                    }
                }
                //this is the individual client getting a message//
                private void receivedError(String json) {
                    Gson gson = new Gson();
                    ErrorMessage received = gson.fromJson(json, ErrorMessage.class);
                    System.out.println(received.getMessage());
                }
                private void receivedNotification(String json){
                    Gson gson = new Gson();
                    NotificationMessage received = gson.fromJson(json, NotificationMessage.class);
                    System.out.println(received.getMessage());
                }
                private void receivedLoadGame(String json){
                    Gson gson = new Gson();
                    UpdateBoardMessage received = gson.fromJson(json, UpdateBoardMessage.class);
                    //game instead of a message//
                    GameData receivedGameData = received.giveGameData();
                    ChessGame receivedGame = receivedGameData.game();

                    if(Objects.equals(sessionUser, receivedGameData.blackUsername())){
                        receivedGame.getBoard().printBlackPerspective();
                    }
                    else{
                        receivedGame.getBoard().printWhitePerspective();
                        //for white team and observers//
                    }

                }
            });
        }
        catch (Exception ex){
            //todo: something//
        }

    };

    public void onOpen(Session session, EndpointConfig endpointConfig) {};


    public void sendConnectCommand(UserGameCommand command){
        try{
            Gson gson = new Gson();
            String jsonToSend = gson.toJson(command);
            session.getBasicRemote().sendText(jsonToSend);
        }
        catch (Exception ex){
            //todo: something here//
        }

        //todo: implement//
    }
    public void sendResignCommand(){
        //todo: implement//
    }
    public void sendLeaveCommand(){
        //todo: implement//
    }
    public void sendMakeMoveCommand(){
        //todo: implement//
    }

    //todo: figure out how to receive ServerResponse messages//

    public void setUserInfo(String auth, String user){ //used in the Chess Client to pass my data//
        this.sessionAuth = auth;
        this.sessionUser = user;
    }

}
