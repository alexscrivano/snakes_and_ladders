package org.example.game.game_saver;

import org.example.board_components.boards.GameBoard;
import org.example.game.GameManager;
import org.example.support.GameType;

import java.io.Serializable;

public abstract class GameSaver {


    abstract void save(GameManager game, String saveName);
    abstract GameManager load(String saveName);
}
