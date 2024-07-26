package server.handlers;

import com.google.gson.Gson;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import models.GameData;
import requests.JoinGameRequest;
import responses.CreateGameResponse;
import responses.JoinGameResponse;
import server.services.JoinGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {
    JoinGameService service;
    DatabaseAccess holder;

    public JoinGameHandler(DatabaseAccess databaseHolder){
        this.service = new JoinGameService();
        this.holder = databaseHolder;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(request == null){
            throw new Exception("empty request!");
        }
        else {
            Gson gson = new Gson();
            JoinGameRequest givenGameData = gson.fromJson(request.body(), JoinGameRequest.class);
            String chosenColor = givenGameData.playerColor();
            Integer gameID = givenGameData.gameID();
            String givenAuthData = request.headers("authorization");
            JoinGameResponse joinResponse = service.joinGame(givenAuthData, chosenColor, gameID, holder);
            if (joinResponse.message() == null) {
                response.status(200); //sets status to 200//
            } else if (joinResponse.message().equals("error: bad request")) {
                response.status(400);
            } else if (joinResponse.message().equals("error: unauthorized")) {
                response.status(401);
            } else if (joinResponse.message().equals("error: already taken")) {
                response.status(403);
            } else {
                response.status(500);
            }
            String returnJson = gson.toJson(joinResponse);
            return returnJson;
        }
    }
}
