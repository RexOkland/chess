package server.services;

import dataaccess.DatabaseHolder;
import dataaccess.authdao.*;
import dataaccess.userdao.*;
import models.AuthData;
import models.UserData;
import org.eclipse.jetty.server.Authentication;
import responses.RegisterResponse;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class RegisterService {
    //business logic function I think//

    public RegisterResponse register(UserData userData, DatabaseHolder db){

        //response data//
        String responseUser = null;
        String responseAuth = null;
        String responseMessage = null;

        UserDao userDao = db.UserDAO();
        if( (Objects.equals(userData.username(), "")) || (Objects.equals(userData.password(), ""))){
            //neither can be left empty... that's a bad request to me//
            responseMessage = "error: bad request";
            return new RegisterResponse(responseUser, responseAuth, responseMessage);
        }
        if( (userData.username() == null) || (userData.password() == null) ){
            //neither can be left empty... that's a bad request to me//
            responseMessage = "error: bad request";
        }

        //userData is NOT blank or null//
        else{

            UserData foundData = userDao.searchUser(userData.username());
            if(foundData == null){
                //username not found in system//
                //all logic tests passed @ this point//
                responseUser = userData.username(); //RESPONSE DATA//
                responseAuth = UUID.randomUUID().toString(); //RESPONSE DATA//

                AuthDao authDao = db.AuthDAO();
                authDao.addItem( new AuthData(responseAuth, responseUser) );
                //auth data added//
                userDao.addItem(userData);
                //user data added/
            }
            else{
                responseMessage = "error: username taken";
            }

        }
        return new RegisterResponse(responseUser, responseAuth, responseMessage);

    }

}
