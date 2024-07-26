package dataaccess.userdao;

import dataaccess.DataAccessException;
import models.UserData;

import java.util.Collection;
import java.util.HashSet;

public interface UserDaoInterface {
    public void addItem(UserData item) throws DataAccessException;
    public UserData searchUser(String user) throws DataAccessException;
    public void clearDAO() throws DataAccessException;
}
