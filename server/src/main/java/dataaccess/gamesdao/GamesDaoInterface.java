package dataaccess.gamesdao;

import models.GameData;

public interface GamesDaoInterface {

    public GameData findGame(String gameName);
    public GameData findGame(int gameID);

    public void updateGame(GameData game);

    public void clearDAO();

}
