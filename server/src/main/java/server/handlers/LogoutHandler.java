package server.handlers;

import com.google.gson.Gson;
import dataaccess.DatabaseHolder;
import models.AuthData;
import models.UserData;
import responses.LoginResponse;
import responses.LogoutResponse;
import server.services.LoginService;
import server.services.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.Reader;

public class LogoutHandler implements Route {
    LogoutService service;
    DatabaseHolder holder;



    public LogoutHandler(DatabaseHolder databaseHolder){
        this.service = new LogoutService();
        this.holder = databaseHolder;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(request == null){
            throw new Exception("empty request!");
        }
        else{
            Gson gson = new Gson();
            AuthData givenData = gson.fromJson(request.headers("authorization"),AuthData.class);
            LogoutResponse loginResponse = service.logout(givenData, holder);
            if(loginResponse.message() == null){
                response.status(200); //sets status to 200//
            }
            else if(loginResponse.message().equals("error: unauthorized")){
                response.status(401);
            }
            else{
                response.status(500);
            }


            return gson.toJson(loginResponse);
        }
    }
}
