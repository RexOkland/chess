package server.handlers;

import com.google.gson.Gson;
import dataaccess.DatabaseHolder;
import models.UserData;
import responses.RegisterResponse;
import spark.Request;
import spark.Response;
import server.services.*;
import spark.Route;

public class RegisterHandler implements Route {
    RegisterService service;
    DatabaseHolder holder;



    public RegisterHandler(DatabaseHolder databaseHolder){
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
            //if(){}
            response.status(200); //sets status to 200//

            return gson.toJson(registerResponse);
        }
        //TODO: code//
        //return null;
    }


}
