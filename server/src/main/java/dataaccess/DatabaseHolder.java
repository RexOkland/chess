package dataaccess;

import dataaccess.authdao.*;
import dataaccess.gamesdao.*;
import dataaccess.userdao.*;

public class DatabaseHolder {
    //we've got an object here to access each of the db tables//
    AuthDao AuthDAO;
    GamesDao GamesDAO;
    UserDao UserDAO;

    public DatabaseHolder(){ //initialize the database//
        this.AuthDAO = new AuthDao();
        this.GamesDAO = new GamesDao();
        this.UserDAO = new UserDao();
    }

    public AuthDao authDAO(){
        return this.AuthDAO;
    }

    public GamesDao gamesDAO(){
        return this.GamesDAO;
    }

    public UserDao userDAO(){
        return this.UserDAO;
    }


}
