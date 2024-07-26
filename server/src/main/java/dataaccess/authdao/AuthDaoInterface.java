package dataaccess.authdao;

import dataaccess.DataAccessException;
import models.AuthData;

public interface AuthDaoInterface {
    public void addItem(AuthData item) throws DataAccessException;
    public void removeItem(AuthData item) throws DataAccessException;
    public AuthData findAuth(String token) throws DataAccessException;
    public void clearDAO() throws DataAccessException;
}
