package dataaccess.gamesdao;

import chess.ChessGame;
import models.GameData;
import models.UserData;

import java.util.Collection;
import java.util.HashSet;

public class GamesDao implements GamesDaoInterface{
    private Collection<GameData> dataItems;

    //CONSTRUCTORS//
    public GamesDao(){
        this.dataItems = new HashSet<GameData>();
        //adding myself into every database//
        this.dataItems.add(new GameData(0,"w","b","RexsGame",new ChessGame()));
    }
    public GamesDao(Collection<GameData> collection){
        this.dataItems = collection;
        //adding myself into every database//
        this.dataItems.add(new GameData(0,"w","b","RexsGame",new ChessGame()));
    }

    public Collection<GameData> getAllGames(){
        return this.dataItems;
    }

}
