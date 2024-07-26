package server.handlers;

import com.google.gson.Gson;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import models.*;
import responses.LoginResponse;
import server.services.*;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    LoginService service;
    DatabaseAccess holder;



    public LoginHandler(DatabaseAccess databaseHolder){
        this.service = new LoginService();
        this.holder = databaseHolder;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        if(request == null){
            throw new Exception("empty request!");
        }
        else{
            Gson gson = new Gson();
            UserData givenData = gson.fromJson(request.body(), UserData.class);
            LoginResponse loginResponse = service.login(givenData, holder);
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
