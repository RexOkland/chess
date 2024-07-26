package dataaccess.userdao;

import dataaccess.DataAccessException;
import models.UserData;

import java.util.Collection;
import java.util.HashSet;

public interface UserDaoInterface {
    public void addItem(UserData item) throws DataAccessException;
    public UserData searchUser(String user) throws DataAccessException;
    public void clearDAO() throws DataAccessException;

    /* because of the exceptions, we basically need to run all DAO functions within a try-catch statement
    this makes sure that things are going okay when we try to access the database... I think these will
    be more relevant once we are working with SQL command calls - these are error 500 things */
}
