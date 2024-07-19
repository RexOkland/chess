package server.services;

import dataaccess.authdao.*;
import dataaccess.userdao.*;
import models.AuthData;
import models.UserData;

import java.util.Collection;

public class RegisterService {
    //business logic function I think//

    private void createUser(UserData userData, UserDao dao){
        dao.addItem(userData);
    };

    private void createAuth(AuthData authData, AuthDao dao){
        dao.addItem(authData);
    }

    private UserData getUser(String userName, UserDao dao){ //for looking to see if User exists
        for(UserData u : dao.returnItems()){
            if(u.getUserName() == userName){
                //user with this username exists//
                return u;
            }
        }
        //none of the users in the dao have this name//
        return null;
    };


}
