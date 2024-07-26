package dataaccess;

import dataaccess.authdao.AuthDao;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.authdao.AuthDaoSQL;
import dataaccess.gamesdao.GamesDao;
import dataaccess.gamesdao.GamesDaoInterface;
import dataaccess.gamesdao.GamesDaoSQL;
import dataaccess.userdao.UserDao;
import dataaccess.userdao.UserDaoInterface;
import dataaccess.userdao.UserDaoSQL;

public class DatabaseHolderSQL implements DatabaseAccess{
    AuthDaoSQL authDaoSQL;
    GamesDaoSQL gamesDaoSQL;
    UserDaoSQL userDaoSQL;

    public DatabaseHolderSQL(){ //initialize the database//
        this.authDaoSQL = new AuthDaoSQL();
        this.gamesDaoSQL = new GamesDaoSQL();
        this.userDaoSQL = new UserDaoSQL();
    }
    public AuthDaoSQL authDAO() {
        return this.authDaoSQL;
    }

    public GamesDaoSQL gamesDAO() {
        return this.gamesDaoSQL;
    }

    public UserDaoSQL userDAO() {
        return this.userDaoSQL;
    }
}
