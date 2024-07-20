package server.handlers;

import com.google.gson.Gson;
import dataaccess.DatabaseHolder;
import models.UserData;
import responses.ListGamesResponse;
import responses.LoginResponse;
import server.services.ListGamesService;
import server.services.LoginService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGamesHandler implements Route {
    ListGamesService service;
    DatabaseHolder holder;

    public ListGamesHandler(DatabaseHolder databaseHolder){
        this.service = new ListGamesService();
        this.holder = databaseHolder;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(request == null){
            throw new Exception("empty request!");
        }
        else {
            Gson gson = new Gson();
            String givenData = request.headers("authorization");
            ListGamesResponse gamesResponse = service.getGames(givenData, holder);
            if (gamesResponse.message() == null) {
                response.status(200); //sets status to 200//
            } else if (gamesResponse.message().equals("error: unauthorized")) {
                response.status(401);
            } else {
                response.status(500);
            }
            String returnJson = gson.toJson(gamesResponse);
            return returnJson;
        }
    }
}
