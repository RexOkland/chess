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

    //AUTH DAO TESTS//

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

    @Test
    @DisplayName("AuthDao - good clear test")
    public void authDaoGoodClear(){
        AuthDaoInterface testAuthDao = new AuthDaoSQL();
        String noExcepction = null;
        try{
            testAuthDao.clearDAO();
        }catch (DataAccessException ex){
            noExcepction = ex.getMessage();
        }
        Assertions.assertNull(noExcepction);
        cleanUpShop();
    }

    //USER DAO TESTS//

    @Test
    @DisplayName("UserDao - good add item test")
    public void userDaoGoodAddItem(){
        //adding good data, there shouldn't be any exceptions set off//
        String noException = null;
        UserDaoInterface userDao = new UserDao();
        UserDaoInterface userDaoSQL = new UserDaoSQL();
        UserData validItem = new UserData("goodName", "okayPassword", "validEmail");

        try{
            userDao.addItem(validItem);
            userDaoSQL.addItem(validItem);
        } catch (DataAccessException ex){
            noException = ex.getMessage();
        }
        Assertions.assertNull(noException);
        cleanUpShop();
    }

    @Test
    @DisplayName("UserDao - bad add item test")
    public void userDaoBadAddItem(){
        //we're gonna try and add the same user twice... should be a no no//
        String exception = null;
        UserDaoInterface userDaoSQL = new UserDaoSQL();
        UserData duplicateItem = existingUser();

        try{
            userDaoSQL.addItem(duplicateItem);
            userDaoSQL.addItem(duplicateItem);
        } catch (DataAccessException ex){
            exception = ex.getMessage();
        }
        Assertions.assertNotNull(exception);
        cleanUpShop();
    }

    @Test
    @DisplayName("UserDao - good find item test")
    public void userDaoGoodFind(){
        setStage();
        String noException = null;
        UserDaoInterface userDaoSQL = new UserDaoSQL();
        UserData existingUserData = existingUser();
        UserData foundData = null;
        try{
            foundData = userDaoSQL.searchUser(existingUserData.username()); //searches by user//
        }catch (DataAccessException ex){
            noException = ex.getMessage();
        }
        Assertions.assertNull(noException);
        Assertions.assertEquals(existingUserData.username(), foundData.username());
        cleanUpShop();
    }


    @Test
    @DisplayName("UserDao - bad find item test")
    public void userDaoBadFind(){
        setStage();
        String stillNoException = null;
        UserDaoInterface userDaoSQL = new UserDaoSQL();
        String madeUpUser = "notEvenARealUser";
        UserData foundData = null;
        try{
            foundData = userDaoSQL.searchUser(madeUpUser); //searches by user//
        }catch (DataAccessException ex){
            stillNoException = ex.getMessage();
        }
        Assertions.assertNull(stillNoException);
        Assertions.assertNull(foundData);
        cleanUpShop();
    }

    @Test
    @DisplayName("AuthDao - good clear test")
    public void userDaoGoodClear(){
        UserDaoInterface testUserDao = new UserDaoSQL();
        String noExcepction = null;
        try{
            testUserDao.clearDAO();
        }catch (DataAccessException ex){
            noExcepction = ex.getMessage();
        }
        Assertions.assertNull(noExcepction);
        cleanUpShop();
    }

    //GAME DAO TESTS//

    @Test
    @DisplayName("GameDao - good add item test")
    public void gameDaoGoodAddItem(){
        //adding good data, there shouldn't be any exceptions set off//
        String noException = null;
        GamesDaoInterface gameDao = new GamesDao();
        GamesDaoInterface gameDaoSQL = new GamesDaoSQL();
        GameData validGame = new GameData(99, null, null, "game99", new ChessGame());

        try{
            gameDao.addGame(validGame);
            gameDaoSQL.addGame(validGame);
        } catch (DataAccessException ex){
            noException = ex.getMessage();
        }
        Assertions.assertNull(noException);
        cleanUpShop();
    }

    @Test
    @DisplayName("GameDao - bad add item test")
    public void gameDaoBadAddItem(){
        //duplicate data should make SQL mad//
        setStage();
        String exception = null;
        GamesDaoInterface gameDao = new GamesDao();
        GamesDaoInterface gameDaoSQL = new GamesDaoSQL();
        GameData duplicateData = existingGame();

        try{
            gameDaoSQL.addGame(duplicateData); //already in system cause of setStage();//
        } catch (DataAccessException ex){
            exception = ex.getMessage();
        }
        Assertions.assertNotNull(exception);
        cleanUpShop();
    }

    @Test
    @DisplayName("GameDao - good update test")
    public void gameDaoGoodUpdate(){
        //loading in a valid game//
        setStage();
        String noException = null;
        GamesDaoInterface gameDaoSQL = new GamesDaoSQL();

        GameData before = existingGame();
        GameData expectedAfter = new GameData(before.gameID(), before.whiteUsername(), "update!", before.gameName(), before.game());
        GameData actualAfter = null;
        try{
            gameDaoSQL.updateGame(expectedAfter);
            actualAfter = gameDaoSQL.findGame(before.gameID());
        } catch (DataAccessException ex){
            noException = ex.getMessage();
        }
        Assertions.assertNull(noException);
        Assertions.assertNotNull(actualAfter.blackUsername());
        Assertions.assertEquals(actualAfter.blackUsername(), expectedAfter.blackUsername());
        cleanUpShop();
    }

    @Test
    @DisplayName("GameDao - bad update test")
    public void gameDaoBadUpdate(){
        //we're going to try and update a game that doesn't exist//
        String exception = null;
        GamesDaoInterface gameDaoSQL = new GamesDaoSQL();
        GameData notRealGame = new GameData(1000, null, null, "notRealGame", new ChessGame());

        try{
            gameDaoSQL.updateGame(notRealGame);
        } catch (DataAccessException ex){
            exception = ex.getMessage();
        }

        Assertions.assertNotNull(exception);
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
