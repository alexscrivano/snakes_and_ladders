package org.example.game.turns;

import org.example.game.GameManager;

public interface State {
    void move(GameManager game, int player);
}
