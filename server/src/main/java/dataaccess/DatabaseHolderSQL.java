package dataaccess;

import dataaccess.authdao.AuthDaoInterface;
import dataaccess.authdao.AuthDaoSQL;
import dataaccess.gamesdao.GamesDaoInterface;
import dataaccess.gamesdao.GamesDaoSQL;
import dataaccess.userdao.UserDaoInterface;
import dataaccess.userdao.UserDaoSQL;

public class DatabaseHolderSQL implements DatabaseAccess{
    @Override
    public AuthDaoSQL authDAO() {
        //TODO: implement
        return null;
    }

    @Override
    public GamesDaoSQL gamesDAO() {
        //TODO: implement
        return null;
    }

    @Override
    public UserDaoSQL userDAO() {
        //TODO: implement
        return null;
    }
}
