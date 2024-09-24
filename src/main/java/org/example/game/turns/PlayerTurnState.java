package org.example.game.turns;

import org.example.game.GameManager;
import org.example.support.GameType;
import org.example.support.Player;

public interface PlayerTurnState {
    void move(GameManager game, Player player);
}
