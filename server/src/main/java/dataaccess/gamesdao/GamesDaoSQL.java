package dataaccess.gamesdao;

import models.GameData;

import java.util.HashSet;

public class GamesDaoSQL implements GamesDaoInterface{

    public GamesDaoSQL(){}
    @Override
    public void addGame(GameData game) {
        //TODO: implement
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
    public void clearDAO() {
        //TODO: implement
    }
}
