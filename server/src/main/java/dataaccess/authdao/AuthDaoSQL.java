package dataaccess.authdao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import models.AuthData;
import models.UserData;

import java.sql.SQLException;

public class AuthDaoSQL implements AuthDaoInterface {

    public AuthDaoSQL(){
        /*try{
            addItem(new AuthData("rex's special auth token", "RexOkland"));
        }catch(DataAccessException ex){
            System.out.println("error in the AuthDaoSQL contructor");
        }*/
    }
    @Override
    public void addItem(AuthData item) throws DataAccessException{
        var conn = DatabaseManager.getConnection();

        String sql = "INSERT INTO authentication (token, username) VALUES (?, ?)";
        try{
            var preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, item.authToken());
            preparedStatement.setString(2, item.username());

            preparedStatement.executeUpdate();
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void removeItem(AuthData item) throws DataAccessException {
        var conn = DatabaseManager.getConnection();

        String sql = "DELETE FROM authentication WHERE `token` =" + "\"" + item.authToken() + "\"";
        try{
            var preparedStatement = conn.prepareStatement(sql);

            preparedStatement.executeUpdate();
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
        try {conn.close();}
        catch(SQLException ex){
            throw new DataAccessException("failed to close connection");
        }
    }

    @Override
    public AuthData findAuth(String token) throws DataAccessException {
        var conn = DatabaseManager.getConnection();

        String sql = "SELECT * FROM authentication WHERE `token` = " + "\"" + token + "\"";
        try{
            var preparedStatement = conn.prepareStatement(sql);
            var queryResult = preparedStatement.executeQuery();

            String tokenData = null;
            String userData = null;

            while(queryResult.next()){
                tokenData = queryResult.getString("token");
                userData = queryResult.getString("username");
            }

            conn.close();

            if(tokenData == null){return null;}
            else{return new AuthData(tokenData, userData);}


        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }

    }

    @Override
    public void clearDAO() throws DataAccessException {
        var conn = DatabaseManager.getConnection();

        String sql = "TRUNCATE TABLE authentication";
        try{
            var preparedStatement = conn.prepareStatement(sql);

            preparedStatement.executeUpdate();

            conn.close();
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
}
