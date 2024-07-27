package dataaccess;

import chess.ChessGame;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.authdao.AuthDaoSQL;
import dataaccess.gamesdao.GamesDao;
import dataaccess.gamesdao.GamesDaoInterface;
import dataaccess.gamesdao.GamesDaoSQL;
import dataaccess.userdao.UserDao;
import dataaccess.userdao.UserDaoInterface;
import dataaccess.userdao.UserDaoSQL;
import models.AuthData;
import models.GameData;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class CustomDaoTests {

    @Test
    @DisplayName("AuthDao - good add item test")
    public void authDaoGoodAddItem(){
        String noException = null;
        AuthData goodData = new AuthData("totallyValidToken", "totallyValidUsername");
        AuthDaoInterface testAuthDao = new AuthDao();
        AuthDaoInterface testAuthDaoSQL = new AuthDaoSQL();
        try{ //testing both versions of the addItem//
            testAuthDao.addItem(goodData);
            testAuthDaoSQL.addItem(goodData);
        }catch (DataAccessException ex){
            noException = ex.getMessage();
        }
        // no sort of error should be thrown with good data //
        Assertions.assertNull(noException);
        cleanUpShop();
    }

    @Test
    @DisplayName("AuthDao - bad add item test")
    public void authDaoBadAddItem(){
        String exception = null;
        AuthData badData = new AuthData("totallyValidToken", "string that's way too long..." +
                ".................................................................................................." +
                ".................................................................................................." +
                ".................................................................................................." +
                ".................................................................................................." +
                ".................................................................................................." +
                ".................................................................................................." +
                ".................................................................................................." +
                ".......................................................................like WAY way too long......");
        AuthDaoInterface testAuthDao = new AuthDao();
        AuthDaoInterface testAuthDaoSQL = new AuthDaoSQL();
        try{ //testing both versions of the addItem//
            testAuthDao.addItem(badData);
            testAuthDaoSQL.addItem(badData);
        }catch (DataAccessException ex){
            exception = ex.getMessage();
        }
        // some sort of error should be thrown with this data //
        Assertions.assertNotNull(exception);
        cleanUpShop();
    }

    @Test
    @DisplayName("AuthDao - good remove item test")
    public void authDaoGoodRemove(){
        //adding and auth token, removing it, then seeing that the data is not found//
        setStage();
        String noException = null;
        AuthData foundData = null;
        AuthData foundDataSQL = null;

        AuthDaoInterface authDao = new AuthDao();
        AuthDaoInterface authDaoSQL = new AuthDaoSQL();
        AuthData existingAuth = new AuthData("existingToken", "existingUser");
        try{
            authDao.removeItem(existingAuth);
            authDaoSQL.removeItem(existingAuth);

            foundData = authDao.findAuth(existingAuth.authToken());
            foundDataSQL = authDaoSQL.findAuth(existingAuth.authToken());
        } catch (DataAccessException ex){
            noException = ex.getMessage();
        }

        Assertions.assertNull(noException);
        Assertions.assertNull(foundData);
        Assertions.assertNull(foundDataSQL);
        cleanUpShop();
    }

    @Test
    @DisplayName("AuthDao - bad remove item test")
    public void authDaoBadRemove(){
        //test is trying to remove using an invalid String//
        String exception = null;
        AuthData nonexistentData = new AuthData("\"wildData", "fakeUsername"); // throwing in " //
        AuthDaoInterface testAuthDao = new AuthDao();
        AuthDaoInterface testAuthDaoSQL = new AuthDaoSQL();
        try{
            testAuthDao.removeItem(nonexistentData);
            testAuthDaoSQL.removeItem(nonexistentData);
        }catch(DataAccessException ex){
            exception = "ladies and gentlemen... we've got him: " + ex.getMessage();
        }
        Assertions.assertNotNull(exception);
        cleanUpShop();
    }

    @Test
    @DisplayName("AuthDao - good find item test")
    public void authDaoGoodFind(){
        /*only testing the SQL version of the functions here... we would have to initialize
        the whole local thing in each function y que flojera la neta*/
        setStage();
        String noException = null;
        AuthData expectedData = existingAuth();
        AuthDaoInterface testAuthDaoSQL = new AuthDaoSQL();

        AuthData foundDataSQL = null;
        try{
            foundDataSQL = testAuthDaoSQL.findAuth(expectedData.authToken());
        }catch (DataAccessException ex){
            noException = ex.getMessage();
        }

        Assertions.assertNull(noException);
        Assertions.assertNotNull(foundDataSQL);
        Assertions.assertEquals(expectedData.username(), foundDataSQL.username());
        cleanUpShop();
    }

    @Test
    @DisplayName("AuthDao - bad find item test")
    public void authDaoBadFind(){
        /*only testing the SQL version of the functions here... we would have to initialize
        the whole local thing in each function y que flojera la neta*/
        setStage();
        String stillNoException = null;
        AuthData badToken = new AuthData("nonValidToken", "whoDis?");
        AuthDaoInterface testAuthDaoSQL = new AuthDaoSQL();

        AuthData foundDataSQL = null;
        try{
            foundDataSQL = testAuthDaoSQL.findAuth(badToken.authToken());
        }catch (DataAccessException ex){
            stillNoException = ex.getMessage();
        }

        Assertions.assertNull(stillNoException);
        Assertions.assertNull(foundDataSQL);
        cleanUpShop();
    }


    //Rex's helper Functions//
    private void cleanUpShop(){
        try{
            //initialize all daos//
            AuthDaoInterface testAuthDao = new AuthDao();
            AuthDaoInterface testAuthDaoSQL = new AuthDaoSQL();
            GamesDaoInterface testGameDao = new GamesDao();
            GamesDaoInterface testGameDaoSQL = new GamesDaoSQL();
            UserDaoInterface testUserDao = new UserDao();
            UserDaoInterface testUserDaoSQL = new UserDaoSQL();
            //clear all daos//
            testAuthDao.clearDAO();
            testAuthDaoSQL.clearDAO();
            testGameDao.clearDAO();
            testGameDaoSQL.clearDAO();
            testUserDao.clearDAO();
            testUserDaoSQL.clearDAO();
            //if there's errors here, we're in troubskis//
        }catch(DataAccessException ex){
            System.out.println("something aint right - cleanUp Function");
        }
    }


    private void setStage(){
        try{
            //initialize all daos//
            AuthDaoInterface authDaoSQL = new AuthDaoSQL();
            GamesDaoInterface gameDaoSQL = new GamesDaoSQL();
            UserDaoInterface userDaoSQL = new UserDaoSQL();
            //create filler data//
            AuthData existingAuth = new AuthData("existingToken", "existingUser");
            GameData existingGame = new GameData(0,null,null,"defaultGame", new ChessGame());
            UserData existingUser = new UserData("existingUser", "existingPassword", "existingEmail");
            //add in filed data//
            authDaoSQL.addItem(existingAuth);
            gameDaoSQL.addGame(existingGame);
            userDaoSQL.addItem(existingUser);
        } catch(DataAccessException ex){
            System.out.println("something aint right - setStage Function");
        }
    }

    private static AuthData existingAuth(){return new AuthData("existingToken", "existingUser");}
    private static GameData existingGame(){return new GameData(0,null,null,"defaultGame", new ChessGame());}
    private static UserData existingUser(){return new UserData("existingUser", "existingPassword", "existingEmail");}
}
