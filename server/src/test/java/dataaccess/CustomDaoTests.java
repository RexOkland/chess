package dataaccess;

import dataaccess.authdao.AuthDao;
import dataaccess.authdao.AuthDaoInterface;
import dataaccess.authdao.AuthDaoSQL;
import models.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    }

}
