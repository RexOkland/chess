package dataaccess.gamesdao;

import models.GameData;

import java.util.HashSet;

public interface GamesDaoInterface {

    public void addGame(GameData game);
    public HashSet<GameData> getAllGames();
    public GameData findGame(String gameName);
    public GameData findGame(int gameID);
    public void updateGame(GameData game);
    public void clearDAO();

}
