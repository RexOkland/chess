package dataaccess.gamesdao;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import models.AuthData;
import models.GameData;
import models.UserData;

import java.sql.SQLException;
import java.util.HashSet;

public class GamesDaoSQL implements GamesDaoInterface{

    public GamesDaoSQL(){}
    @Override
    public void addGame(GameData game) throws DataAccessException {
        var conn = DatabaseManager.getConnection();

        String sql = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, gameData)"
                +" VALUES (?, ?, ?, ?, ?)";
        try{
            var preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, game.gameID());
            preparedStatement.setString(2, game.whiteUsername());
            preparedStatement.setString(3, game.blackUsername());
            preparedStatement.setString(4, game.gameName());

            Gson gson = new Gson();
            String chessData = gson.toJson(game.game());

            preparedStatement.setString(5, chessData);

            preparedStatement.executeUpdate();
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public HashSet<GameData> getAllGames() throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        HashSet<GameData> foundGames = new HashSet<GameData>();

        String sql = "SELECT * FROM game";
        try{
            var preparedStatement = conn.prepareStatement(sql);
            var queryResult = preparedStatement.executeQuery();

            Integer id = null;
            String white = null;
            String black = null;
            String name = null;
            String data = null;

            while (queryResult.next()) {
                id = queryResult.getInt("gameID");
                white = queryResult.getString("whiteUsername");
                black = queryResult.getString("blackUsername");
                name = queryResult.getString("gameName");
                data = queryResult.getString("gameData");

                Gson gson = new Gson();
                ChessGame formattedData = gson.fromJson(data, ChessGame.class);

                GameData foundGame = new GameData(id, white, black, name, formattedData);
                foundGames.add(foundGame);

            }
            conn.close();
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }

        return foundGames;
    }

    @Override
    public GameData findGame(String gameName) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE gameName = ?";
        return findGameLogic(sql, gameName);
    }

    @Override
    public GameData findGame(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE gameID = ?";
        return findGameLogic(sql, gameID);
    }

    //chatGPT used my two original codes - which were identical besides the parameter - into this helper function//
    private GameData findGameLogic(String sql, Object parameter) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {

            // Set the parameter for the SQL statement
            if (parameter instanceof String) {
                preparedStatement.setString(1, (String) parameter);
            } else if (parameter instanceof Integer) {
                preparedStatement.setInt(1, (Integer) parameter);
            }

            var queryResult = preparedStatement.executeQuery();

            Integer foundID = null;
            String foundWhiteUser = null;
            String foundBlackUser = null;
            String foundGameName = null;
            String foundGameData = null;

            while (queryResult.next()) {
                foundID = queryResult.getInt("gameID");
                foundWhiteUser = queryResult.getString("whiteUsername");
                foundBlackUser = queryResult.getString("blackUsername");
                foundGameName = queryResult.getString("gameName");
                foundGameData = queryResult.getString("gameData");
            }

            Gson gson = new Gson();
            ChessGame formattedGameData = gson.fromJson(foundGameData, ChessGame.class);
            conn.close();

            if (foundID == null) {return null;}
            else {
                return new GameData(foundID, foundWhiteUser, foundBlackUser, foundGameName, formattedGameData);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    private void removeGame(Integer id) throws DataAccessException {
        var conn = DatabaseManager.getConnection();

        String sql = "DELETE FROM game WHERE `gameID` =" + "\"" + id + "\"";
        try{
            var preparedStatement = conn.prepareStatement(sql);

            preparedStatement.executeUpdate();
            conn.close();
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }


    @Override
    public void updateGame(GameData game) throws DataAccessException{
        GameData foundGame = this.findGame(game.gameID());
        if(foundGame == null){throw new DataAccessException("this game don't exist...");}
        this.removeGame(game.gameID());
        this.addGame(game);
    }

    @Override
    public void clearDAO() throws DataAccessException {
        var conn = DatabaseManager.getConnection();

        String sql = "TRUNCATE TABLE game";
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
