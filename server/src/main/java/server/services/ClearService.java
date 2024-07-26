package server.services;

import dataaccess.DataAccessException;
import dataaccess.DatabaseHolder;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.gamesdao.GamesDaoInterface;
import dataaccess.userdao.UserDaoInterface;
import responses.ClearResponse;

public class ClearService {
    String responseMessage = null;
    public ClearResponse clear(DatabaseHolder db){
        //interface objects allow for local and sql solutions to work//
        AuthDaoInterface authDao = db.authDAO();
        GamesDaoInterface gamesDao = db.gamesDAO();
        UserDaoInterface userDao = db.userDAO();
        try {
            authDao.clearDAO();
            gamesDao.clearDAO();
            userDao.clearDAO();
        }
        catch(DataAccessException ex){
            return new ClearResponse(ex.getMessage());
        }
        return new ClearResponse(null);
    }
}
