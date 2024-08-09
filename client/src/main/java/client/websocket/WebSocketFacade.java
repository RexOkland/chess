package client.websocket;

import chess.*;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class WebSocketFacade extends Endpoint {
    private final String serverUrl;
    private URI uri;
    private WebSocketContainer socketContainer;
    private Session session;
    private String sessionAuth = null; //null until someone logs in//
    private String sessionUser = null;
    private ChessBoard lastBoard;
    private ChessGame.TeamColor lastBoardColor;


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
                        lastBoard = receivedGame.getBoard();
                        lastBoardColor = ChessGame.TeamColor.BLACK;
                    }
                    else{
                        receivedGame.getBoard().printWhitePerspective();
                        //for white team and observers//
                        lastBoard = receivedGame.getBoard();
                        lastBoardColor = ChessGame.TeamColor.WHITE;
                    }

                }
            });
        }
        catch (Exception ex){
            //todo: something//
        }

    };

    public void onOpen(Session session, EndpointConfig endpointConfig) {};


    public void sendCommand(UserGameCommand command){
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
    public void sendMakeMoveCommand(){
        //todo: implement//
    }

    public void redrawBoard(){
        if(lastBoardColor == ChessGame.TeamColor.WHITE){
            lastBoard.printWhitePerspective();
        }
        else{
            lastBoard.printBlackPerspective();
        }
    }

    public void highlightBoard(ChessPosition spot){
        ChessPiece specialPiece = lastBoard.getPiece(spot);
        Collection<ChessMove> validSpots = specialPiece.pieceMoves(lastBoard, spot);
        Collection<ChessPosition> spotsToMark = new HashSet<ChessPosition>();
        for(ChessMove m : validSpots){
            spotsToMark.add(m.getEndPosition());
        }
        spotsToMark.add(spot);

        if(this.lastBoardColor == ChessGame.TeamColor.BLACK){lastBoard.printBlackHighlightPerspective(spotsToMark);}
        else{lastBoard.printWhiteHighlightPerspective(spotsToMark);}
    }

    public void setUserInfo(String user, String auth){ //used in the Chess Client to pass my data//
        this.sessionAuth = auth;
        this.sessionUser = user;
    }

}
