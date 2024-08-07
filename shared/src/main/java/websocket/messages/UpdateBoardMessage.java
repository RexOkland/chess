package websocket.messages;

import chess.ChessGame;

public class UpdateBoardMessage extends ServerMessage{
    ChessGame game;
    public UpdateBoardMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
}
