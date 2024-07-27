package dataaccess.gamesdao;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import models.AuthData;
import models.GameData;

import java.sql.SQLException;
import java.util.HashSet;

public class GamesDaoSQL implements GamesDaoInterface{

    public GamesDaoSQL(){
        try{
            addGame(new GameData(0, null,null,"rexsDefaultGame", new ChessGame()));
        }catch(DataAccessException ex){
            System.out.println("error in the GamesDaoSQL contructor");
        }
    }
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
    public HashSet<GameData> getAllGames() {
        //TODO: implement
        return null;
    }

    @Override
    public GameData findGame(String gameName) {
        //TODO: implement
        return null;
    }

    @Override
    public GameData findGame(int gameID) {
        //TODO: implement
        return null;
    }

    @Override
    public void updateGame(GameData game) {
        //TODO: implement
    }

    @Override
    public void clearDAO() throws DataAccessException {
        var conn = DatabaseManager.getConnection();

        String sql = "TRUNCATE TABLE game";
        try{
            var preparedStatement = conn.prepareStatement(sql);

            preparedStatement.executeUpdate();
        }
        catch(SQLException ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
}
