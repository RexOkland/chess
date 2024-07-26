package dataaccess;

import dataaccess.authdao.*;
import dataaccess.gamesdao.*;
import dataaccess.userdao.*;

public class DatabaseHolder implements DatabaseAccess{
    //we've got an object here to access each of the db tables//
    AuthDao authDAO;
    GamesDao gamesDAO;
    UserDao userDAO;

    public DatabaseHolder(){ //initialize the database//
        this.authDAO = new AuthDao();
        this.gamesDAO = new GamesDao();
        this.userDAO = new UserDao();
    }
    public AuthDao authDAO(){
        return this.authDAO;
    }
    public GamesDao gamesDAO(){
        return this.gamesDAO;
    }
    public UserDao userDAO(){
        return this.userDAO;
    }
}
