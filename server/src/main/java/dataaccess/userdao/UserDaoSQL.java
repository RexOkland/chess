package dataaccess.userdao;

import dataaccess.DataAccessException;
import models.UserData;
import org.eclipse.jetty.server.Authentication;

public class UserDaoSQL implements UserDaoInterface {
    @Override
    public void addItem(UserData item) throws DataAccessException {
        //TODO: implement//
    }

    @Override
    public UserData searchUser(String user) throws DataAccessException {
        //TODO: implement//
        return null;
    }

    @Override
    public void clearDAO() throws DataAccessException {
        //TODO: implement//
    }
}
