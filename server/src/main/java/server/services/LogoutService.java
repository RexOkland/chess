package server.services;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.userdao.UserDao;
import models.AuthData;
import models.UserData;
import responses.LoginResponse;
import responses.LogoutResponse;

import java.util.Objects;
import java.util.UUID;

public class LogoutService {

    public LogoutResponse logout(String authString, DatabaseAccess db){

        //response data//
        String responseMessage = null;

        AuthDaoInterface authDao = db.authDAO();
        AuthData foundData;
        try{foundData= authDao.findAuth(authString);}
        catch (DataAccessException ex){ //500 type error//
            responseMessage = "data access error: " + ex.getMessage();
            return new LogoutResponse(responseMessage);
        }

        if(foundData == null){
            responseMessage = "error: unauthorized";
            return new LogoutResponse(responseMessage);
        }
        else{
            try{authDao.removeItem(foundData);}
            catch (DataAccessException ex){ //500 type error//
                responseMessage = "data access error: " + ex.getMessage();
                return new LogoutResponse(responseMessage);
            }
            return new LogoutResponse(responseMessage);
        }
    }

}
