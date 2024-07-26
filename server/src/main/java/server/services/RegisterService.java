package server.services;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import dataaccess.authdao.*;
import dataaccess.userdao.*;
import models.AuthData;
import models.UserData;
import org.eclipse.jetty.server.Authentication;
import responses.RegisterResponse;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class RegisterService {
    //business logic function I think//

    public RegisterResponse register(UserData userData, DatabaseAccess db){

        //response data//
        String responseUser = null;
        String responseAuth = null;
        String responseMessage = null;

        UserDaoInterface userDao = db.userDAO();
        if( (Objects.equals(userData.username(), "")) || (Objects.equals(userData.password(), ""))){
            //neither can be left empty... that's a bad request to me//
            responseMessage = "error: bad request";
            return new RegisterResponse(responseUser, responseAuth, responseMessage);
        }
        if( (userData.username() == null) || (userData.password() == null) ){
            //neither can be left empty... that's a bad request to me//
            responseMessage = "error: bad request";
        }
        //userData is NOT blank or null... which is good//
        else{
            UserData foundData; //declaring this variable before try-catch blocs=ks//

            //searching user table to see if the username is taken already//
            try {
                foundData = userDao.searchUser(userData.username());
            }catch (DataAccessException ex){
                responseMessage = "data access error: " + ex.getMessage();
                return new RegisterResponse(responseUser, responseAuth, responseMessage);
            }

            if(foundData == null){
                //username not found in system... which is good//
                //all logic tests passed @ this point//

                //ASSIGNING RESPONSE DATA//
                responseUser = userData.username();
                responseAuth = UUID.randomUUID().toString();

                //adding new auth token to authentication table//
                AuthDaoInterface authDao = db.authDAO();
                try{authDao.addItem( new AuthData(responseAuth, responseUser) );}
                catch(DataAccessException ex){ //500 data access error//
                    responseMessage = "data access error: " + ex.getMessage();
                    return new RegisterResponse(responseUser, responseAuth, responseMessage);
                }

                //adding new user to user table//
                try{userDao.addItem(userData);}
                catch(DataAccessException ex){ //500 data access error//
                    responseMessage = "data access error: " + ex.getMessage();
                    return new RegisterResponse(responseUser, responseAuth, responseMessage);
                }
            }
            else{
                responseMessage = "error: username taken";
            }

        }
        return new RegisterResponse(responseUser, responseAuth, responseMessage);

    }

}
