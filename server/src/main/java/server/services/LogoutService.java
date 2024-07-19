package server.services;

import dataaccess.DatabaseHolder;
import dataaccess.authdao.AuthDao;
import dataaccess.userdao.UserDao;
import models.AuthData;
import models.UserData;
import responses.LoginResponse;
import responses.LogoutResponse;

import java.util.Objects;
import java.util.UUID;

public class LogoutService {

    public LogoutResponse logout(String authString, DatabaseHolder db){

        //response data//
        String responseMessage = null;

        AuthDao authDao = db.AuthDAO();
        AuthData foundData = authDao.findAuth(authString);
        if(foundData == null){
            responseMessage = "error: unauthorized";
            return new LogoutResponse(responseMessage);
        }
        else{
            authDao.removeItem(foundData);
            return new LogoutResponse(responseMessage);
        }
    }

}
