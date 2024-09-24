package org.example.game.turns;

import org.example.game.GameManager;

public interface PlayerTurnState {
    void move(GameManager game, int player);
}
