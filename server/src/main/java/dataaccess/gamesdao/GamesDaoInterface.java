package dataaccess.gamesdao;

import dataaccess.DataAccessException;
import models.GameData;

import java.util.HashSet;

public interface GamesDaoInterface {

    public void addGame(GameData game) throws DataAccessException;
    public HashSet<GameData> getAllGames() throws DataAccessException;
    public GameData findGame(String gameName) throws DataAccessException;
    public GameData findGame(int gameID) throws DataAccessException;
    public void updateGame(GameData game) throws DataAccessException;
    public void clearDAO();

}
