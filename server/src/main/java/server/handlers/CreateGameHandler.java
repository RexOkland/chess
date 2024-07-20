package server.handlers;

import com.google.gson.Gson;
import dataaccess.DatabaseHolder;
import models.GameData;
import models.UserData;
import responses.CreateGameResponse;
import responses.ListGamesResponse;
import server.services.CreateGameService;
import server.services.ListGamesService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {
    CreateGameService service;
    DatabaseHolder holder;

    public CreateGameHandler(DatabaseHolder databaseHolder){
        this.service = new CreateGameService();
        this.holder = databaseHolder;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(request == null){
            throw new Exception("empty request!");
        }
        else {
            Gson gson = new Gson();
            GameData givenGameData = gson.fromJson(request.body(), GameData.class);
            String givenAuthData = request.headers("authorization");
            CreateGameResponse gamesResponse = service.createGame(givenAuthData, givenGameData.gameName(), holder);
            if (gamesResponse.message() == null) {
                response.status(200); //sets status to 200//
            } else if (gamesResponse.message().equals("error: bad request")) {
                response.status(400);
            } else if (gamesResponse.message().equals("error: unauthorized")) {
                response.status(401);
            } else {
                response.status(500);
            }
            return gson.toJson(gamesResponse);
        }
    }
}
