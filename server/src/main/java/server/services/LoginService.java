package server.services;

import dataaccess.DatabaseHolder;
import dataaccess.userdao.UserDao;
import models.AuthData;
import models.UserData;
import responses.LoginResponse;
import responses.RegisterResponse;

import javax.xml.crypto.Data;
import java.util.Objects;
import java.util.UUID;

public class LoginService {

    public LoginResponse login(UserData userData, DatabaseHolder db){

        //response data//
        String responseUser = null;
        String responseAuth = null;
        String responseMessage = null;

        UserDao userDao = db.userDAO();
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

            UserData foundData = userDao.searchUser(userData.username());
            if((foundData == null) || (!Objects.equals(foundData.password(), userData.password()))){
                responseMessage = "error: unauthorized";
                return new LoginResponse(responseUser, responseAuth, responseMessage);
            }
            else{ //they DO match up//
                responseAuth = UUID.randomUUID().toString();
                responseUser = foundData.username();
                db.authDAO().addItem( new AuthData(responseAuth, responseUser) );
            }
            return new LoginResponse(responseUser, responseAuth, responseMessage);
        }
    }
}
