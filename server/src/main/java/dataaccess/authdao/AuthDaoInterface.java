package dataaccess.authdao;

import models.AuthData;

public interface AuthDaoInterface {
    public void addItem(AuthData item);
    public void removeItem(AuthData item);
    public AuthData findAuth(String token);
    public void clearDAO();
}
