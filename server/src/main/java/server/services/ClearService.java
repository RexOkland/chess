package server.services;

import dataaccess.DataAccessException;
import dataaccess.DatabaseAccess;
import dataaccess.DatabaseHolder;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.gamesdao.GamesDaoInterface;
import dataaccess.userdao.UserDaoInterface;
import responses.ClearResponse;

public class ClearService {
    public ClearResponse clear(DatabaseAccess db){
        //interface objects allow for local and sql solutions to work//
        AuthDaoInterface authDao = db.authDAO();
        GamesDaoInterface gamesDao = db.gamesDAO();
        UserDaoInterface userDao = db.userDAO();
        try {
            authDao.clearDAO();
            gamesDao.clearDAO();
            userDao.clearDAO();
        }
        catch(DataAccessException ex){ //500 type error//
            return new ClearResponse(ex.getMessage());
        }
        return new ClearResponse(null);
    }
}
