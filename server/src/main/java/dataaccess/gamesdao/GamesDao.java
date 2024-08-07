package dataaccess.gamesdao;

import chess.ChessGame;
import dataaccess.DatabaseHolder;
import dataaccess.authdao.AuthDao;
import models.GameData;
import models.UserData;

import java.util.Collection;
import java.util.HashSet;

public class GamesDao implements GamesDaoInterface{
    private HashSet<GameData> dataItems;

    //CONSTRUCTORS//
    public GamesDao(){
        this.dataItems = new HashSet<GameData>();
        //adding myself into every database//
        //this.dataItems.add(new GameData(0,"w","b","RexsGame",new ChessGame()));
    }
    public GamesDao(HashSet<GameData> collection){
        this.dataItems = collection;
        //adding myself into every database//
        //this.dataItems.add(new GameData(0,"w","b","RexsGame",new ChessGame()));
    }

    public void addGame(GameData game){
        this.dataItems.add(game);
    }
    public HashSet<GameData> getAllGames(){
        return this.dataItems;
    }

    public GameData findGame(String gameName){
        for(GameData g : this.dataItems){
            if(g.gameName().equals(gameName)){
                return g;
            }
        }
        return null;
    }
    public GameData findGame(int gameID){
        for(GameData g : this.dataItems){
            if(g.gameID() == gameID){
                return g;
            }
        }
        return null;
    }

    public void updateGame(GameData game){
        this.dataItems.remove( findGame(game.gameID()) );//remove the old//
        addGame(game);
    }

    public void clearDAO(){
        this.dataItems.clear();
    }

}
