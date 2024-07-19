package dataaccess;

import dataaccess.authdao.*;
import dataaccess.gamesdao.*;
import dataaccess.userdao.*;

public class DatabaseManager {
    //we've got an object here to access each of the db tables//
    AuthDao AuthDAO;
    GamesDao GamesDAO;
    UserDao UserDAO;

    public AuthDao AuthDAO(){
        return this.AuthDAO;
    }

    public GamesDao GamesDAO(){
        return this.GamesDAO;
    }

    public UserDao UserDAO(){
        return this.UserDAO;
    }


}
