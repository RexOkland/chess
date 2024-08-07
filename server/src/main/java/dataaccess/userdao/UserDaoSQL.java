package dataaccess.userdao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import models.UserData;
import org.eclipse.jetty.server.Authentication;

import java.sql.SQLException;

public class UserDaoSQL implements UserDaoInterface {

    public UserDaoSQL(){}
    @Override
    public void addItem(UserData item) throws DataAccessException {
        var conn = DatabaseManager.getConnection();

        String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try{
            var preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, item.username());
            preparedStatement.setString(2, item.password());
            preparedStatement.setString(3, item.email());

            preparedStatement.executeUpdate();

            conn.close();
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public UserData searchUser(String user) throws DataAccessException {
        var conn = DatabaseManager.getConnection();

        String sql = "SELECT * FROM user WHERE `username` = " + "\"" + user + "\"";
        try{
            var preparedStatement = conn.prepareStatement(sql);
            var queryResult = preparedStatement.executeQuery();

            String userData = null;
            String passwordData = null;
            String emailData = null;

            while(queryResult.next()){
                userData = queryResult.getString("username");
                passwordData = queryResult.getString("password");
                emailData = queryResult.getString("email");
            }

            conn.close();

            if(userData == null){return null;}
            else{return new UserData(userData, passwordData, emailData);}
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void clearDAO() throws DataAccessException {
        var conn = DatabaseManager.getConnection();

        String sql = "TRUNCATE TABLE user";
        try{
            var preparedStatement = conn.prepareStatement(sql);

            preparedStatement.executeUpdate();

            conn.close();;
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
}
