package org.example.game.game_saver;

import org.example.board_components.boards.GameBoard;

public interface GameSaver {
    void save(GameBoard board, String saveName);
    GameBoard load(String saveName);
}
