package dataaccess;

import dataaccess.authdao.AuthDao;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.gamesdao.GamesDao;
import dataaccess.gamesdao.GamesDaoInterface;
import dataaccess.userdao.UserDao;
import dataaccess.userdao.UserDaoInterface;

public interface DatabaseAccess {
    public AuthDaoInterface authDAO();
    public GamesDaoInterface gamesDAO();
    public UserDaoInterface userDAO();
}
