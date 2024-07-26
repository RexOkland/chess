package server.services;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import dataaccess.userdao.UserDao;
import dataaccess.userdao.UserDaoInterface;
import models.AuthData;
import models.UserData;
import responses.LoginResponse;
import responses.RegisterResponse;

import javax.xml.crypto.Data;
import java.util.Objects;
import java.util.UUID;

public class LoginService {

    public LoginResponse login(UserData userData, DatabaseAccess db){

        //response data//
        String responseUser = null;
        String responseAuth = null;
        String responseMessage = null;

        UserDaoInterface userDao = db.userDAO();

        if( (Objects.equals(userData.username(), "")) || (Objects.equals(userData.password(), ""))){
            responseMessage = "error: unauthorized";
            return new LoginResponse(responseUser, responseAuth, responseMessage);
        }
        if( (userData.username() == null) || (userData.password() == null)){
            responseMessage = "error: unauthorized";
            return new LoginResponse(responseUser, responseAuth, responseMessage);
        }

        //userData is NOT blank/null//
        else{
            UserData foundData; //declaring a variable before try-catch statements//
            try{foundData = userDao.searchUser(userData.username());}
            catch(DataAccessException exception){ //500-type error//
                responseMessage = "error: failed to connect to server";
                return new LoginResponse(responseUser, responseAuth, responseMessage);
            }

            //at this point, database connection was successful//
            if((foundData == null) || (!Objects.equals(foundData.password(), userData.password()))){
                responseMessage = "error: unauthorized";
                return new LoginResponse(responseUser, responseAuth, responseMessage);
            }
            else{ //they DO match up//
                responseAuth = UUID.randomUUID().toString();
                responseUser = foundData.username();
                try{db.authDAO().addItem( new AuthData(responseAuth, responseUser) );}
                catch(DataAccessException ex){
                    responseMessage = "data access error: " + ex.getMessage();
                    return new LoginResponse(responseUser, responseAuth, responseMessage);
                }
            }
            return new LoginResponse(responseUser, responseAuth, responseMessage);
        }
    }
}
