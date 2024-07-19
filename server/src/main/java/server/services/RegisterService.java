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
        else{
            if(!userDao.returnItems().isEmpty()){
                //we're not checking this if there aren't any users yet//
                for(UserData u : userDao.returnItems()){
                    if(Objects.equals(u.username(), userData.username())){ //RESPONSE DATA//

                        responseMessage = "error: username taken";
                        return new RegisterResponse(responseUser, responseAuth, responseMessage);
                    }
                }
            }
        }

        //all logic tests passed @ this point//
        responseUser = userData.username(); //RESPONSE DATA//
        responseAuth = UUID.randomUUID().toString(); //RESPONSE DATA//


        AuthDao authDao = db.AuthDAO();
        authDao.addItem( new AuthData(responseAuth, responseUser) );
        //auth data added//
        userDao.addItem(userData);
        //user data added/

        return new RegisterResponse(responseUser, responseAuth, responseMessage);
    }

}
