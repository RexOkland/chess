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

    public LogoutResponse logout(AuthData authData, DatabaseHolder db){

        //response data//
        String responseMessage = null;

        AuthDao authDao = db.AuthDAO();
        for(AuthData a : authDao.returnItems()){
            //enters this 'if' statement if we find a matching authentication token//
            if(Objects.equals(authData.authToken(), a.authToken())){
                authDao.removeItem(a);
                return new LogoutResponse(responseMessage);
            }
        }
        //given token was not found... attempting to log out someone who's not logged in//
        responseMessage = "error: unauthorized";
        return new LogoutResponse(responseMessage);
    }

}
