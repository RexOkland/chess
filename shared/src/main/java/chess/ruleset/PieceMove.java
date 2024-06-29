package chess.ruleset;

import chess.*;
import java.util.Collection;

public interface PieceMove { //interface = abstract class//
    Collection<ChessMove> calculateMoves (ChessBoard board, ChessPiece piece, ChessPosition location);
}
