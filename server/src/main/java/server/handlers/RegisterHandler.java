package server.handlers;

import com.google.gson.Gson;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import models.UserData;
import responses.RegisterResponse;
import spark.Request;
import spark.Response;
import server.services.*;
import spark.Route;

public class RegisterHandler implements Route {
    RegisterService service;
    DatabaseAccess holder;



    public RegisterHandler(DatabaseAccess databaseHolder){
        this.service = new RegisterService();
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
            RegisterResponse registerResponse = service.register(givenData, holder);
            if(registerResponse.message() == null){
                response.status(200); //sets status to 200//
            }
            else if(registerResponse.message().equals("error: bad request")){
                response.status(400);
            }
            else if(registerResponse.message().equals("error: username taken")){
                response.status(403);
            }
            else{
                response.status(500);
            }

            return gson.toJson(registerResponse);
        }
    }


}
