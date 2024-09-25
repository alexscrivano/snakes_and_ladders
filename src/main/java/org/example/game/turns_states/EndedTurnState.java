package org.example.game.turns_states;

import org.example.game.GameManager;
import org.example.support.Player;

public class EndedTurnState implements PlayerTurnState {
    @Override
    public void move(GameManager game, Player player) {} //do nothing
}
