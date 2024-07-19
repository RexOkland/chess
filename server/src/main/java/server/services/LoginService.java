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

        UserDao userDao = db.UserDAO();
        if( (Objects.equals(userData.username(), "")) || (Objects.equals(userData.password(), ""))){
            //neither can be left empty... that's a bad request to me//
            responseMessage = "error: unauthorized";
            return new LoginResponse(responseUser, responseAuth, responseMessage);
        }
        else{
            for(UserData u : userDao.returnItems()){
                //enters this 'if' statement if we find the user//
                if(Objects.equals(u.username(), userData.username())){ //RESPONSE DATA//
                    //found the user//
                    if(u.password().equals(userData.password())){
                        //password matches too... they're in//
                        responseAuth = UUID.randomUUID().toString();
                        responseUser = u.username();
                        db.AuthDAO().addItem( new AuthData(responseAuth, responseUser) );
                    }
                    else{
                        //password does NOT match//
                        responseMessage = "error: unauthorized";
                    }
                    return new LoginResponse(responseUser, responseAuth, responseMessage);
                }
            }
            //given username was not found... bad request right?//
            responseMessage = "error: unauthorized";
            return new LoginResponse(responseUser, responseAuth, responseMessage);
        }
    }
}
