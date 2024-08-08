package websocket.messages;

import models.GameData;

public class UpdateBoardMessage extends ServerMessage{
    GameData game;
    public UpdateBoardMessage(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public GameData giveGameData(){
        return this.game;
    }
}
